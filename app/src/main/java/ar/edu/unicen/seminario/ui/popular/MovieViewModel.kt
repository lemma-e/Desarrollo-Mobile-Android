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

enum class MovieLoadError {
    CONNECTION,
    SERVER,
    UNKNOWN
}

sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<Movie>) : MoviesUiState()
    data class Error(val reason: MovieLoadError) : MoviesUiState()
}

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val _uiState = MutableLiveData<MoviesUiState>()
    val uiState: LiveData<MoviesUiState> = _uiState

    init { loadPopularMovies() }

    fun loadPopularMovies() {
        _uiState.value = MoviesUiState.Loading
        viewModelScope.launch {
            try {
                val movies = repository.getPopularMovies()
                _uiState.value = MoviesUiState.Success(movies)

                //arreglar exepciones
                //añadidas los catch
                //si da un error me muero
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