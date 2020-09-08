package com.example.filmsapp.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
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

    private lateinit var args: MainFragmentArgs
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = MainFragmentArgs.fromBundle(requireArguments())
        viewModel.listType = args.listType
    }

    override fun render(state: MainState) {
        Timber.i(state.toString())
        with(state) {
            when (uiEvent) {
                is MainUiEvent.OpenFilm -> openFilmDetails(uiEvent.filmDetailsDto)
                is MainUiEvent.Back -> onBackPressed()
            }

            adapter.isLoading = loading && !firstLoading
            binding.refreshLayout.isRefreshing = loading && firstLoading
            if (!isEmptyList) adapter.submitList(films.toMutableList())
            if (errorString != null && errorString.isNotEmpty()) view?.snack(errorString)
            if (errorMessage != null) view?.snack(errorMessage)
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
        binding.mainToolbar.title = getString(args.listType.titleId)
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
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailsFragment(
                filmDetailsDto.id,
                filmDetailsDto.posterUrl,
                filmDetailsDto.backdropUrl,
                filmDetailsDto.isFavorite
            )
        )
    }

    companion object {
        const val MIN_COLUMN_COUNT = 2
        const val MARGIN_OFFSET = 24
    }
}
