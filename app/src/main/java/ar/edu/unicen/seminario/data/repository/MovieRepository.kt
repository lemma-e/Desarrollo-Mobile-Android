package ar.edu.unicen.seminario.data.repository

import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.data.remote.TmdbRetrofitClient
import ar.edu.unicen.seminario.model.Movie
import ar.edu.unicen.seminario.model.MovieDetails
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ar.edu.unicen.seminario.data.paging.MoviePagingSource
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class MovieRepository {
    private val authorization = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}"
    private val language = Locale.getDefault().toLanguageTag()
    suspend fun getPopularMovies(): List<Movie> {
        val response = TmdbRetrofitClient.apiService.getPopularMovies(authorization, language = language)
        return response.results
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return TmdbRetrofitClient.apiService.getMovieDetails(movieId, authorization, language)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        val response = TmdbRetrofitClient.apiService.searchMovies(query, authorization, language)
        return response.results
    }

    fun getPagedPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(
                    TmdbRetrofitClient.apiService,
                    authorization,
                    language
                )
            }
        ).flow
    }

    suspend fun getTrendingMovies(): List<Movie> {
        val response = TmdbRetrofitClient.apiService.getTrendingMovies(authorization, language)
        return response.results
    }
    suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        val response = TmdbRetrofitClient.apiService.getSimilarMovies(movieId, authorization, language)
        return response.results
    }

    suspend fun getRecommendedMovies(movieId: Int): List<Movie> {
        val response = TmdbRetrofitClient.apiService
            .getRecommendedMovies(movieId, authorization, language)
        return response.results
    }
}