package ar.edu.unicen.seminario.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.databinding.ItemMovieCarouselBinding
import ar.edu.unicen.seminario.model.Movie
import com.bumptech.glide.Glide

class MovieCarouselAdapter(private val onMovieClick: (Movie) -> Unit) : RecyclerView.Adapter<MovieCarouselAdapter.MovieViewHolder>() {
    private var movies: List<Movie> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.movieTitleTextView.text = movie.title
        val posterUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w185$it" }
        Glide.with(holder.itemView).load(posterUrl).into(holder.binding.moviePosterImageView)
        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(val binding: ItemMovieCarouselBinding) : RecyclerView.ViewHolder(binding.root)
}
