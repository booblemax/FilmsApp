package com.example.filmsapp.ui.details

import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.BuildConfig
import com.example.filmsapp.domain.FilmsRepository
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import com.google.api.services.youtube.model.SearchListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(
    private val repository: FilmsRepository
) : BaseViewModel() {

    private val _film = MutableLiveData<Resource<FilmModel>>()
    val film get() = _film

    //todo refactor
    val requestAuthorizationPermission = MutableLiveData<UserRecoverableAuthIOException>()
    val displayGpsUnavailable = MutableLiveData<Int>()
    val searchResult = MutableLiveData<String>()

    fun loadFilm(id: String) {
        baseContext.launch {
            _film.value = Resource.LOADING()
            _film.value = repository.getFilm(id)
        }
    }

    fun requestFilmTrailer(filmName: String, credential: GoogleAccountCredential) {
        baseContext.launch {
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

            if (result.items.isEmpty()) {
                searchResult.postValue("Empty list")
            } else {
                result.items.forEach { item ->
                    searchResult.postValue(item.snippet.title)
                }
            }
        }
    }

    override fun handleException(exception: Throwable) {
        super.handleException(exception)
        when (exception) {
            is GooglePlayServicesAvailabilityIOException -> displayGpsUnavailable.value = exception.connectionStatusCode
            is UserRecoverableAuthIOException -> requestAuthorizationPermission.value = exception
            else -> showSnackbar.value = "Error occur: ${exception.message}"
        }
    }
}
