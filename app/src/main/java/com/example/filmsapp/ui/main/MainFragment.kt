package com.example.filmsapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmsapp.R
import com.example.filmsapp.databinding.MainFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.util.snack
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>() {

    override val layoutRes: Int = R.layout.main_fragment
    override val viewModel: MainViewModel by viewModel()

    override fun init() {
        binding.viewModel = viewModel
        val adapter = MainAdapter()

        binding.refreshLayout.setOnRefreshListener {
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
        binding.rvFilms.layoutManager =
            GridLayoutManager(context, MIN_COLUMN_COUNT)
        binding.rvFilms.adapter = adapter
        binding.rvFilms.addItemDecoration(SimpleItemDecoration())
    }

    private fun initListener(adapter: MainAdapter) {
        viewModel.popularFilms.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> it.data?.let { list -> adapter.submitList(list) }
                is Resource.ERROR -> binding.rvFilms.snack(it.message?.localizedMessage ?: "")
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
