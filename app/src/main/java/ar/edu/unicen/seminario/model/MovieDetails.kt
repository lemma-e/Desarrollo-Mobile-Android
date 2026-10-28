package ar.edu.unicen.seminario.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    val overview: String?,
    val genres: List<MovieGenre>,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val homepage: String? = null
)
data class MovieGenre(
    val id: Int,
    val name: String
)