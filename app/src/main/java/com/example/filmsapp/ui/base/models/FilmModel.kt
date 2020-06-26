package com.example.filmsapp.ui.base.models

import com.example.filmsapp.data.remote.response.BackdropsDto
import com.example.filmsapp.data.remote.response.Genre

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
    val genres: List<Genre>? = null,
    val backdrops: BackdropsDto? = null,
    val video: Boolean = false
)
