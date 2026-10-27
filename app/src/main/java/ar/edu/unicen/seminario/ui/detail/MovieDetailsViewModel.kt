package ar.edu.unicen.seminario.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.data.repository.MovieRepository
import ar.edu.unicen.seminario.model.MovieDetails
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ar.edu.unicen.seminario.data.local.WatchlistRepository
import ar.edu.unicen.seminario.model.Movie

enum class MovieDetailsLoadError {
    CONNECTION,
    SERVER,
    UNKNOWN
}

sealed class MovieDetailsUiState {
    object Loading : MovieDetailsUiState()
    data class Success(val movie: MovieDetails) : MovieDetailsUiState()
    data class Error(val reason: MovieDetailsLoadError) : MovieDetailsUiState()
}

class MovieDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MovieRepository()
    private val _uiState = MutableLiveData<MovieDetailsUiState>()
    val uiState: LiveData<MovieDetailsUiState> = _uiState
    private val watchlistRepository = WatchlistRepository(application)
    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    fun loadMovieDetails(movieId: Int) {
        _uiState.value = MovieDetailsUiState.Loading
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(movieId)
                _uiState.value = MovieDetailsUiState.Success(movie)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value = MovieDetailsUiState.Error(
                    MovieDetailsLoadError.CONNECTION
                )
            } catch (e: HttpException) {
                _uiState.value = MovieDetailsUiState.Error(
                    MovieDetailsLoadError.SERVER
                )
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState.Error(
                    MovieDetailsLoadError.UNKNOWN
                )
            }
        }
    }
    fun checkWatchlist(movieId: Int) {
        viewModelScope.launch {
            val movies = watchlistRepository.getMovies()
            _isSaved.value = movies.any {
                it.id == movieId
            }
        }
    }

    fun toggleWatchlist(movie: MovieDetails) {
        viewModelScope.launch {
            val movies = watchlistRepository.getMovies()
            val saved = movies.any {
                it.id == movie.id
            }
            if (saved) {
                watchlistRepository.removeMovie(movie.id)
            } else {
                val newMovie = Movie(movie.id, movie.title, movie.posterPath)
                watchlistRepository.addMovie(newMovie)
            }
            _isSaved.value = !saved
        }
    }
}