package ar.edu.unicen.seminario.data.remote

import ar.edu.unicen.seminario.model.MovieDetails
import ar.edu.unicen.seminario.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int = 1
    ): PopularMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Header("Authorization") authorization: String
    ): MovieDetails

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Header("Authorization") authorization: String
    ): PopularMoviesResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Header("Authorization") authorization: String
    ): PopularMoviesResponse
}