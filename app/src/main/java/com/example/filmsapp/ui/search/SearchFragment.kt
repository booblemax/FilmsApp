package com.example.filmsapp.ui.search

import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.databinding.SearchFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.base.common.EndlessRecyclerScrollListener
import com.example.filmsapp.ui.base.common.SimpleItemDecoration
import com.example.filmsapp.ui.base.common.WrappedGridLayoutManager
import com.example.filmsapp.util.setupToolbar
import com.example.filmsapp.util.snack
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

    override val viewModel: SearchViewModel by viewModel()
    override val layoutRes: Int = R.layout.search_fragment

    private lateinit var adapter: SearchAdapter
    private lateinit var searchView: SearchView

    override fun init() {
        initTitle()
        initAdapter()
        initRecyclerView(adapter)
        initListener()
    }

    private fun initTitle() {
        requireActivity().setupToolbar(binding.searchToolbar)
    }

    private fun initAdapter() {
        adapter = SearchAdapter {
            (binding.searchList.layoutManager as WrappedGridLayoutManager).let { layoutManager ->
                viewModel.lastKnownPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            }
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                    it.id,
                    it.poster,
                    it.backdropPath,
                    false
                )
            )
        }
    }

    private fun initRecyclerView(adapter: SearchAdapter) {
        with(binding.searchList) {
            val gridLayoutManager =
                WrappedGridLayoutManager(
                    context,
                    MIN_COLUMN_COUNT
                )
            gridLayoutManager.spanSizeLookup = SearchSpanSizeLookup(adapter)
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            this.adapter = adapter
            addItemDecoration(SimpleItemDecoration(MARGIN_OFFSET))

            addOnScrollListener(object : EndlessRecyclerScrollListener(gridLayoutManager) {
                    override fun loadMoreItems() {
                        viewModel.callLoad(searchView.query.toString())
                    }

                    override fun isLastPage(): Boolean = false

                    override fun isLoading(): Boolean = adapter.isLoading
                }
            )

            if (viewModel.lastKnownPosition != -1) {
                scrollToPosition(viewModel.lastKnownPosition)
            }
        }
    }

    private fun initListener() {
        viewModel.films.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> {
                    adapter.isLoading = false
                    viewModel.fetchedDataIsEmpty(it.data?.isEmpty() ?: true)
                    adapter.submitList(it.data?.toMutableList() ?: mutableListOf())
                }
                is Resource.ERROR -> {
                    adapter.isLoading = false
                    viewModel.fetchedDataIsEmpty(true)
                    viewModel.decPageNumber()
                    binding.searchList.snack(it.message?.localizedMessage ?: "")
                }
                is Resource.LOADING -> adapter.isLoading = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        menu.findItem(R.id.search)?.let {
            it.expandActionView()
            searchView = it.actionView as SearchView
            searchView.setOnQueryTextListener(viewModel.textListener)
            searchView.requestFocus()
        }
    }

    companion object {
        const val MIN_COLUMN_COUNT = 2
        const val MARGIN_OFFSET = 24
    }
}
