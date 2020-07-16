package com.example.filmsapp.domain.repos

import com.example.filmsapp.ui.base.models.YoutubeFilmModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

interface YoutubeRepository {

    suspend fun getTrailerForFilm(filmName: String, credential: GoogleAccountCredential): YoutubeFilmModel?

}