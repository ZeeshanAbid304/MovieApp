package com.example.moviedb.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ImagesViewModel : ViewModel() {
    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private val _images = MutableLiveData<Movie>()
    val imagesList: LiveData<Movie> = _images

    fun fetchMovieImages(movieId: Int) {
        viewModelScope.launch {
            try {
                val imagesListDetail = apiService.getImagesDetails(movieId)
                _images.value = imagesListDetail
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }
}