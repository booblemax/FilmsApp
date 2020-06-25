package com.example.filmsapp.ui.lists

import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ListsFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.base.common.SimpleItemDecoration
import com.example.filmsapp.ui.base.common.WrappedLinearLayoutManager
import com.example.filmsapp.ui.base.models.FilmModel
import com.example.filmsapp.ui.base.models.ListType
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.src
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListsFragment : BaseFragment<ListsViewModel, ListsFragmentBinding>() {

    override val viewModel: ListsViewModel by viewModel()
    override val layoutRes: Int = R.layout.lists_fragment

    private val filmItemClickListener: (FilmModel) -> Unit = { filmModel: FilmModel ->
        findNavController().navigate(
            ListsFragmentDirections.actionListsFragmentToDetailsFragment(
                filmModel.id,
                filmModel.poster,
                filmModel.backdropPath
            )
        )
    }

    private val filmsBackgroundItemClickListener: (ListType) -> Unit = { type ->
        findNavController().navigate(
            ListsFragmentDirections.actionListsFragmentToMainFragment(type)
        )
    }

    private val popularAdapter = ListsAdapter(filmItemClickListener)
    private val topRatedAdapter = ListsAdapter(filmItemClickListener)
    private val upcomingAdapter = ListsAdapter(filmItemClickListener)

    override fun init() {
        initRecyclerView(binding.listsPopularFilms, popularAdapter)
        initRecyclerView(binding.listsTopratedFilms, topRatedAdapter)
        initRecyclerView(binding.listsUpcomingFilms, upcomingAdapter)

        initClickListeners()
        initListener()
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: ListsAdapter) {
        recyclerView.layoutManager =
            WrappedLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SimpleItemDecoration(MARGIN_OFFSET))
    }

    private fun initClickListeners() {
        binding.listsPopularBackground.setOnClickListener { filmsBackgroundItemClickListener(ListType.POPULAR) }
        binding.listsTopratedBackground.setOnClickListener { filmsBackgroundItemClickListener(ListType.TOP_RATED) }
        binding.listsPopularBackground.setOnClickListener { filmsBackgroundItemClickListener(ListType.POPULAR) }
    }

    private fun initListener() {
        viewModel.latestFilm.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> it.data?.let { model ->
                    if (model.poster != null) {
                        binding.listsLatestFilm.src(model.poster)
                    } else {
                        binding.listsLatestFilm.gone()
                    }
                }
                is Resource.ERROR -> binding.listsLatestFilm.gone()
            }
        }
        viewModel.popularFilms.observe(viewLifecycleOwner) {
            popularAdapter.isLoading = it is Resource.LOADING
            when (it) {
                is Resource.SUCCESS -> it.data?.let { models ->
                    popularAdapter.submitList(models.toMutableList())
                }
                is Resource.ERROR -> {
                    binding.listsPopularTitle.gone()
                    binding.listsPopularFilms.gone()
                }
            }
        }
        viewModel.topRatedFilms.observe(viewLifecycleOwner) {
            topRatedAdapter.isLoading = it is Resource.LOADING
            when (it) {
                is Resource.SUCCESS -> it.data?.let { models ->
                    topRatedAdapter.submitList(models)
                }
                is Resource.ERROR -> {
                    binding.listsTopratedTitle.gone()
                    binding.listsTopratedFilms.gone()
                }
            }
        }
        viewModel.upcomingFilms.observe(viewLifecycleOwner) {
            upcomingAdapter.isLoading = it is Resource.LOADING
            when (it) {
                is Resource.SUCCESS -> it.data?.let { models -> upcomingAdapter.submitList(models) }
                is Resource.ERROR -> {
                    binding.listsUpcomingTitle.gone()
                    binding.listsUpcomingFilms.gone()
                }
            }
        }
    }

    companion object {
        const val MARGIN_OFFSET = 12
    }
}
