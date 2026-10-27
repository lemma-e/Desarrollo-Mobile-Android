package ar.edu.unicen.seminario.data.local

import android.content.Context
import ar.edu.unicen.seminario.model.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WatchlistRepository(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences("cinebox_watchlist", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val listType = object : TypeToken<List<Movie>>() {}.type

    suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        val json = preferences.getString("movies", null)
        if (json == null) {
            emptyList()
        } else {
            gson.fromJson<List<Movie>>(json, listType) ?: emptyList()
        }
    }

    suspend fun addMovie(movie: Movie) = withContext(Dispatchers.IO) {
        val movies = getMovies().toMutableList()
        if (movies.none { it.id == movie.id }) {
            movies.add(movie)
            preferences.edit().putString("movies", gson.toJson(movies)).apply()
        }
    }

    suspend fun removeMovie(movieId: Int) = withContext(Dispatchers.IO) {
        val movies = getMovies().filter { it.id != movieId }
        preferences.edit().putString("movies", gson.toJson(movies)).apply()
    }
}