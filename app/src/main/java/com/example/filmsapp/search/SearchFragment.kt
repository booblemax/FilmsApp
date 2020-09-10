package com.example.filmsapp.search

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.common.EndlessRecyclerScrollListener
import com.example.filmsapp.base.common.SimpleItemDecoration
import com.example.filmsapp.base.common.WrappedGridLayoutManager
import com.example.filmsapp.common.FilmDetailsDto
import com.example.filmsapp.databinding.SearchFragmentBinding
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.setupToolbar
import com.example.filmsapp.util.snack
import com.example.filmsapp.util.visible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment :
    BaseFragment<SearchViewModel, SearchFragmentBinding, SearchState, SearchIntents>() {

    override val viewModel: SearchViewModel by viewModel()
    override val layoutRes: Int = R.layout.search_fragment

    private lateinit var adapter: SearchAdapter
    private lateinit var searchView: SearchView

    override fun render(state: SearchState) {
        Timber.i(state.toString())
        with(state) {
            uiEvent?.let { processUiEvent(it) }

            adapter.isLoading = loading
            manageUiVisibility(isEmptyList, isEmptyQuery)

            if (!isEmptyList) adapter.submitList(films.toMutableList())
            if (errorString != null && errorString.isNotEmpty()) view?.snack(errorString)
            if (errorMessage != null) view?.snack(errorMessage)
        }
    }

    private fun processUiEvent(event: Event<SearchUiEvent>) {
        event.getContentIfNotHandled()?.let { uiEvent ->
            when (uiEvent) {
                is SearchUiEvent.OpenFilm -> openFilmDetails(uiEvent.filmDetailsDto)
                is SearchUiEvent.Back -> onBackPressed()
            }
        }
    }

    private fun manageUiVisibility(isEmptyList: Boolean, isEmptyQuery: Boolean) {
        with(binding) {
            if (isEmptyList) {
                searchList.gone()
                searchLabel.visible()
                searchLabel.text =
                    if (isEmptyQuery) getString(R.string.empty_data)
                    else getString(R.string.label_input_film_name)
            } else {
                searchList.visible()
                searchLabel.gone()
            }
        }
    }

    override fun init() {
        setHasOptionsMenu(true)
        initTitle()
        initAdapter()
        initRecyclerView(adapter)
    }

    private fun initTitle() {
        requireActivity().setupToolbar(binding.searchToolbar)
    }

    private fun initAdapter() {
        adapter = SearchAdapter {
            (binding.searchList.layoutManager as WrappedGridLayoutManager).let { layoutManager ->
                viewModel.lastKnownPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            }
            viewModel.pushIntent(
                SearchIntents.OpenFilm(FilmDetailsDto(it.id, it.poster, it.backdropPath))
            )
        }
    }

    private fun initRecyclerView(adapter: SearchAdapter) {
        with(binding.searchList) {
            val gridLayoutManager = WrappedGridLayoutManager(context, MIN_COLUMN_COUNT)
            gridLayoutManager.spanSizeLookup = SearchSpanSizeLookup(adapter)
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            this.adapter = adapter
            addItemDecoration(SimpleItemDecoration(MARGIN_OFFSET))

            addOnScrollListener(object : EndlessRecyclerScrollListener(gridLayoutManager) {
                override fun loadMoreItems() {
                    viewModel.pushIntent(SearchIntents.LoadNextPage(searchView.query.toString()))
                }

                override fun isLastPage(): Boolean = false

                override fun isLoading(): Boolean = adapter.isLoading
            })

            if (viewModel.lastKnownPosition != -1) {
                scrollToPosition(viewModel.lastKnownPosition)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        menu.findItem(R.id.search)?.let {
            it.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.pushIntent(SearchIntents.OnBack)
                    return false
                }
            })
            it.expandActionView()
            searchView = it.actionView as SearchView
            searchView.setOnQueryTextListener(viewModel.textListener)
            searchView.requestFocus()
        }
    }

    private fun openFilmDetails(filmDetailsDto: FilmDetailsDto) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                filmDetailsDto.id,
                filmDetailsDto.posterUrl,
                filmDetailsDto.posterUrl,
                filmDetailsDto.isFavorite
            )
        )
    }

    companion object {
        const val MIN_COLUMN_COUNT = 2
        const val MARGIN_OFFSET = 24
    }
}
