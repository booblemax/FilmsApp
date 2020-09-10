package com.example.filmsapp.search

import com.example.domain.models.FilmModel
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.common.FilmDetailsDto

data class SearchState(
    val loading: Boolean = true,
    val films: List<FilmModel> = listOf(),
    val isEmptyList: Boolean = films.isEmpty(),
    val isEmptyQuery: Boolean = true,
    val errorString: String? = null,
    val errorMessage: Int? = null,
    val uiEvent: Event<SearchUiEvent>? = null
) : IState {

    override fun toString(): String {
        return "SearchState(loading=$loading," +
            " films=${films::class.java.simpleName}," +
            " isEmptyList=$isEmptyList," +
            " errorString=$errorString," +
            " errorMessage=$errorMessage," +
            " uiEvent=$uiEvent)"
    }
}

sealed class SearchUiEvent {
    data class OpenFilm(val filmDetailsDto: FilmDetailsDto) : SearchUiEvent()
    object Back : SearchUiEvent()
}
