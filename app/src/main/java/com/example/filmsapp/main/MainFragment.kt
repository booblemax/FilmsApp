package com.example.filmsapp.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.ListType
import com.example.filmsapp.base.common.EndlessRecyclerScrollListener
import com.example.filmsapp.base.common.SimpleItemDecoration
import com.example.filmsapp.base.common.WrappedGridLayoutManager
import com.example.filmsapp.common.FilmDetailsDto
import com.example.filmsapp.databinding.MainFragmentBinding
import com.example.filmsapp.util.snack
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding, MainState, MainIntents>() {

    override val layoutRes: Int = R.layout.main_fragment
    override val viewModel: MainViewModel by viewModel()

    private lateinit var adapter: MainAdapter

    private val listType: ListType get() =
        requireArguments().getInt(LIST_TYPE_ARG).run { ListType.values()[this] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.listType = listType
    }

    override fun render(state: MainState) {
        Timber.i(state.toString())
        with(state) {
            uiEvent?.let { processUiEvent(it) }

            adapter.isLoading = loading && !firstLoading
            binding.refreshLayout.isRefreshing = loading && firstLoading
            if (!isEmptyList) adapter.submitList(films.toMutableList())
            if (errorString != null && errorString.isNotEmpty()) view?.snack(errorString)
            if (errorMessage != null) view?.snack(errorMessage)
        }
    }

    private fun processUiEvent(event: Event<MainUiEvent>) {
        event.getContentIfNotHandled()?.let { uiEvent ->
            when (uiEvent) {
                is MainUiEvent.OpenFilm -> openFilmDetails(uiEvent.filmDetailsDto)
                is MainUiEvent.Back -> onBackPressed()
            }
        }
    }

    override fun init() {
        initTitle()
        initAdapter()
        initRecyclerView(adapter)
        initRefreshLayout()
        viewModel.pushIntent(MainIntents.InitialEvent)
    }

    private fun initTitle() {
        (activity as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.title = getString(listType.titleId)
        binding.mainToolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_back, context?.theme)
        binding.mainToolbar.setNavigationOnClickListener { viewModel.pushIntent(MainIntents.OnBack) }
    }

    private fun initAdapter() {
        adapter = MainAdapter {
            (binding.rvFilms.layoutManager as WrappedGridLayoutManager).let { layoutManager ->
                viewModel.lastKnownPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            }
            viewModel.pushIntent(
                MainIntents.OpenFilm(
                    FilmDetailsDto(it.id, it.poster, it.backdropPath)
                )
            )
        }
    }

    private fun initRecyclerView(adapter: MainAdapter) {
        val layoutManager = WrappedGridLayoutManager(context, MIN_COLUMN_COUNT)
        layoutManager.spanSizeLookup = MainSpanSizeLookup(adapter)
        binding.rvFilms.layoutManager = layoutManager
        binding.rvFilms.setHasFixedSize(true)
        binding.rvFilms.adapter = adapter
        binding.rvFilms.addItemDecoration(SimpleItemDecoration(MARGIN_OFFSET))
        binding.rvFilms.addOnScrollListener(object : EndlessRecyclerScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.pushIntent(MainIntents.LoadNextPage)
            }

            override fun isLastPage(): Boolean = false

            override fun isLoading(): Boolean = adapter.isLoading
        })
        if (viewModel.lastKnownPosition != -1) {
            binding.rvFilms.scrollToPosition(viewModel.lastKnownPosition)
        }
    }

    private fun initRefreshLayout() {
        binding.refreshLayout.setOnRefreshListener { viewModel.pushIntent(MainIntents.ReloadList) }
        binding.refreshLayout.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context?.theme)
        )
    }

    private fun openFilmDetails(filmDetailsDto: FilmDetailsDto) {
        viewModel.openDetails(
            filmDetailsDto.id,
            filmDetailsDto.posterUrl ?: "",
            filmDetailsDto.backdropUrl ?: "",
            filmDetailsDto.isFavorite
        )
    }

    companion object {
        const val TAG = "MainFragment"
        const val MIN_COLUMN_COUNT = 2
        const val MARGIN_OFFSET = 24

        private const val LIST_TYPE_ARG = "LIST_TYPE_ARG"

        fun newInstance(listType: Int): Fragment = MainFragment().apply {
            arguments = Bundle().apply {
                putInt(LIST_TYPE_ARG, listType)
            }
        }
    }
}
