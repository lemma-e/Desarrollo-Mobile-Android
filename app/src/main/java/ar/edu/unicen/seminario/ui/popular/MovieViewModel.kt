package ar.edu.unicen.seminario.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.data.repository.MovieRepository
import ar.edu.unicen.seminario.model.Movie
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

enum class MovieLoadError {
    CONNECTION,
    SERVER,
    UNKNOWN
}

sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>, val isSearch: Boolean = false) : MoviesUiState()
    data class Error(val reason: MovieLoadError) : MoviesUiState()
}

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val _uiState = MutableLiveData<MoviesUiState>()
    val uiState: LiveData<MoviesUiState> = _uiState
    private var currentQuery = ""
    private var requestJob: Job? = null

    init { loadPopularMovies() }

    fun loadPopularMovies() {
        currentQuery = ""
        loadMovies()
    }

    fun searchMovies(query: String) {
        currentQuery = query.trim()
        loadMovies()
    }

    fun retry() {
        loadMovies()
    }

    private fun loadMovies() {
        requestJob?.cancel()
        val query = currentQuery
        val isSearch = query.isNotEmpty()
        _uiState.value = MoviesUiState.Loading
        requestJob = viewModelScope.launch {
            try {
                val movies = if (isSearch) {
                    delay(350)
                    repository.searchMovies(query)
                } else {
                    repository.getPopularMovies()
                }
                _uiState.value = MoviesUiState.Success(
                    movies,
                    isSearch
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                _uiState.value = MoviesUiState.Error(MovieLoadError.CONNECTION)
            } catch (e: HttpException) {
                _uiState.value = MoviesUiState.Error(MovieLoadError.SERVER)
            } catch (e: Exception) {
                _uiState.value = MoviesUiState.Error(MovieLoadError.UNKNOWN)
            }
        }
    }
}