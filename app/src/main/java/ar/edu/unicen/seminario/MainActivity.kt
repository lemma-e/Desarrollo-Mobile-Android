package ar.edu.unicen.seminario

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.unicen.seminario.databinding.ActivityMainBinding
import ar.edu.unicen.seminario.ui.popular.MovieAdapter
import ar.edu.unicen.seminario.ui.popular.MovieLoadError
import ar.edu.unicen.seminario.ui.popular.MovieViewModel
import ar.edu.unicen.seminario.ui.popular.MoviesUiState

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieAdapter = MovieAdapter()
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.moviesRecyclerView.adapter = movieAdapter
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                is MoviesUiState.Success -> {
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.moviesRecyclerView.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE

                    movieAdapter.updateMovies(state.movies)
                }
                is MoviesUiState.Error -> {
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE

                    val errorMessage = when (state.reason) {
                        MovieLoadError.CONNECTION -> R.string.error_connection
                        MovieLoadError.SERVER -> R.string.error_server
                        MovieLoadError.UNKNOWN -> R.string.movies_load_error
                    }
                    binding.errorTextView.setText(errorMessage)
                }
            }
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPopularMovies()
        }
    }
}