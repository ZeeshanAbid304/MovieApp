package com.example.moviedb.model

data class TrailersResponse(
    val results: List<Trailer>
)

data class Trailer(
    val id: String,
    val key: String,
    val name: String,
    val site: String
    // Other properties as needed
)
