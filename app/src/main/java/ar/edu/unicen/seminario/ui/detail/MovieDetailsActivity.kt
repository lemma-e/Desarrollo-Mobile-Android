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
import ar.edu.unicen.seminario.ui.popular.MovieAdapter
import ar.edu.unicen.seminario.model.Movie
import ar.edu.unicen.seminario.ui.popular.MoviesUiState
import android.net.Uri

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var similarAdapter: MovieAdapter
    private lateinit var recommendedAdapter: MovieAdapter
    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        if (movieId <= 0) {
            binding.detailsErrorLayout.visibility = View.VISIBLE
            binding.detailsErrorTextView.setText(R.string.details_invalid_id)
            binding.detailsRetryButton.visibility = View.GONE
            return
        }
        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        similarAdapter = MovieAdapter { movie -> openMovieDetails(movie) }
        recommendedAdapter = MovieAdapter { movie -> openMovieDetails(movie) }
        binding.similarMoviesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.similarMoviesRecyclerView.adapter = similarAdapter
        binding.recommendedMoviesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recommendedMoviesRecyclerView.adapter = recommendedAdapter
        viewModel.isSaved.observe(this) { saved ->
            if (saved) {
                binding.watchlistButton.setText(R.string.remove_from_watchlist)
            } else {
                binding.watchlistButton.setText(R.string.add_to_watchlist)
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
        if (viewModel.uiState.value == null) {
            viewModel.loadMovieDetails(movieId)
        }

        viewModel.similarMoviesState.observe(this) { state ->
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.similarMoviesRecyclerView.visibility = View.GONE
                }
                is MoviesUiState.Success -> {
                    similarAdapter.updateMovies(state.movies)
                    binding.similarMoviesRecyclerView.visibility =
                        if (state.movies.isEmpty()) View.GONE
                            else View.VISIBLE
                }
                is MoviesUiState.Error -> {
                    binding.similarMoviesRecyclerView.visibility = View.GONE
                }
            }
        }
        viewModel.recommendedMoviesState.observe(this) { state ->
            when (state) {
                is MoviesUiState.Loading -> {
                    binding.recommendedMoviesRecyclerView.visibility = View.GONE
                }
                is MoviesUiState.Success -> {
                    recommendedAdapter.updateMovies(state.movies)

                    binding.recommendedMoviesRecyclerView.visibility =
                        if (state.movies.isEmpty()) View.GONE
                            else View.VISIBLE
                }
                is MoviesUiState.Error -> {
                    binding.recommendedMoviesRecyclerView.visibility = View.GONE
                }
            }
        }
    }
    private fun showMovieDetails(movie: MovieDetails) {
        binding.movieTitleTextView.text = movie.title
        binding.movieOverviewTextView.text =
            movie.overview?.takeIf { it.isNotBlank() }
                ?: getString(R.string.details_no_overview)
        binding.movieGenresTextView.text =
            if (movie.genres.isEmpty()) {
                getString(R.string.details_no_genres)
            } else {
                movie.genres.joinToString(", ") { it.name }
            }
        binding.movieRatingTextView.text = getString(R.string.movie_rating_value, movie.voteAverage)
        val posterUrl = movie.posterPath?.let {
            "https://image.tmdb.org/t/p/w342$it"
        }
        binding.moviePosterImageView.visibility =
            if (posterUrl == null) View.GONE else View.VISIBLE
        Glide.with(this)
            .load(posterUrl)
            .into(binding.moviePosterImageView)
    }
    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(EXTRA_MOVIE_ID, movie.id)
        startActivity(intent)
    }
}