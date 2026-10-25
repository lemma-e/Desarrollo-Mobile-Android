package ar.edu.unicen.seminario.ui.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.databinding.ItemMovieBinding
import ar.edu.unicen.seminario.model.Movie
import com.bumptech.glide.Glide

class MoviePagingAdapter(
    private val onMovieClick: (Movie) -> Unit) : PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.binding.movieTitleTextView.text = movie.title
        val posterUrl = movie.posterPath?.let {
            "https://image.tmdb.org/t/p/w185$it"
        }
        Glide.with(holder.itemView).load(posterUrl).into(holder.binding.moviePosterImageView)
        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }
    }

    class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem.id == newItem.id
                }
                override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem == newItem
                }
            }
    }
}