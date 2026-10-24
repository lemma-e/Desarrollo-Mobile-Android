package ar.edu.unicen.seminario.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,

    @SerializedName("poster_path")
    val posterPath: String?
)