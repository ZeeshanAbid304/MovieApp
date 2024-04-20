package com.example.moviedb.Room

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private const val TAG = "MovieDatabase"

        // Override the onOpen method to log database creation and check if it's empty
        val callback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                Log.d(TAG, "Database opened successfully.")
                CoroutineScope(Dispatchers.IO).launch {
                    val count = db.query("SELECT COUNT(*) FROM upcoming_movies").use {
                        if (it.moveToFirst()) {
                            it.getInt(0)
                        } else {
                            0
                        }
                    }
                    Log.d(TAG, "Number of rows in the database: $count")
                }
            }
        }
    }
}