package ar.edu.unicen.seminario.ui.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.unicen.seminario.data.repository.MovieRepository
import ar.edu.unicen.seminario.ui.popular.MovieLoadError
import ar.edu.unicen.seminario.ui.popular.MoviesUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TrendingViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val _uiState = MutableLiveData<MoviesUiState>()
    val uiState: LiveData<MoviesUiState> = _uiState

    fun loadTrendingMovies() {
        _uiState.value = MoviesUiState.Loading
        viewModelScope.launch {
            try {
                val movies = repository.getTrendingMovies()
                _uiState.value = MoviesUiState.Success(movies)
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
