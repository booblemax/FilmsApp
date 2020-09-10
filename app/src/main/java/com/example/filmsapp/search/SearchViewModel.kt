package com.example.filmsapp.search

import com.example.domain.Resource
import com.example.domain.dispatchers.DispatcherProvider
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.R
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.PagedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(
    dispatcherProvider: DispatcherProvider,
    private val filmsRepository: FilmsRepository
) : PagedViewModel<SearchState, SearchIntents>(dispatcherProvider, SearchState()) {

    val textListener = TextListener().apply {
        channel
            .debounce(DEBOUNCE_TIME)
            .filter { query ->
                query.isNotEmpty().also { reduce { it.copy(isEmptyQuery = true) } }
            }
            .distinctUntilChanged()
            .flowOn(dispatcherProvider.default())
            .onEach { processIntention(SearchIntents.LoadQuery(it)) }
            .launchIn(baseScope)
    }

    var lastKnownPosition = -1

    override suspend fun processIntention(intent: SearchIntents) {
        super.processIntention(intent)
        when (intent) {
            is SearchIntents.LoadQuery -> {
                resetPageNumber()
                incPageNumber()
                loadFilms(intent.query, true)
            }
            is SearchIntents.LoadNextPage -> {
                incPageNumber()
                loadFilms(intent.query)
            }
            is SearchIntents.OpenFilm -> reduce {
                it.copy(uiEvent = Event(SearchUiEvent.OpenFilm(intent.filmDetailsDto)))
            }
            is SearchIntents.OnBack -> reduce {
                it.copy(uiEvent = Event(SearchUiEvent.Back))
            }
        }
    }

    private fun loadFilms(query: String, forceUpdate: Boolean = false) {
        baseScope.launch {
            reduce { it.copy(loading = true) }
            val searchRes = filmsRepository.searchFilms(query, pageNumber, forceUpdate)
            reduce {
                when (searchRes) {
                    is Resource.SUCCESS -> {
                        val films = searchRes.data ?: listOf()
                        it.copy(
                            loading = false,
                            films = films,
                            isEmptyList = films.isEmpty()
                        )
                    }
                    is Resource.ERROR ->
                        it.copy(
                            loading = false,
                            isEmptyList = true,
                            errorString = searchRes.error?.message
                        )
                    else -> it.copy(
                        loading = false,
                        isEmptyList = true,
                        errorMessage = R.string.error
                    )
                }
            }
        }
    }

    companion object {
        private const val DEBOUNCE_TIME = 500L
    }
}
