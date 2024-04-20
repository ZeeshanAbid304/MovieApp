package com.example.moviedb.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviedb.ApiService
import com.example.moviedb.Room.MovieDao
import com.example.moviedb.Room.MovieEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(private val movieDao: MovieDao) : ViewModel() {

    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>> = _movies

    private val apiService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private var isFirstLoad = true

    fun fetchMoviesIfNeeded() {
        viewModelScope.launch {
            try {
                val apiData = apiService.getMovies()
                val movies = apiData.results.map { movie ->
                    MovieEntity(
                        id = movie.id,
                        title = movie.title,
                        releaseDate = movie.releaseDate,
                        posterPath = movie.posterPath
                    )
                }
                // Log the number of movies being inserted
                Log.d(TAG, "Number of movies fetched: ${movies.size}")
                movieDao.insertAll(movies)
                _movies.value = movies
                if (isFirstLoad) {
                    Log.d(TAG, "Movies fetched and saved successfully!")
                    showToast("Movies fetched and saved successfully!")
                    isFirstLoad = false
                }
            } catch (e: Exception) {
                Log.d(TAG, "Failed to fetch movies")
                e.printStackTrace()
                showToast("Failed to fetch movies: ${e.message}")
            }
        }
    }

    fun loadMoviesFromDatabase() {
        viewModelScope.launch {
            try {
                val moviesLiveData = movieDao.getAll()
                val movies = moviesLiveData.value ?: emptyList()
                _movies.value = movies
                if (isFirstLoad && movies.isNotEmpty()) {
                    showToast("Movies loaded from the database.")
                    isFirstLoad = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Failed to load movies from database: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        // Implement this method according to your UI framework
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}