package com.example.filmsapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.databinding.MainFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.base.EndlessRecyclerScrollListener
import com.example.filmsapp.util.snack
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>() {

    override val layoutRes: Int = R.layout.main_fragment
    override val viewModel: MainViewModel by viewModel()

    private lateinit var adapter: MainAdapter

    override fun init() {
        adapter = MainAdapter()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.resetPageNumber()
            viewModel.loadPopularFilms()
        }

        binding.refreshLayout.setColorSchemeColors(
            ResourcesCompat.getColor(resources, R.color.colorAccent, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimary, context?.theme),
            ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, context?.theme)
        )

        initRecyclerView(adapter)
        initListener(adapter)
    }

    private fun initRecyclerView(adapter: MainAdapter) {
        val layoutManager = GridLayoutManager(context, MIN_COLUMN_COUNT)
        binding.rvFilms.layoutManager = layoutManager
        binding.rvFilms.adapter = adapter
        binding.rvFilms.addItemDecoration(SimpleItemDecoration())
        binding.rvFilms.addOnScrollListener(object : EndlessRecyclerScrollListener(layoutManager) {

            override fun loadMoreItems() { viewModel.loadPopularFilms() }

            override fun isLastPage(): Boolean = false

            override fun isLoading(): Boolean = (adapter.isLoading)

        })
    }

    private fun initListener(adapter: MainAdapter) {
        viewModel.popularFilms.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> {
                    adapter.isLoading = false
                    it.data?.let { list ->
                        adapter.submitList(
                            list.toMutableList(),
                            viewModel.pageNumber == 1
                        )
                    }
                    viewModel.incPageNumber()
                }
                is Resource.ERROR -> {
                    binding.rvFilms.snack(it.message?.localizedMessage ?: "")
                    adapter.isLoading = false
                }
                is Resource.LOADING -> {
                    if (viewModel.pageNumber > 0)
                        adapter.isLoading = true
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadPopularFilms()
    }

    companion object {
        const val MIN_COLUMN_COUNT = 2
    }
}
