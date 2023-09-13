package com.example.movieassignment.data.models

data class Page(
    val title: String,
    val `page-num`: Int,
    val `total-conent-items`: Int,
    val `page-size`: Int,
    val `content-items`: ContentItem
)

data class ContentItem(
    val content: List<Movie>
)

data class Movie(
    val name: String,
    val `poster-image`: String
)