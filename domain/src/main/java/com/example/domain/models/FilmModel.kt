package com.example.domain.models

data class FilmModel(
    val id: String = "",
    val title: String = "",
    val poster: String? = "",
    val runtime: Int = 0,
    val releaseDate: String? = "",
    val backdropPath: String? = null,
    val overview: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = -1,
    val genres: List<GenreModel>? = null,
    val backdrops: BackdropsModel? = null,
    val video: Boolean = false,
    val hasBookmark: Boolean = false
)
