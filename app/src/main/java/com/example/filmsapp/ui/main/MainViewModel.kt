package com.example.filmsapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.filmsapp.domain.FilmsRepository
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FilmsRepository
) : BaseViewModel() {

    private var pageNumber = FIRST_PAGE_NUMBER

    private val _popularFilms = MutableLiveData<Resource<List<FilmModel>>>()
    val popularFilms: LiveData<Resource<List<FilmModel>>> get() = _popularFilms

    val isFirstLoading = Transformations.map(popularFilms) {
        it is Resource.LOADING<*> && pageNumber == FIRST_PAGE_NUMBER
    }

    var lastKnownPosition = -1

    init {
        loadPopularFilms(true)
    }

    fun loadPopularFilms(forceUpdate: Boolean = false) {
        baseContext.launch {
            incPageNumber()
            _popularFilms.value = Resource.LOADING()
            _popularFilms.value = repository.getPopularFilms(pageNumber, forceUpdate)
        }
    }

    fun resetPageNumber() {
        pageNumber = FIRST_PAGE_NUMBER
    }

    fun incPageNumber() {
        pageNumber++
    }

    fun decPageNumber() {
        pageNumber--
    }

    fun isFirstPageLoading() = pageNumber == FIRST_PAGE_NUMBER

    companion object {
        private const val FIRST_PAGE_NUMBER = 0
    }
}
