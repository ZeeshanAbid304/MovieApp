package com.example.moviedb.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailViewModel : ViewModel() {
    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private val _movieDetails = MutableLiveData<Movie>()
    val movieDetails: LiveData<Movie> = _movieDetails

    private val _trailerUrl = MutableLiveData<String?>()
    val trailerUrl: LiveData<String?> = _trailerUrl

    private var savedTrailerUrl: String? = null

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetails = apiService.getMovieDetails(movieId)
                _movieDetails.value = movieDetails
            } catch (e: Exception) {
                // Handle error
                Log.e("DetailViewModel", "Error fetching movie details", e)
                _movieDetails.value = null
            }
        }
    }

    fun fetchMovieTrailer(movieId: Int) {
        viewModelScope.launch {
            try {
                val trailersResponse = apiService.getTrendingMovieTrailers(movieId)
                val trailerKey = trailersResponse.results?.firstOrNull()?.key
                if (trailerKey != null) {
                    val trailerUrl = "https://www.youtube.com/watch?v=$trailerKey"
                    _trailerUrl.value = trailerUrl
                    savedTrailerUrl = trailerUrl
                } else {
                    _trailerUrl.value = null // No trailers found
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("DetailViewModel", "Error fetching movie trailer", e)
                _trailerUrl.value = null
            }
        }
    }

    fun getSavedTrailerUrl(): String? {
        return savedTrailerUrl
    }
}