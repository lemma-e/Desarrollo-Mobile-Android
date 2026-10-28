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
import ar.edu.unicen.seminario.ui.watchlist.WatchlistViewModel
import ar.edu.unicen.seminario.ui.trending.TrendingViewModel
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var pagingAdapter: MoviePagingAdapter
    private var popularLoadStates: CombinedLoadStates? = null
    private lateinit var watchlistViewModel: WatchlistViewModel
    private lateinit var watchlistAdapter: MovieAdapter
    private var showingWatchlist = false
    private lateinit var trendingViewModel: TrendingViewModel
    private lateinit var trendingAdapter: MovieAdapter
    private var showingTrending = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movieAdapter = MovieAdapter { movie -> openMovieDetails(movie) }
        pagingAdapter = MoviePagingAdapter { movie -> openMovieDetails(movie) }
        watchlistAdapter = MovieAdapter { movie -> openMovieDetails(movie) }
        trendingAdapter = MovieAdapter { movie -> openMovieDetails(movie) }
        val pagingAdapterConFooter = pagingAdapter.withLoadStateFooter(
            footer = MovieLoadStateAdapter {
                pagingAdapter.retry()
            }
        )

        binding.moviesRecyclerView.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val gridLayoutManager = GridLayoutManager(this, 2)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (binding.moviesRecyclerView.adapter == pagingAdapterConFooter && position >= pagingAdapter.itemCount) {
                            2
                        } else {
                            1
                        }
                    }
                }
                gridLayoutManager
            } else {
                LinearLayoutManager(this)
            }
        binding.moviesRecyclerView.adapter = pagingAdapterConFooter
        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        watchlistViewModel = ViewModelProvider(this)[WatchlistViewModel::class.java]
        trendingViewModel = ViewModelProvider(this)[TrendingViewModel::class.java]
        pagingAdapter.addLoadStateListener {
            loadStates -> popularLoadStates = loadStates
            if (!showingWatchlist && !showingTrending && !isSearching()) {
                showPopularState()
            }
        }

        lifecycleScope.launch {
            viewModel.pagedPopularMovies.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        watchlistViewModel.movies.observe(this) {
            movies -> watchlistAdapter.updateMovies(movies)
            if (showingWatchlist) {
                binding.loadingProgressBar.visibility = View.GONE
                if (movies.isEmpty()) {
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.VISIBLE
                    binding.errorTextView.setText(R.string.empty_watchlist)
                    binding.retryButton.visibility = View.GONE
                } else {
                    binding.moviesRecyclerView.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
                }
            }
        }
        viewModel.uiState.observe(this) { state ->
            if (showingWatchlist || showingTrending || !isSearching()) {
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

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_popular -> {
                    showingWatchlist = false
                    showingTrending = false
                    binding.popularMoviesTitleTextView.setText(R.string.popular_movies)
                    binding.searchMovieEditText.visibility = View.VISIBLE
                    if (isSearching()) {
                        binding.moviesRecyclerView.adapter = movieAdapter
                        viewModel.searchMovies(binding.searchMovieEditText.text.toString())
                    } else {
                        binding.moviesRecyclerView.adapter = pagingAdapterConFooter
                        showPopularState()
                    }
                    true
                }
                R.id.navigation_trending -> {
                    showingWatchlist = false
                    showingTrending = true
                    binding.popularMoviesTitleTextView.setText(R.string.trending_movies)
                    binding.searchMovieEditText.visibility = View.GONE
                    binding.moviesRecyclerView.adapter = trendingAdapter
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    trendingViewModel.loadTrendingMovies()
                    true
                }
                R.id.navigation_watchlist -> {
                    showingWatchlist = true
                    binding.popularMoviesTitleTextView.setText(R.string.my_watchlist)
                    binding.searchMovieEditText.visibility = View.GONE
                    binding.moviesRecyclerView.adapter = watchlistAdapter
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                    watchlistViewModel.loadWatchlist()
                    true
                }
                else -> false
            }
        }

        trendingViewModel.uiState.observe(this) { state ->
            if (!showingTrending) {
                return@observe
            }
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.loadingProgressBar.visibility = View.VISIBLE
                    binding.moviesRecyclerView.visibility = View.GONE
                    binding.errorLayout.visibility = View.GONE
                }
                is MoviesUiState.Success -> {
                    trendingAdapter.updateMovies(state.movies)
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.moviesRecyclerView.visibility = View.VISIBLE
                    binding.errorLayout.visibility = View.GONE
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
                    if (showingWatchlist || showingTrending) {
                        return
                    }
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
            if (showingTrending) {
                trendingViewModel.loadTrendingMovies()
            } else if (isSearching()) {
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