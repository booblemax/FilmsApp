package com.example.filmsapp.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.domain.Resource
import com.example.filmsapp.R
import com.example.filmsapp.databinding.MainFragmentBinding
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.common.EndlessRecyclerScrollListener
import com.example.filmsapp.base.common.SimpleItemDecoration
import com.example.filmsapp.base.common.WrappedGridLayoutManager
import com.example.filmsapp.util.snack
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>() {

    override val layoutRes: Int = R.layout.main_fragment
    override val viewModel: MainViewModel by viewModel()

    private lateinit var args: MainFragmentArgs

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = MainFragmentArgs.fromBundle(requireArguments())
        viewModel.listType = args.listType
        viewModel.loadFilms(true)
    }

    override fun init() {
        initTitle()
        initAdapter()
        initRecyclerView(adapter)
        initRefreshLayout()
        initListener(adapter)
    }

    private fun initTitle() {
        (activity as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        binding.mainToolbar.title = getString(args.listType.titleId)
        binding.mainToolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_arrow_back, context?.theme)
        binding.mainToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initAdapter() {
        adapter = MainAdapter {
            (binding.rvFilms.layoutManager as WrappedGridLayoutManager).let { layoutManager ->
                viewModel.lastKnownPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            }
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailsFragment(
                    it.id,
                    it.poster,
                    it.backdropPath,
                    viewModel.isFavoriteList()
                )
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
        binding.rvFilms.addItemDecoration(SimpleItemDecoration(MARGIN_OFFSET))
        binding.rvFilms.addOnScrollListener(object : EndlessRecyclerScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.loadFilms()
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
            viewModel.loadFilms(true)
        }

        binding.refreshLayout.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context?.theme)
        )
    }

    private fun initListener(adapter: MainAdapter) {
        viewModel.films.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> {
                    adapter.isLoading = false
                    viewModel.fetchedDataIsEmpty(it.data?.isEmpty() ?: true)
                    adapter.submitList(it.data?.toMutableList() ?: mutableListOf())
                }
                is Resource.ERROR -> {
                    viewModel.fetchedDataIsEmpty(true)
                    viewModel.decPageNumber()
                    binding.rvFilms.snack(it.message?.localizedMessage ?: "")
                    adapter.isLoading = false
                }
                is Resource.LOADING -> {
                    if (!viewModel.isFirstPageLoading()) {
                        adapter.isLoading = true
                    }
                }
            }
        }
    }

    companion object {
        const val MIN_COLUMN_COUNT = 2
        const val MARGIN_OFFSET = 24
    }
}
