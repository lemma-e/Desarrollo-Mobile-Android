package ar.edu.unicen.seminario.ui.popular

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ItemPagingFooterBinding
import retrofit2.HttpException
import java.io.IOException

class MovieLoadStateAdapter(
    private val onRetry: () -> Unit ): LoadStateAdapter<MovieLoadStateAdapter.MovieLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): MovieLoadStateViewHolder {
        val binding = ItemPagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return MovieLoadStateViewHolder(binding, onRetry)
    }

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
    class MovieLoadStateViewHolder(
        private val binding: ItemPagingFooterBinding,
        private val onRetry: () -> Unit ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.footerRetryButton.setOnClickListener {
                onRetry()
            }
        }

        fun bind(loadState: LoadState) {
            binding.footerProgressBar.visibility =
                if (loadState is LoadState.Loading)
                    View.VISIBLE
                else View.GONE

            val error = loadState as? LoadState.Error
            binding.footerErrorTextView.visibility =
                if (error != null)
                    View.VISIBLE
                else View.GONE

            binding.footerRetryButton.visibility =
                if (error != null)
                    View.VISIBLE
                else View.GONE

            if (error != null) {
                val errorMessage = when (error.error) {
                    is IOException -> R.string.error_connection
                    is HttpException -> R.string.error_server
                    else -> R.string.movies_load_error
                }
                binding.footerErrorTextView.setText(errorMessage)
            }
        }
    }
}