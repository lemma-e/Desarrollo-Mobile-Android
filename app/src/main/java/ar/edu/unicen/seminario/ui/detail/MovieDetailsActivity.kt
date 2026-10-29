package ar.edu.unicen.seminario.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ActivityMovieDetailsBinding
import ar.edu.unicen.seminario.model.MovieDetails
import com.bumptech.glide.Glide
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import ar.edu.unicen.seminario.model.Movie
import ar.edu.unicen.seminario.ui.popular.MoviesUiState
import android.net.Uri
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.ui.SystemBarsHelper
import com.google.android.material.chip.Chip

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var similarAdapter: MovieCarouselAdapter
    private lateinit var recommendedAdapter: MovieCarouselAdapter
    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.detailsToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        SystemBarsHelper.applyInsets(binding.root)
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        if (movieId <= 0) {
            binding.detailsErrorLayout.visibility = View.VISIBLE
            binding.detailsErrorTextView.setText(R.string.details_invalid_id)
            binding.detailsRetryButton.visibility = View.GONE
            return
        }
        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        similarAdapter = MovieCarouselAdapter { movie -> openMovieDetails(movie) }
        recommendedAdapter = MovieCarouselAdapter { movie -> openMovieDetails(movie) }
        binding.similarMoviesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.similarMoviesRecyclerView.adapter = similarAdapter
        binding.recommendedMoviesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedMoviesRecyclerView.adapter = recommendedAdapter
        configurarIndicadorCarrusel(binding.similarMoviesRecyclerView, binding.similarScrollHintTextView)
        configurarIndicadorCarrusel(binding.recommendedMoviesRecyclerView, binding.recommendedScrollHintTextView)
        viewModel.isSaved.observe(this) { saved ->
            if (saved) {
                binding.watchlistButton.setText(R.string.remove_from_watchlist)
                binding.watchlistButton.setIconResource(R.drawable.ic_details_bookmark)
            } else {
                binding.watchlistButton.setText(R.string.add_to_watchlist)
                binding.watchlistButton.setIconResource(R.drawable.ic_details_bookmark_border)
            }
        }
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is MovieDetailsUiState.Loading -> {
                    binding.detailsProgressBar.visibility = View.VISIBLE
                    binding.detailsScrollView.visibility = View.GONE
                    binding.detailsErrorLayout.visibility = View.GONE
                }
                is MovieDetailsUiState.Success -> {
                    binding.detailsProgressBar.visibility = View.GONE
                    binding.detailsScrollView.visibility = View.VISIBLE
                    binding.detailsErrorLayout.visibility = View.GONE
                    showMovieDetails(state.movie)
                    if (viewModel.similarMoviesState.value == null) {
                        viewModel.loadSimilarMovies(state.movie.id)
                    }
                    if (viewModel.recommendedMoviesState.value == null) {
                        viewModel.loadRecommendedMovies(state.movie.id)
                    }
                    viewModel.checkWatchlist(state.movie.id)
                    binding.watchlistButton.setOnClickListener {
                        viewModel.toggleWatchlist(state.movie)
                    }
                    binding.shareMovieButton.setOnClickListener {
                        val shareText = "${state.movie.title}\n\n${state.movie.overview.orEmpty()}"
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_movie)))
                    }
                    val homepage = state.movie.homepage
                    if (!homepage.isNullOrBlank() && (homepage.startsWith("https://") || homepage.startsWith("http://"))) {
                        binding.openHomepageButton.visibility = View.VISIBLE
                        binding.openHomepageButton.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(homepage))
                            startActivity(intent)
                        }
                    } else {
                        binding.openHomepageButton.visibility = View.GONE
                    }
                }
                is MovieDetailsUiState.Error -> {
                    binding.detailsProgressBar.visibility = View.GONE
                    binding.detailsScrollView.visibility = View.GONE
                    binding.detailsErrorLayout.visibility = View.VISIBLE
                    val errorMessage = when (state.reason) {
                        MovieDetailsLoadError.CONNECTION -> R.string.error_connection
                        MovieDetailsLoadError.SERVER -> R.string.error_server
                        MovieDetailsLoadError.UNKNOWN -> R.string.details_load_error
                    }
                    binding.detailsErrorTextView.setText(errorMessage)
                }
            }
        }
        binding.detailsRetryButton.setOnClickListener {
            viewModel.loadMovieDetails(movieId)
        }
        binding.similarMoviesRetryButton.setOnClickListener {
            viewModel.loadSimilarMovies(movieId)
        }
        binding.recommendedMoviesRetryButton.setOnClickListener {
            viewModel.loadRecommendedMovies(movieId)
        }
        if (viewModel.uiState.value == null) {
            viewModel.loadMovieDetails(movieId)
        }

        viewModel.similarMoviesState.observe(this) { state ->
            binding.similarMoviesRecyclerView.visibility = View.GONE
            binding.similarScrollHintTextView.visibility = View.GONE
            binding.similarMoviesStatusLayout.visibility = View.VISIBLE
            binding.similarMoviesProgressBar.visibility = View.GONE
            binding.similarMoviesRetryButton.visibility = View.GONE
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.similarMoviesProgressBar.visibility = View.VISIBLE
                    binding.similarMoviesMessageTextView.setText(R.string.cinebox_details_loading_movies)
                }
                is MoviesUiState.Success -> {
                    similarAdapter.updateMovies(state.movies)
                    if (state.movies.isEmpty()) {
                        binding.similarMoviesMessageTextView.setText(R.string.cinebox_details_similar_empty)
                    } else {
                        binding.similarMoviesStatusLayout.visibility = View.GONE
                        binding.similarMoviesRecyclerView.visibility = View.VISIBLE
                        binding.similarMoviesRecyclerView.post {
                            actualizarIndicadorCarrusel(binding.similarMoviesRecyclerView, binding.similarScrollHintTextView)
                        }
                    }
                }
                is MoviesUiState.Error -> {
                    binding.similarMoviesMessageTextView.setText(R.string.cinebox_details_carousel_error)
                    binding.similarMoviesRetryButton.visibility = View.VISIBLE
                }
            }
        }
        viewModel.recommendedMoviesState.observe(this) { state ->
            binding.recommendedMoviesRecyclerView.visibility = View.GONE
            binding.recommendedScrollHintTextView.visibility = View.GONE
            binding.recommendedMoviesStatusLayout.visibility = View.VISIBLE
            binding.recommendedMoviesProgressBar.visibility = View.GONE
            binding.recommendedMoviesRetryButton.visibility = View.GONE
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.recommendedMoviesProgressBar.visibility = View.VISIBLE
                    binding.recommendedMoviesMessageTextView.setText(R.string.cinebox_details_loading_movies)
                }
                is MoviesUiState.Success -> {
                    recommendedAdapter.updateMovies(state.movies)
                    if (state.movies.isEmpty()) {
                        binding.recommendedMoviesMessageTextView.setText(R.string.cinebox_details_recommended_empty)
                    } else {
                        binding.recommendedMoviesStatusLayout.visibility = View.GONE
                        binding.recommendedMoviesRecyclerView.visibility = View.VISIBLE
                        binding.recommendedMoviesRecyclerView.post {
                            actualizarIndicadorCarrusel(binding.recommendedMoviesRecyclerView, binding.recommendedScrollHintTextView)
                        }
                    }
                }
                is MoviesUiState.Error -> {
                    binding.recommendedMoviesMessageTextView.setText(R.string.cinebox_details_carousel_error)
                    binding.recommendedMoviesRetryButton.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun showMovieDetails(movie: MovieDetails) {
        binding.movieTitleTextView.text = movie.title
        binding.movieOverviewTextView.text =
            movie.overview?.takeIf { it.isNotBlank() } ?: getString(R.string.details_no_overview)
        val rating = getString(R.string.movie_rating_value, movie.voteAverage)
        binding.movieRatingTextView.text = rating
        binding.movieRatingTextView.contentDescription = "${getString(R.string.movie_rating)}: $rating"
        binding.movieGenresChipGroup.removeAllViews()
        if (movie.genres.isEmpty()) {
            binding.movieGenresChipGroup.visibility = View.GONE
            binding.movieGenresTextView.visibility = View.VISIBLE
            binding.movieGenresTextView.setText(R.string.details_no_genres)
        } else {
            binding.movieGenresChipGroup.visibility = View.VISIBLE
            binding.movieGenresTextView.visibility = View.GONE
            movie.genres.forEach { genre ->
                val chip = Chip(this).apply {
                    text = genre.name
                    isCheckable = false
                    isClickable = false
                    isFocusable = false
                }
                binding.movieGenresChipGroup.addView(chip)
            }
        }
        val posterUrl = movie.posterPath?.takeIf { it.isNotBlank() }?.let { "https://image.tmdb.org/t/p/w342$it" }
        binding.moviePosterImageView.visibility =
            if (posterUrl == null) View.GONE
                else View.VISIBLE
        Glide.with(this)
            .load(posterUrl)
            .into(binding.moviePosterImageView)
    }
    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(EXTRA_MOVIE_ID, movie.id)
        startActivity(intent)
    }
    private fun configurarIndicadorCarrusel(recyclerView: RecyclerView, hintTextView: TextView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                actualizarIndicadorCarrusel(recyclerView, hintTextView)
            }
        })
    }
    private fun actualizarIndicadorCarrusel(recyclerView: RecyclerView, hintTextView: TextView) {
        if (recyclerView.visibility != View.VISIBLE) {
            hintTextView.visibility = View.GONE
            return
        }
        val puedeIrIzquierda = recyclerView.canScrollHorizontally(-1)
        val puedeIrDerecha = recyclerView.canScrollHorizontally(1)
        when {
            puedeIrIzquierda && puedeIrDerecha -> {
                hintTextView.setText(R.string.carousel_scroll_hint_both)
            }
            puedeIrDerecha -> {
                hintTextView.setText(R.string.carousel_scroll_hint)
            }
            puedeIrIzquierda -> {
                hintTextView.setText(R.string.carousel_scroll_hint_back)
            }
            else -> {
                hintTextView.visibility = View.GONE
                return
            }
        }
        hintTextView.visibility = View.VISIBLE
    }
}