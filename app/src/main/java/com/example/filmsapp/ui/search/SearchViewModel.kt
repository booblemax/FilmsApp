package com.example.filmsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.ui.base.PagedViewModel
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(
    dispatcherProvider: DispatcherProvider,
    private val filmsRepository: FilmsRepository
) : PagedViewModel(dispatcherProvider) {

    private val _films = MutableLiveData<Resource<List<FilmModel>>>()
    val films: LiveData<Resource<List<FilmModel>>> get() = _films

    private val _emptyQuery = MutableLiveData<Event<Boolean>>()
    val emptyQuery: LiveData<Event<Boolean>> get() = _emptyQuery

    private val _emptyData = MutableLiveData<Event<Boolean>>()
    val emptyData: LiveData<Event<Boolean>> get() = _emptyData

    val textListener = TextListener().apply {
        baseScope.launch {
            channel
                .debounce(DEBOUNCE_TIME)
                .filter { query ->
                    query.isNotEmpty().also { _emptyQuery.postValue(Event(it)) }
                }
                .distinctUntilChanged()
                .flowOn(dispatcherProvider.default())
                .collect {
                    resetPageNumber()
                    this@SearchViewModel.loadFilms(it, true)
                }
        }
    }

    var lastKnownPosition = -1

    fun loadFilms(query: String, forceUpdate: Boolean = false) {
        baseScope.launch {
            incPageNumber()
            _films.value = Resource.LOADING()
            _films.value = filmsRepository.searchFilms(query, pageNumber, forceUpdate)
        }
    }

    fun fetchedDataIsEmpty(isEmpty: Boolean) {
        _emptyData.value = Event(isEmpty && isFirstPageLoading())
    }

    companion object {
        private const val DEBOUNCE_TIME = 500L
    }
}
