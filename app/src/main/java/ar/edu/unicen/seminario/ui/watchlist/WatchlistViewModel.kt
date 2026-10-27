package ar.edu.unicen.seminario.ui.watchlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.data.local.WatchlistRepository
import ar.edu.unicen.seminario.model.Movie
import kotlinx.coroutines.launch

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WatchlistRepository(application)
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun loadWatchlist() {
        viewModelScope.launch {
            _movies.value = repository.getMovies()
        }
    }
}