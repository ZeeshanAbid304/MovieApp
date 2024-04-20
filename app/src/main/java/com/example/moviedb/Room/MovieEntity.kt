package com.example.moviedb.Room
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "upcoming_movies")
data class MovieEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String?, // Ensure the format of this date matches the format returned by the server
    @SerializedName("poster_path")
    val posterPath: String?
)