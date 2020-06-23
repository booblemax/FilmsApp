package com.example.filmsapp.ui.main

import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.databinding.MainFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.base.common.EndlessRecyclerScrollListener
import com.example.filmsapp.ui.base.common.WrappedGridLayoutManager
import com.example.filmsapp.util.snack
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>() {

    override val layoutRes: Int = R.layout.main_fragment
    override val viewModel: MainViewModel by viewModel()

    private lateinit var adapter: MainAdapter

    override fun init() {
        initAdapter()
        initRecyclerView(adapter)
        initRefreshLayout()
        initListener(adapter)
    }

    private fun initAdapter() {
        adapter = MainAdapter {
            (binding.rvFilms.layoutManager as WrappedGridLayoutManager).let { layoutManager ->
                viewModel.lastKnownPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            }
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(it.id, it.poster, it.backdropPath)
            )
        }
    }

    private fun initRecyclerView(adapter: MainAdapter) {
        val layoutManager =
            WrappedGridLayoutManager(
                context,
                MIN_COLUMN_COUNT
            )
        layoutManager.spanSizeLookup = MainSpanSizeLookup(adapter)
        binding.rvFilms.layoutManager = layoutManager
        binding.rvFilms.setHasFixedSize(true)
        binding.rvFilms.adapter = adapter
        binding.rvFilms.addItemDecoration(SimpleItemDecoration())
        binding.rvFilms.addOnScrollListener(object : EndlessRecyclerScrollListener(layoutManager) {

            override fun loadMoreItems() {
                viewModel.loadPopularFilms()
            }

            override fun isLastPage(): Boolean = false

            override fun isLoading(): Boolean = adapter.isLoading
        })
        if (viewModel.lastKnownPosition != -1) {
            binding.rvFilms.scrollToPosition(viewModel.lastKnownPosition)
        }
    }

    private fun initRefreshLayout() {
        binding.refreshLayout.setOnRefreshListener {
            viewModel.resetPageNumber()
            viewModel.loadPopularFilms(true)
        }

        binding.refreshLayout.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context?.theme)
        )
    }

    private fun initListener(adapter: MainAdapter) {
        viewModel.popularFilms.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> {
                    adapter.isLoading = false
                    it.data?.let { list ->
                        adapter.submitList(list.toMutableList())
                    }
                }
                is Resource.ERROR -> {
                    binding.rvFilms.snack(it.message?.localizedMessage ?: "")
                    viewModel.decPageNumber()
                    adapter.isLoading = false
                }
                is Resource.LOADING -> {
                    if (!viewModel.isFirstPageLoading())
                        adapter.isLoading = true
                }
            }
        }
    }

    companion object {
        const val MIN_COLUMN_COUNT = 2
    }
}
