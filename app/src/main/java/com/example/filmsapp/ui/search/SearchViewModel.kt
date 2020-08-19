package com.example.filmsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(
    dispatcherProvider: DispatcherProvider,
    private val filmsRepository: FilmsRepository
) : BaseViewModel(dispatcherProvider) {

    private var pageNumber = FIRST_PAGE_NUMBER

    private val _films = MutableLiveData<Resource<List<FilmModel>>>()
    val films: LiveData<Resource<List<FilmModel>>> get() = _films

    private val _emptyData = MutableLiveData<Event<Boolean>>()
    val emptyData: LiveData<Event<Boolean>> get() = _emptyData

    val textListener = TextListener().apply {
        baseScope.launch {
            channel.debounce(DEBOUNCE_TIME).flowOn(baseScope.coroutineContext).collect {
                if (it.isNotEmpty()) {
                    resetPageNumber()
                    this@SearchViewModel.loadFilms(it)
                }
            }
        }
    }

    var lastKnownPosition = -1

    fun loadFilms(query: String) {
        baseScope.launch {
            incPageNumber()
            _films.value = Resource.LOADING()
            _films.value = filmsRepository.searchFilms(query, pageNumber)
        }
    }

    fun fetchedDataIsEmpty(isEmpty: Boolean) {
        _emptyData.value = Event(isEmpty && pageNumber == FIRST_PAGE_NUMBER)
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
        private const val DEBOUNCE_TIME = 500L
    }
}
