package ar.edu.unicen.seminario.data.remote

import ar.edu.unicen.seminario.model.MovieDetails
import ar.edu.unicen.seminario.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TmdbApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String
    ): PopularMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String
    ): MovieDetails
}