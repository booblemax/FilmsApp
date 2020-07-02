package com.example.filmsapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.domain.repos.YoutubeRepository
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import com.example.filmsapp.ui.base.models.YoutubeFilmModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val filmsRepository: FilmsRepository,
    private val youtubeRepository: YoutubeRepository
) : BaseViewModel() {

    private val _film = MutableLiveData<Resource<FilmModel>>()
    val film get() = _film

    private val _requestAuthorizationPermission = MutableLiveData<UserRecoverableAuthIOException>()
    val requestAuthorizationPermission: LiveData<UserRecoverableAuthIOException> get() =
        MutableLiveData<UserRecoverableAuthIOException>()

    private val _displayGpsUnavailable = MutableLiveData<Int>()
    val displayGpsUnavailable: LiveData<Int> get() = _displayGpsUnavailable

    private val _youtubeMovieSearchResult = MutableLiveData<YoutubeFilmModel>()
    val youtubeMovieSearchResult: LiveData<YoutubeFilmModel> get() = _youtubeMovieSearchResult

    fun loadFilm(id: String) {
        baseContext.launch {
            _film.value = Resource.LOADING()
            _film.value = filmsRepository.getFilm(id)
        }
    }

    fun requestFilmTrailer(filmName: String, credential: GoogleAccountCredential) {
        baseContext.launch {
            val model = youtubeRepository.getTrailerForFilm(filmName, credential)
            _youtubeMovieSearchResult.value = model
        }
    }

    override fun handleException(exception: Throwable) {
        super.handleException(exception)
        when (exception) {
            is GooglePlayServicesAvailabilityIOException -> _displayGpsUnavailable.value =
                exception.connectionStatusCode
            is UserRecoverableAuthIOException -> _requestAuthorizationPermission.value = exception
            else -> _showSnackbar.value = exception.message
        }
    }
}
