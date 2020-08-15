package com.example.filmsapp.ui.lists

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
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
import com.example.filmsapp.util.animateVisible
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.src
import com.example.filmsapp.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListsFragment : BaseFragment<ListsViewModel, ListsFragmentBinding>() {

    override val viewModel: ListsViewModel by viewModel()
    override val layoutRes: Int = R.layout.lists_fragment

    private val filmItemClickListener: (FilmModel) -> Unit = { filmModel: FilmModel ->
        findNavController().navigate(
            ListsFragmentDirections.actionListsFragmentToDetailsFragment(
                filmModel.id,
                filmModel.poster,
                filmModel.backdropPath,
                false
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
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.listsToolbar)

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
        binding.listsPopularBackground.setOnClickListener {
            filmsBackgroundItemClickListener(
                ListType.POPULAR
            )
        }
        binding.listsTopratedBackground.setOnClickListener {
            filmsBackgroundItemClickListener(
                ListType.TOP_RATED
            )
        }
        binding.listsUpcomingBackground.setOnClickListener {
            filmsBackgroundItemClickListener(
                ListType.UPCOMING
            )
        }
    }

    private fun initListener() {
        viewModel.latestFilm.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.SUCCESS -> it.data?.let { model ->
                    if (model.poster != null) {
                        binding.listsLatestFilm.src(model.poster)
                        binding.listsLatestFilm.animateVisible()
                    }
                }
                is Resource.ERROR -> binding.listsLatestFilm.gone()
                is Resource.LOADING -> { /*do nothing*/
                }
            }
        }
        viewModel.popularFilms.observe(viewLifecycleOwner) {
            popularAdapter.isLoading = it is Resource.LOADING
            if (it is Resource.SUCCESS) it.data?.let { models ->
                popularAdapter.submitList(models.toMutableList())
                binding.listsPopularTitle.animateVisible()
                binding.listsPopularFilms.animateVisible()
                binding.listsPopularBackground.visible()
            }
        }
        viewModel.topRatedFilms.observe(viewLifecycleOwner) {
            topRatedAdapter.isLoading = it is Resource.LOADING
            if (it is Resource.SUCCESS) it.data?.let { models ->
                topRatedAdapter.submitList(models)
                binding.listsTopratedTitle.animateVisible()
                binding.listsTopratedFilms.animateVisible()
                binding.listsTopratedBackground.visible()
            }
        }
        viewModel.upcomingFilms.observe(viewLifecycleOwner) {
            upcomingAdapter.isLoading = it is Resource.LOADING
            if (it is Resource.SUCCESS) it.data?.let { models ->
                upcomingAdapter.submitList(models)
                binding.listsUpcomingTitle.animateVisible()
                binding.listsUpcomingFilms.animateVisible()
                binding.listsUpcomingBackground.visible()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.lists_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorites -> {
                findNavController().navigate(
                    ListsFragmentDirections.actionListsFragmentToMainFragment(ListType.FAVOURITES)
                )
                true
            }
            R.id.settings -> {
                findNavController().navigate(
                    ListsFragmentDirections.actionListsFragmentToSettingsFragment2()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val MARGIN_OFFSET = 12
    }
}
