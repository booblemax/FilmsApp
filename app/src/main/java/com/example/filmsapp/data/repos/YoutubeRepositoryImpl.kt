package com.example.filmsapp.data.repos

import com.example.filmsapp.BuildConfig
import com.example.filmsapp.domain.repos.YoutubeRepository
import com.example.filmsapp.ui.base.models.YoutubeFilmModel
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import com.google.api.services.youtube.model.SearchListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YoutubeRepositoryImpl : YoutubeRepository {

    override suspend fun getTrailerForFilm(filmName: String, credential: GoogleAccountCredential): YoutubeFilmModel? {
        val transport = AndroidHttp.newCompatibleTransport()
        val factory = JacksonFactory.getDefaultInstance()
        val service = YouTube.Builder(transport, factory, credential)
            .setApplicationName("FilmsApp")
            .setYouTubeRequestInitializer(YouTubeRequestInitializer(BuildConfig.GOOGLE_API_KEY))
            .build()

        val query = "$filmName trailer"
        val result: SearchListResponse = withContext(Dispatchers.IO) {
            service.search().list("snippet").apply {
                maxResults = 1
                q = query
            }.execute()
        }

        return result.items.firstOrNull()?.let { item ->
            val snippet = item.snippet
            YoutubeFilmModel(snippet.title, snippet.channelId, item.id.videoId)
        }
    }
}
