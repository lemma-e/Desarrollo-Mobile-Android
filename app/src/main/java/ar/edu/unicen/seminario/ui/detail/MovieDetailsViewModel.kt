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
import ar.edu.unicen.seminario.ui.popular.MovieLoadError
import ar.edu.unicen.seminario.ui.popular.MoviesUiState

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
    private val _similarMoviesState = MutableLiveData<MoviesUiState>()
    val similarMoviesState: LiveData<MoviesUiState> = _similarMoviesState
    private val _recommendedMoviesState = MutableLiveData<MoviesUiState>()
    val recommendedMoviesState: LiveData<MoviesUiState> = _recommendedMoviesState

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

    fun loadSimilarMovies(movieId: Int) {
        _similarMoviesState.value = MoviesUiState.Loading
        viewModelScope.launch {
            try {
                val movies = repository.getSimilarMovies(movieId)
                _similarMoviesState.value = MoviesUiState.Success(movies)

            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _similarMoviesState.value = MoviesUiState.Error(MovieLoadError.CONNECTION)
            } catch (e: HttpException) {
                _similarMoviesState.value = MoviesUiState.Error(MovieLoadError.SERVER)
            } catch (e: Exception) {
                _similarMoviesState.value = MoviesUiState.Error(MovieLoadError.UNKNOWN)
            }
        }
    }

    fun loadRecommendedMovies(movieId: Int) {
        _recommendedMoviesState.value = MoviesUiState.Loading
        viewModelScope.launch {
            try {
                val movies = repository.getRecommendedMovies(movieId)
                _recommendedMoviesState.value = MoviesUiState.Success(movies)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _recommendedMoviesState.value = MoviesUiState.Error(MovieLoadError.CONNECTION)
            } catch (e: HttpException) {
                _recommendedMoviesState.value = MoviesUiState.Error(MovieLoadError.SERVER)

            } catch (e: Exception) {
                _recommendedMoviesState.value = MoviesUiState.Error(MovieLoadError.UNKNOWN)
            }
        }
    }
}