package com.example.filmsapp.details

import com.example.domain.models.FilmModel
import com.example.domain.models.YoutubeFilmModel
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.mvi.IState

data class DetailsState(
    val loading: Boolean = true,
    val filmModel: FilmModel? = null,
    val youtubeModel: YoutubeFilmModel? = null,
    val isFavorite: Boolean = false,
    val errorMessage: String? = null,
    val errorRes: Int? = null,
    val uiEvent: Event<DetailsUiEvent>? = null
) : IState

sealed class DetailsUiEvent {
    data class OpenPlayer(val videoId: String) : DetailsUiEvent()
    object Back : DetailsUiEvent()
}
