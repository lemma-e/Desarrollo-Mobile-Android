package ar.edu.unicen.seminario.data.repository

import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.data.remote.TmdbRetrofitClient
import ar.edu.unicen.seminario.model.Movie
import ar.edu.unicen.seminario.model.MovieDetails

class MovieRepository {
    private val authorization = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}"
    suspend fun getPopularMovies(): List<Movie> {
        val response = TmdbRetrofitClient.apiService.getPopularMovies(authorization)
        return response.results
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return TmdbRetrofitClient.apiService.getMovieDetails(movieId, authorization)
    }

    suspend fun searchMovies(query: String): List<Movie> {

        val response = TmdbRetrofitClient.apiService
            .searchMovies(query, authorization)

        return response.results
    }
}