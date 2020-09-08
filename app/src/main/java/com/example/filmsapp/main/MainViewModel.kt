package com.example.filmsapp.main

import com.example.domain.Resource
import com.example.domain.dispatchers.DispatcherProvider
import com.example.domain.models.FilmModel
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.R
import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.PagedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainViewModel(
    dispatcherProvider: DispatcherProvider,
    private val repository: FilmsRepository
) : PagedViewModel<MainState, MainIntents>(dispatcherProvider, MainState()) {

    lateinit var listType: ListType
    var lastKnownPosition = -1

    override suspend fun processIntention(intent: MainIntents) {
        when (intent) {
            is MainIntents.InitialEvent -> loadFilms()
            is MainIntents.ReloadList -> {
                resetPageNumber()
                loadFilms(true)
            }
            is MainIntents.LoadNextPage -> loadFilms()
            is MainIntents.OpenFilm -> reduce {
                it.copy(uiEvent = MainUiEvent.OpenFilm(intent.filmDetailsDto))
            }
        }
    }

    private fun loadFilms(forceUpdate: Boolean = false) {
        baseScope.launch {
            incPageNumber()
            reduce {
                it.copy(loading = true, firstLoading = isFirstPageLoading())
            }
            val listResource = loadByListType(forceUpdate)
            reduce {
                when (listResource) {
                    is Resource.SUCCESS<List<FilmModel>> -> {
                        val films = listResource.data ?: listOf()
                        it.copy(
                            loading = false,
                            firstLoading = false,
                            films = films,
                            isEmptyList = films.isEmpty()
                        )
                    }
                    is Resource.ERROR -> {
                        decPageNumber()
                        it.copy(
                            loading = false,
                            firstLoading = false,
                            errorString = listResource.error?.message,
                            isEmptyList = true
                        )
                    }
                    else -> it.copy(
                        loading = false,
                        firstLoading = false,
                        errorMessage = R.string.error,
                        isEmptyList = true
                    )
                }
            }
        }
    }

    private suspend fun loadByListType(forceUpdate: Boolean = false) =
        when (listType) {
            ListType.POPULAR -> repository.getPopularFilms(pageNumber, forceUpdate)
            ListType.TOP_RATED -> repository.getTopRatedFilms(pageNumber, forceUpdate)
            ListType.UPCOMING -> repository.getUpcomingFilms(pageNumber, forceUpdate)
            ListType.FAVOURITES -> repository.getFavouritesFilms(pageNumber)
        }
}
