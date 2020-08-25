package com.example.filmsapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.domain.Resource
import com.example.domain.dispatchers.DispatcherProvider
import com.example.domain.models.FilmModel
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.PagedViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    dispatcherProvider: DispatcherProvider,
    private val repository: FilmsRepository
) : PagedViewModel(dispatcherProvider) {

    private val _films = MutableLiveData<Resource<List<FilmModel>>>()
    val films: LiveData<Resource<List<FilmModel>>> get() = _films

    private val _emptyData = MutableLiveData<Event<Boolean>>()
    val emptyData: LiveData<Event<Boolean>> get() = _emptyData

    val isFirstLoading = Transformations.map(films) {
        it is Resource.LOADING<*> && isFirstPageLoading()
    }

    lateinit var listType: ListType
    var lastKnownPosition = -1

    fun loadFilms(forceUpdate: Boolean = false) {
        baseScope.launch {
            incPageNumber()
            _films.value = Resource.LOADING()
            _films.value = loadByListType(forceUpdate)
        }
    }

    private suspend fun loadByListType(forceUpdate: Boolean = false): Resource<List<FilmModel>> =
        when (listType) {
            ListType.POPULAR -> repository.getPopularFilms(pageNumber, forceUpdate)
            ListType.TOP_RATED -> repository.getTopRatedFilms(pageNumber, forceUpdate)
            ListType.UPCOMING -> repository.getUpcomingFilms(pageNumber, forceUpdate)
            ListType.FAVOURITES -> repository.getFavouritesFilms(pageNumber)
        }

    fun isFavoriteList() = listType == ListType.FAVOURITES

    fun fetchedDataIsEmpty(isEmpty: Boolean) {
        _emptyData.value = Event(isEmpty && isFirstPageLoading())
    }
}
