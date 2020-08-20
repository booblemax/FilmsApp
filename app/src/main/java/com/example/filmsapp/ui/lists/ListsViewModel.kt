package com.example.filmsapp.ui.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.dispatcherProvider.DispatcherProvider
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ListsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val repository: FilmsRepository
) : BaseViewModel(dispatcherProvider) {

    private val _latestFilm = MutableLiveData<Resource<FilmModel>>()
    val latestFilm: LiveData<Resource<FilmModel>> get() = _latestFilm

    private val _popularFilms = MutableLiveData<Resource<List<FilmModel>>>()
    val popularFilms: LiveData<Resource<List<FilmModel>>> get() = _popularFilms

    private val _topratedFilms = MutableLiveData<Resource<List<FilmModel>>>()
    val topRatedFilms: LiveData<Resource<List<FilmModel>>> get() = _topratedFilms

    private val _upcomingFilms = MutableLiveData<Resource<List<FilmModel>>>()
    val upcomingFilms: LiveData<Resource<List<FilmModel>>> get() = _upcomingFilms

    init {
        loadFilms()
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
        _latestFilm.postValue(Resource.LOADING())
        _latestFilm.postValue(repository.getLatestFilm())
    }

    private suspend fun loadPopular() {
        _popularFilms.postValue(Resource.LOADING())
        _popularFilms.postValue(repository.getPopularFilms())
    }

    private suspend fun loadTopRated() {
        _topratedFilms.postValue(Resource.LOADING())
        _topratedFilms.postValue(repository.getTopRatedFilms())
    }

    private suspend fun loadUpcoming() {
        _upcomingFilms.postValue(Resource.LOADING())
        _upcomingFilms.postValue(repository.getUpcomingFilms())
    }
}
