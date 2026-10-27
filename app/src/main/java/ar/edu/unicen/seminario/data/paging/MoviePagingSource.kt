package ar.edu.unicen.seminario.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ar.edu.unicen.seminario.data.remote.TmdbApiService
import ar.edu.unicen.seminario.model.Movie
import kotlinx.coroutines.CancellationException

class MoviePagingSource(
    private val apiService: TmdbApiService,
    private val authorization: String,
    private val language: String = "en-US") : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getPopularMovies(authorization, page, language)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) {
                    null
                } else {
                    page + 1
                }
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition)
        return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
    }
}