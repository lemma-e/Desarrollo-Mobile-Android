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

class MovieDetailsViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val _uiState = MutableLiveData<MovieDetailsUiState>()
    val uiState: LiveData<MovieDetailsUiState> = _uiState

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
}