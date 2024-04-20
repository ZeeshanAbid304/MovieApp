package com.example.moviedb.Room
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.model.MainViewModel

class ViewModelFactory(private val application: Application, private val movieDao: MovieDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(movieDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
