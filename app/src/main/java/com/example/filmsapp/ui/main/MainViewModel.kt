package com.example.filmsapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.FilmsRepository
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FilmsRepository
) : BaseViewModel() {

    private val _popularFilms = MutableLiveData<Resource<List<FilmModel>>>()
    val popularFilms: LiveData<Resource<List<FilmModel>>> get() = _popularFilms

    fun loadPopularFilms() {
        baseContext.launch {
            _popularFilms.value = Resource.LOADING()
            _popularFilms.value = repository.getPopularFilm()
        }
    }
}
