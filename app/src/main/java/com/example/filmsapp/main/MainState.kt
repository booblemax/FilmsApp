package com.example.filmsapp.main

import com.example.domain.models.FilmModel
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.common.FilmDetailsDto

data class MainState(
    val loading: Boolean = true,
    val firstLoading: Boolean = true,
    val films: List<FilmModel> = listOf(),
    val isEmptyList: Boolean = films.isEmpty(),
    val isFavoriteList: Boolean = false,
    val errorString: String? = null,
    val errorMessage: Int? = null,
    val uiEvent: Event<MainUiEvent>? = null
) : IState {

    override fun toString(): String {
        return "MainState(" +
            "loading=$loading," +
            " firstLoading=$firstLoading," +
            " films=${films::class.java}," +
            " isEmptyList=$isEmptyList," +
            " isFavoriteList=$isFavoriteList," +
            " errorString=$errorString," +
            " errorMessage=$errorMessage," +
            " uiEvent=$uiEvent)"
    }
}

sealed class MainUiEvent {
    data class OpenFilm(val filmDetailsDto: FilmDetailsDto) : MainUiEvent()
    object Back : MainUiEvent()
}
