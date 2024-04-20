package com.example.moviedb.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.moviedb.R
import com.example.moviedb.Room.MovieEntity

class MovieAdapter(private val listener: MovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: List<MovieEntity> = listOf()

    fun submitList(movies: List<MovieEntity>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val movieTitleTextView: TextView = itemView.findViewById(R.id.movieTitle)
        private val releaseDateTextView: TextView = itemView.findViewById(R.id.releaseDate)
        private val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
//        private val loadingProgressBar: ProgressBar = itemView.findViewById(R.id.loadingProgress)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: MovieEntity) {
            movieTitleTextView.text = movie.title
            releaseDateTextView.text = movie.releaseDate
//            loadingProgressBar.visibility = View.VISIBLE

            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .centerInside()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
//                        loadingProgressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
//                        loadingProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(posterImageView)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val movie = movies[position]
                listener.onMovieClick(movie)
            }
        }
    }

    interface MovieClickListener {
        fun onMovieClick(movie: MovieEntity)
    }
}