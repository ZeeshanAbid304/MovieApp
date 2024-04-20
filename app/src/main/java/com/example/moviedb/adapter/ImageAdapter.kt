package com.example.moviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R

class ImageAdapter(private val filePaths: List<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val filePath = filePaths[position]

        // Load image using Glide or any other image loading library
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500$filePath")
            .placeholder(R.drawable.loading)
            .error(R.drawable.loading)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return filePaths.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}