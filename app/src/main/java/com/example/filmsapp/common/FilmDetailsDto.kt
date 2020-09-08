package com.example.filmsapp.common

data class FilmDetailsDto(
    val id: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val isFavorite: Boolean = false
)
