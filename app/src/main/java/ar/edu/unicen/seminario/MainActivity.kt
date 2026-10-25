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
import ar.edu.unicen.seminario.ui.detail.MovieDetailsActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import ar.edu.unicen.seminario.model.Movie
import ar.edu.unicen.seminario.ui.popular.MoviePagingAdapter
import ar.edu.unicen.seminario.ui.popular.MovieLoadStateAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var pagingAdapter: MoviePagingAdapter
    private var popularLoadStates: CombinedLoadStates? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieAdapter = MovieAdapter { movie -> openMovieDetails(movie) }
        pagingAdapter = MoviePagingAdapter { movie -> openMovieDetails(movie) }
        val pagingAdapterConFooter = pagingAdapter.withLoadStateFooter(
            footer = MovieLoadStateAdapter {
                pagingAdapter.retry()
            }
        )
        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.moviesRecyclerView.adapter = pagingAdapterConFooter
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        pagingAdapter.addLoadStateListener {
            loadStates -> popularLoadStates = loadStates
            if (!isSearching()) {
                showPopularState()
            }
        }

        lifecycleScope.launch {
            viewModel.pagedPopularMovies.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        viewModel.uiState.observe(this) { state ->
            if (!isSearching()) {
                return@observe
            }

            when (state) {
                is MoviesUiState.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                is MoviesUiState.Success -> {
                    if (!state.isSearch) {
                        return@observe
                    }
                    binding.loadingProgressBar.visibility = View.GONE

                    if (state.movies.isEmpty()) {
                        binding.moviesRecyclerView.visibility = View.GONE
                        binding.errorLayout.visibility = View.VISIBLE
                        binding.errorTextView.setText(R.string.no_search_results)
                        binding.retryButton.visibility = View.GONE
                    } else {
                        movieAdapter.updateMovies(state.movies)
                        binding.moviesRecyclerView.visibility = View.VISIBLE
                        binding.errorLayout.visibility = View.GONE
                    }
                }

                is MoviesUiState.Error -> {
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.retryButton.visibility = View.VISIBLE
                    val errorMessage = when (state.reason) {
                        MovieLoadError.CONNECTION -> R.string.error_connection
                        MovieLoadError.SERVER -> R.string.error_server
                        MovieLoadError.UNKNOWN -> R.string.movies_load_error
                    }
                    binding.errorTextView.setText(errorMessage)
                }
            }
        }

        binding.searchMovieEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val query = s?.toString().orEmpty()
                    if (query.isBlank()) {
                        if (binding.moviesRecyclerView.adapter != pagingAdapterConFooter) {
                            binding.moviesRecyclerView.adapter = pagingAdapterConFooter
                        }
                        showPopularState()
                    } else {
                        if (binding.moviesRecyclerView.adapter != movieAdapter) {
                            binding.moviesRecyclerView.adapter = movieAdapter
                        }
                    }
                    viewModel.searchMovies(query)
                }
                override fun afterTextChanged(s: Editable?) {
                }
            }
        )

        binding.retryButton.setOnClickListener {
            if (isSearching()) {
                viewModel.retry()
            } else {
                pagingAdapter.retry()
            }
        }
    }

    private fun isSearching(): Boolean {
        return binding.searchMovieEditText.text.toString().isNotBlank()
    }

    private fun showPopularState() {
        when (val state = popularLoadStates?.refresh) {
            is LoadState.Loading, null -> {
                binding.loadingProgressBar.visibility = View.VISIBLE
                binding.moviesRecyclerView.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
            }

            is LoadState.NotLoading -> {
                binding.loadingProgressBar.visibility = View.GONE
                binding.moviesRecyclerView.visibility = View.VISIBLE
                binding.errorLayout.visibility = View.GONE
            }

            is LoadState.Error -> {
                binding.loadingProgressBar.visibility = View.GONE
                binding.moviesRecyclerView.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
                binding.retryButton.visibility = View.VISIBLE

                val errorMessage = when (state.error) {
                    is IOException -> R.string.error_connection
                    is HttpException -> R.string.error_server
                    else -> R.string.movies_load_error
                }

                binding.errorTextView.setText(errorMessage)
            }
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE_ID, movie.id)
        startActivity(intent)
    }
}