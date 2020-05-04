package com.example.filmsapp.ui.base.models

import com.example.filmsapp.data.remote.response.Genre

data class FilmModel(
    val id: Int = -1,
    val title: String = "",
    val poster: String? = "",
    val backdropPath: String? = null,
    val overview: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = -1,
    val genres: List<Genre>? = null
)