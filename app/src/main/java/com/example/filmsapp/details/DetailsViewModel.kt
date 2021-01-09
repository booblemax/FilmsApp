package com.example.filmsapp.details

import android.view.View
import com.example.domain.Resource
import com.example.domain.dispatchers.DispatcherProvider
import com.example.domain.repos.FilmsRepository
import com.example.domain.repos.YoutubeRepository
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.Event
import com.example.filmsapp.navigation.FilmScreen.ImagesCarouselScreen
import com.example.filmsapp.navigation.FilmScreen.PlayerScreen
import com.github.terrakok.cicerone.Router
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import kotlinx.android.synthetic.main.item_backdrop.view.image_backdrop
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DetailsViewModel(
    router: Router,
    dispatcherProvider: DispatcherProvider,
    private val filmsRepository: FilmsRepository,
    private val youtubeRepository: YoutubeRepository
) : BaseViewModel<DetailsState, DetailsIntents>(router, dispatcherProvider, DetailsState()) {

    override suspend fun processIntention(intent: DetailsIntents) {
        super.processIntention(intent)
        when (intent) {
            is DetailsIntents.Initial -> loadFilm(intent.id, intent.isfavorite)
            is DetailsIntents.LoadTrailer -> requestFilmTrailer(intent.title, intent.creds)
            is DetailsIntents.ChangeFavoriteState -> favoriteClicked()
            is DetailsIntents.OpenPlayer ->
                reduce { it.copy(uiEvent = Event(DetailsUiEvent.OpenPlayer(intent.videoId))) }
            is DetailsIntents.Back -> reduce { it.copy(uiEvent = Event(DetailsUiEvent.Back)) }
        }
    }

    private fun loadFilm(id: String, isFavorites: Boolean) {
        baseScope.launch {
            reduce { it.copy(loading = true) }
            val isFilmStored = checkFilmStoredInDb(isFavorites, id)
            val filmRes = filmsRepository.getFilm(id, !isFilmStored)
            reduce {
                when (filmRes) {
                    is Resource.SUCCESS -> it.copy(
                        loading = false,
                        filmModel = filmRes.data,
                        isFavorite = isFilmStored
                    )
                    is Resource.ERROR -> it.copy(
                        loading = false,
                        errorMessage = filmRes.error?.message
                    )
                    else -> it.copy(
                        loading = false,
                        errorRes = R.string.error
                    )
                }
            }
        }
    }

    private suspend fun checkFilmStoredInDb(isFavorites: Boolean, id: String): Boolean {
        return if (isFavorites) {
            true
        } else {
            filmsRepository.isFilmStoredInDb(id)
        }
    }

    private fun requestFilmTrailer(filmName: String, credential: GoogleAccountCredential) {
        baseScope.launch {
            val model = youtubeRepository.getTrailerForFilm(filmName, credential)
            reduce { it.copy(youtubeModel = model) }
        }
    }

    private fun favoriteClicked() {
        baseScope.launch {
            val isFilmStored = state.value.isFavorite
            if (isFilmStored) {
                removeFilmFromStore()
            } else {
                storeFilm()
            }
            reduce { it.copy(isFavorite = !isFilmStored) }
        }
    }

    private suspend fun storeFilm() {
        state.value.filmModel?.let {
                filmsRepository.saveFilm(it)
                postMessage(R.string.film_saved)
        } ?: postMessage(R.string.error)
    }

    private suspend fun removeFilmFromStore() {
        state.value.filmModel?.let {
                filmsRepository.deleteFilm(it)
                postMessage(R.string.film_removed)
        } ?: postMessage(R.string.error)
    }

    fun openBackdrop(urls: List<String>, position: Int) {
        router.navigateTo(ImagesCarouselScreen(urls, position))
    }

    fun openPlayer(videoId: String) {
        router.navigateTo(PlayerScreen(videoId))
    }
}
