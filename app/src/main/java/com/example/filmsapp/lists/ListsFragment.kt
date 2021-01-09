package com.example.filmsapp.lists

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.FilmModel
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.common.SimpleItemDecoration
import com.example.filmsapp.base.common.WrappedLinearLayoutManager
import com.example.filmsapp.common.FilmDetailsDto
import com.example.filmsapp.databinding.ListsFragmentBinding
import com.example.filmsapp.util.animateVisible
import com.example.filmsapp.util.src
import com.example.filmsapp.util.visible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class ListsFragment :
    BaseFragment<ListsViewModel, ListsFragmentBinding, ListsState, ListsIntents>() {

    override val viewModel: ListsViewModel by viewModel()
    override val layoutRes: Int = R.layout.lists_fragment

    private val filmItemClickListener: (FilmModel) -> Unit = { model: FilmModel ->
        viewModel.pushIntent(
            ListsIntents.OpenFilm(FilmDetailsDto(model.id, model.poster, model.backdropPath))
        )
    }

    private val popularAdapter = ListsAdapter(filmItemClickListener)
    private val topRatedAdapter = ListsAdapter(filmItemClickListener)
    private val upcomingAdapter = ListsAdapter(filmItemClickListener)

    override fun render(state: ListsState) {
        Timber.i(state.toString())
        with(state) {
            uiEvent?.let { processUiEvent(it) }

            popularAdapter.isLoading = popularLoading
            topRatedAdapter.isLoading = topratedLoading
            upcomingAdapter.isLoading = upcomingLoading

            latestFilm?.let { initLatestFilm(it) }
            initPopularList(popularFilms)
            initTopratedList(topRatedFilms)
            initUpcomingList(upcomingFilms)
        }
    }

    private fun processUiEvent(event: Event<ListsUiEvent>) {
        event.getContentIfNotHandled()?.let { uiEvent ->
            when (uiEvent) {
                is ListsUiEvent.OpenList -> viewModel.openMainScreen(uiEvent.type.ordinal)
                is ListsUiEvent.OpenFilm -> viewModel.openDetailsScreen(
                    uiEvent.filmDetailsDto.id,
                    uiEvent.filmDetailsDto.posterUrl ?: "",
                    uiEvent.filmDetailsDto.backdropUrl ?: "",
                    uiEvent.filmDetailsDto.isFavorite
                )
                is ListsUiEvent.OpenSearch -> viewModel.openSearchScreen()
                is ListsUiEvent.OpenSettings -> viewModel.openSettingsScreen()
            }
        }
    }

    private fun initLatestFilm(model: FilmModel) {
        binding.listsLatestFilm.src(model.poster)
        binding.listsLatestFilm.animateVisible()
    }

    private fun initPopularList(list: List<FilmModel>) {
        popularAdapter.submitList(list)
        if (list.isNotEmpty()) {
            binding.listsPopularTitle.animateVisible()
            binding.listsPopularFilms.animateVisible()
            binding.listsPopularBackground.visible()
        }
    }

    private fun initTopratedList(list: List<FilmModel>) {
        topRatedAdapter.submitList(list)
        if (list.isNotEmpty()) {
            binding.listsTopratedTitle.animateVisible()
            binding.listsTopratedFilms.animateVisible()
            binding.listsTopratedBackground.visible()
        }
    }

    private fun initUpcomingList(list: List<FilmModel>) {
        upcomingAdapter.submitList(list)
        if (list.isNotEmpty()) {
            binding.listsUpcomingTitle.animateVisible()
            binding.listsUpcomingFilms.animateVisible()
            binding.listsUpcomingBackground.visible()
        }
    }

    override fun init() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.listsToolbar)

        initRecyclerView(binding.listsPopularFilms, popularAdapter)
        initRecyclerView(binding.listsTopratedFilms, topRatedAdapter)
        initRecyclerView(binding.listsUpcomingFilms, upcomingAdapter)

        initClickListeners()
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: ListsAdapter) {
        recyclerView.layoutManager =
            WrappedLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SimpleItemDecoration(MARGIN_OFFSET))
    }

    private fun initClickListeners() {
        binding.listsLatestFilm.setOnClickListener {
            viewModel.state.value.latestFilm?.let {
                    viewModel.pushIntent(
                        ListsIntents.OpenFilm(FilmDetailsDto(it.id, it.poster, it.backdropPath))
                    )
                }
            }

        binding.listsPopularBackground.setOnClickListener {
            viewModel.pushIntent(ListsIntents.OpenLists(ListType.POPULAR))
        }
        binding.listsTopratedBackground.setOnClickListener {
            viewModel.pushIntent(ListsIntents.OpenLists(ListType.TOP_RATED))
        }
        binding.listsUpcomingBackground.setOnClickListener {
            viewModel.pushIntent(ListsIntents.OpenLists(ListType.UPCOMING))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lists_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                viewModel.pushIntent(ListsIntents.OpenSearch)
                true
            }
            R.id.favorites -> {
                viewModel.pushIntent(ListsIntents.OpenLists(ListType.FAVOURITES))
                true
            }
            R.id.settings -> {
                viewModel.pushIntent(ListsIntents.OpenSettings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val TAG = "ListsFragment"
        const val MARGIN_OFFSET = 12
    }
}
