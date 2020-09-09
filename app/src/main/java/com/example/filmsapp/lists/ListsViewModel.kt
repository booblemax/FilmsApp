package com.example.filmsapp.lists

import com.example.domain.Resource
import com.example.domain.dispatchers.DispatcherProvider
import com.example.domain.models.FilmModel
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class ListsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val repository: FilmsRepository
) : BaseViewModel<ListsState, ListsIntents>(dispatcherProvider, ListsState()) {

    init {
        pushIntent(ListsIntents.InitialIntent)
    }

    override suspend fun processIntention(intent: ListsIntents) {
        when (intent) {
            is ListsIntents.InitialIntent -> loadFilms()
            is ListsIntents.OpenLists -> reduce { it.copy(uiEvent = Event(ListsUiEvent.OpenList(intent.type))) }
            is ListsIntents.OpenFilm -> reduce {
                val isFavorite = repository.isFilmStoredInDb(intent.filmDetailsDto.id)
                val corrFilmDetails = intent.filmDetailsDto.copy(isFavorite = isFavorite)
                it.copy(uiEvent = Event(ListsUiEvent.OpenFilm(corrFilmDetails)))
            }
            is ListsIntents.OpenSearch -> reduce { it.copy(uiEvent = Event(ListsUiEvent.OpenSearch)) }
            is ListsIntents.OpenSettings -> reduce { it.copy(uiEvent = Event(ListsUiEvent.OpenSettings)) }
        }
    }

    private fun loadFilms() {
        baseScope.launch {
            val latestDeferred = async { loadLatest() }
            val popularDeferred = async { loadPopular() }
            val topRatedDeferred = async { loadTopRated() }
            val upcomingDeferred = async { loadUpcoming() }
            awaitAll(latestDeferred, popularDeferred, topRatedDeferred, upcomingDeferred)
        }
    }

    private suspend fun loadLatest() {
        val latestFilmResource = repository.getLatestFilm()
        reduce { oldState ->
            when (latestFilmResource) {
                is Resource.SUCCESS<FilmModel> -> oldState.copy(
                    latestFilm = latestFilmResource.data
                )
                is Resource.ERROR -> oldState.copy(latestFilm = null)
                else -> {
                    Timber.e("Wrong result latest")
                    oldState.copy(errorMessage = R.string.error)
                }
            }
        }
    }

    private suspend fun loadPopular() {
        reduce { it.copy(popularLoading = true) }
        val popularFilmResource = repository.getPopularFilms()
        reduce { oldState ->
            when (popularFilmResource) {
                is Resource.SUCCESS<List<FilmModel>> -> oldState.copy(
                    popularLoading = false,
                    popularFilms = popularFilmResource.data ?: listOf()
                )
                is Resource.ERROR -> oldState.copy(
                    popularLoading = false,
                    popularFilms = listOf()
                )
                else -> {
                    Timber.e("Wrong result popular")
                    oldState.copy(errorMessage = R.string.error)
                }
            }
        }
    }

    private suspend fun loadTopRated() {
        reduce { it.copy(topratedLoading = true) }
        val topratedFilmResource = repository.getTopRatedFilms()
        reduce { oldState ->
            when (topratedFilmResource) {
                is Resource.SUCCESS<List<FilmModel>> -> oldState.copy(
                    topratedLoading = false,
                    topRatedFilms = topratedFilmResource.data ?: listOf()
                )
                is Resource.ERROR -> oldState.copy(
                    topratedLoading = false,
                    topRatedFilms = listOf()
                )
                else -> {
                    Timber.e("Wrong result toprated")
                    oldState.copy(errorMessage = R.string.error)
                }
            }
        }
    }

    private suspend fun loadUpcoming() {
        reduce { it.copy(upcomingLoading = true) }
        val upcomingFilmResource = repository.getUpcomingFilms()
        reduce { oldState ->
            when (upcomingFilmResource) {
                is Resource.SUCCESS<List<FilmModel>> -> oldState.copy(
                    upcomingLoading = false,
                    upcomingFilms = upcomingFilmResource.data ?: listOf()
                )
                is Resource.ERROR -> oldState.copy(
                    upcomingLoading = false,
                    upcomingFilms = listOf()
                )
                else -> {
                    Timber.e("Wrong result upcoming")
                    oldState.copy(errorMessage = R.string.error)
                }
            }
        }
    }

    override fun handleException(exception: Throwable) {
        Timber.e(exception)
        pushIntent(ListsIntents.Exception(exception))
    }
}
