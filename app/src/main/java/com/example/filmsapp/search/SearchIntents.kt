package com.example.filmsapp.search

import com.example.filmsapp.base.mvi.BaseIntent
import com.example.filmsapp.common.FilmDetailsDto

sealed class SearchIntents : BaseIntent() {

    data class LoadQuery(val query: String) : SearchIntents()
    data class LoadNextPage(val query: String) : SearchIntents()
    data class OpenFilm(val filmDetailsDto: FilmDetailsDto) : SearchIntents()
    object OnBack : SearchIntents()
}
