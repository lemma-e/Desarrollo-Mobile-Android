package ar.edu.unicen.seminario.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ActivityMovieDetailsBinding
import ar.edu.unicen.seminario.model.MovieDetails
import com.bumptech.glide.Glide

class MovieDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var viewModel: MovieDetailsViewModel
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
}