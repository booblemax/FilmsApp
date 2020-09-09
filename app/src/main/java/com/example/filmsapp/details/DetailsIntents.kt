package com.example.filmsapp.details

import com.example.filmsapp.base.mvi.BaseIntent
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

sealed class DetailsIntents : BaseIntent() {

    data class Initial(val id: String, val isfavorite: Boolean) : DetailsIntents()
    object ChangeFavoriteState : DetailsIntents()
    data class OpenPlayer(val videoId: String) : DetailsIntents()
    data class LoadTrailer(val title: String, val creds: GoogleAccountCredential): DetailsIntents()
    object Back : DetailsIntents()
}
