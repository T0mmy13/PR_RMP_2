package adapters

import Models.Movie
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.bignerdranch.android.praktikabotomnavigation.databinding.MovieItemBinding
import com.bignerdranch.android.praktikabotomnavigation.ui.Movie.NotificationsViewModel


class MovieAdapter(private val viewModel: NotificationsViewModel) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private var items: List<Movie> = emptyList()
    private var onClickListener: OnClickListener? = null
    var movieList: List<Movie> = listOf()
    var filteredMovieList: List<Movie> = listOf()
    companion object Factory {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.sortedByDescending { it.rating }[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    fun refreshMovies(newItems: List<Movie>) {
        this.items = newItems
        movieList = newItems
        filteredMovieList = newItems
        notifyDataSetChanged()
    }
    fun refreshMoviess(newItems: List<Movie>) {
        movieList = newItems
        filter(viewModel.lastSearchQuery ?: "")
    }
    interface OnClickListener {
        fun onClick(position: Int, model: Movie)
    }
    class ViewHolder(private val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.MovieName.text = movie.movie
            binding.ratingMovie.text = movie.rating.toString()
        }
    }
    fun filter(query: String) {
        filteredMovieList = if (query.isEmpty()) {
            movieList
        } else {
            movieList.filter { it.movie.contains(query, ignoreCase = true) }
        }
        items = filteredMovieList
        notifyDataSetChanged()
    }
}