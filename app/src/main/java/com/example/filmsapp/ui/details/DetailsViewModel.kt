package com.example.filmsapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.R
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.domain.repos.YoutubeRepository
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.ui.base.models.FilmModel
import com.example.filmsapp.ui.base.models.YoutubeFilmModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val filmsRepository: FilmsRepository,
    private val youtubeRepository: YoutubeRepository
) : BaseViewModel(dispatcherProvider) {

    private val _film = MutableLiveData<Resource<FilmModel>>()
    val film get() = _film

    private val _isFavorites = MutableLiveData<Event<Boolean>>()
    val isFavorites: LiveData<Event<Boolean>> get() = _isFavorites

    private val _requestAuthorizationPermission = MutableLiveData<UserRecoverableAuthIOException>()
    val requestAuthorizationPermission: LiveData<UserRecoverableAuthIOException> get() = _requestAuthorizationPermission

    private val _displayGpsUnavailable = MutableLiveData<Int>()
    val displayGpsUnavailable: LiveData<Int> get() = _displayGpsUnavailable

    private val _youtubeMovieSearchResult = MutableLiveData<YoutubeFilmModel>()
    val youtubeMovieSearchResult: LiveData<YoutubeFilmModel> get() = _youtubeMovieSearchResult

    fun loadFilm(id: String, isFavorites: Boolean) {
        baseContext.launch {
            _film.value = Resource.LOADING()
            checkFilmStoredInDb(isFavorites, id)
            _film.value = filmsRepository.getFilm(id, !isFavorites)
        }
    }

    private suspend fun checkFilmStoredInDb(isFavorites: Boolean, id: String) {
        if (isFavorites) {
            _isFavorites.value = Event(true)
        } else {
            _isFavorites.value = Event(filmsRepository.isFilmStoredInDb(id))
        }
    }

    fun requestFilmTrailer(filmName: String, credential: GoogleAccountCredential) {
        baseContext.launch {
            val model = youtubeRepository.getTrailerForFilm(filmName, credential)
            _youtubeMovieSearchResult.value = model
        }
    }

    fun favoriteClicked() {
        _isFavorites.value?.let {
            if (it.peekContent()) {
                removeFilmFromStore()
            } else {
                storeFilm()
            }
            _isFavorites.value = Event(!it.peekContent())
        }
    }

    private fun storeFilm() {
        (film.value as? Resource.SUCCESS)?.data?.let {
            baseContext.launch {
                filmsRepository.saveFilm(it)
                postMessage(R.string.film_saved)
            }
        } ?: postMessage(R.string.error)
    }

    private fun removeFilmFromStore() {
        (film.value as? Resource.SUCCESS)?.data?.let {
            baseContext.launch {
                filmsRepository.deleteFilm(it)
                postMessage(R.string.film_removed)
            }
        } ?: postMessage(R.string.error)
    }

    override fun handleException(exception: Throwable) {
        super.handleException(exception)
        when (exception) {
            is GooglePlayServicesAvailabilityIOException ->
                _displayGpsUnavailable.value =
                    exception.connectionStatusCode
            is UserRecoverableAuthIOException -> _requestAuthorizationPermission.value = exception
            else -> {
                Timber.e(exception)
                postMessage(R.string.error)
            }
        }
    }
}
