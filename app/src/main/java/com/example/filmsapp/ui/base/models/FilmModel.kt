package com.example.filmsapp.ui.base.models

import com.example.filmsapp.data.remote.response.Genre

data class FilmModel(
    val id: Int,
    val title: String,
    val poster: String,
    val backdropPath: String?,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val genres: List<Genre>?
)