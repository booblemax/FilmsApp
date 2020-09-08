package com.example.filmsapp.main

import com.example.filmsapp.base.mvi.BaseIntent
import com.example.filmsapp.common.FilmDetailsDto

sealed class MainIntents : BaseIntent() {

    object InitialEvent : MainIntents()
    object ReloadList : MainIntents()
    object LoadNextPage : MainIntents()
    data class OpenFilm(val filmDetailsDto: FilmDetailsDto) : MainIntents()
    object OnBack : MainIntents()
}
