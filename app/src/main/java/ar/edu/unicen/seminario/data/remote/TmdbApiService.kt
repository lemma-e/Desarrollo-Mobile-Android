package ar.edu.unicen.seminario.data.remote

import ar.edu.unicen.seminario.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface TmdbApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String
    ): PopularMoviesResponse
}