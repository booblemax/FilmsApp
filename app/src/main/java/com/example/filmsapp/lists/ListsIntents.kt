package com.example.filmsapp.lists

import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.mvi.BaseIntent
import com.example.filmsapp.common.FilmDetailsDto

sealed class ListsIntents : BaseIntent() {

    object InitialIntent : ListsIntents()
    data class OpenLists(val type: ListType) : ListsIntents()
    data class OpenFilm(val filmDetailsDto: FilmDetailsDto) : ListsIntents()
    object OpenSettings : ListsIntents()
    object OpenSearch : ListsIntents()

    data class Exception(val exception: Throwable) : ListsIntents()
}
