package com.example.filmsapp.lists

import com.example.domain.models.FilmModel
import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.common.FilmDetailsDto

data class ListsState(
    val popularLoading: Boolean = false,
    val topratedLoading: Boolean = false,
    val upcomingLoading: Boolean = false,
    val latestFilm: FilmModel? = null,
    val popularFilms: List<FilmModel> = listOf(),
    val topRatedFilms: List<FilmModel> = listOf(),
    val upcomingFilms: List<FilmModel> = listOf(),
    val error: Exception? = null,
    val errorMessage: Int? = null,
    val uiEvent: ListsUiEvent? = null
) : IState {

    override fun toString(): String {
        return "ListsState(popularLoading=$popularLoading," +
            " topratedLoading=$topratedLoading, " +
            "upcomingLoading=$upcomingLoading, " +
            "latestFilm=$latestFilm," +
            " popularFilms=${popularFilms::class.java}, " +
            "topRatedFilms=${topRatedFilms::class.java}," +
            " upcomingFilms=${upcomingFilms::class.java}," +
            " error=$error, " +
            "errorMessage=$errorMessage," +
            " uiEvent=$uiEvent)"
    }
}

sealed class ListsUiEvent {

    data class OpenList(val type: ListType) : ListsUiEvent()
    data class OpenFilm(val filmDetailsDto: FilmDetailsDto) : ListsUiEvent()

    object OpenSettings : ListsUiEvent()
    object OpenSearch : ListsUiEvent()
}
