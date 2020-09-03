package com.example.filmsapp.lists

import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.mvi.BaseIntent

sealed class ListsIntents : BaseIntent() {

    object InitialIntent : ListsIntents()
    data class OpenLists(val type: ListType) : ListsIntents()
    data class OpenFilm(
        val id: String,
        val posterUrl: String?,
        val backdropUrl: String?
    ) : ListsIntents()
    object OpenSettings : ListsIntents()
    object OpenSearch : ListsIntents()

    data class Exception(val exception: Throwable) : ListsIntents()
}
