package com.example.filmsapp.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.Resource
import com.example.domain.dispatchers.DispatcherProvider
import com.example.domain.models.FilmModel
import com.example.domain.models.YoutubeFilmModel
import com.example.domain.repos.FilmsRepository
import com.example.domain.repos.YoutubeRepository
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.mvi.EmptyState
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DetailsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val filmsRepository: FilmsRepository,
    private val youtubeRepository: YoutubeRepository
) : BaseViewModel<IState, Intention>(dispatcherProvider, EmptyState()) {

    private val _film = MutableLiveData<Resource<FilmModel>>()
    val film get() = _film

    private val _isFavorites = MutableLiveData<Event<Boolean>>()
    val isFavorites: LiveData<Event<Boolean>> get() = _isFavorites

    private val _youtubeMovieSearchResult = MutableLiveData<YoutubeFilmModel>()
    val youtubeMovieSearchResult: LiveData<YoutubeFilmModel> get() = _youtubeMovieSearchResult

    fun loadFilm(id: String, isFavorites: Boolean) {
        baseScope.launch {
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
        baseScope.launch {
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
            baseScope.launch {
                filmsRepository.saveFilm(it)
                postMessage(R.string.film_saved)
            }
        } ?: postMessage(R.string.error)
    }

    private fun removeFilmFromStore() {
        (film.value as? Resource.SUCCESS)?.data?.let {
            baseScope.launch {
                filmsRepository.deleteFilm(it)
                postMessage(R.string.film_removed)
            }
        } ?: postMessage(R.string.error)
    }
}
