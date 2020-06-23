package com.example.filmsapp.ui.details

import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.FilmsRepository
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: FilmsRepository
) : BaseViewModel() {

    private val _film = MutableLiveData<Resource<FilmModel>>()
    val film get() = _film

    fun loadFilm(id: String) {
        baseContext.launch {
            _film.value = Resource.LOADING()
            _film.value = repository.getFilm(id)
        }
    }
}
