package ar.edu.unicen.seminario.data.repository

import ar.edu.unicen.seminario.BuildConfig
import ar.edu.unicen.seminario.data.remote.TmdbRetrofitClient
import ar.edu.unicen.seminario.model.Movie

class MovieRepository {
    suspend fun getPopularMovies(): List<Movie> {
        val authorization = "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}"
        val response = TmdbRetrofitClient.apiService.getPopularMovies(authorization)
        return response.results
    }
}