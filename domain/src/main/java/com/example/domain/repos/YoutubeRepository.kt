package com.example.domain.repos

import com.example.domain.models.YoutubeFilmModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential

interface YoutubeRepository {

    suspend fun getTrailerForFilm(filmName: String, creds: GoogleAccountCredential): YoutubeFilmModel?
}
