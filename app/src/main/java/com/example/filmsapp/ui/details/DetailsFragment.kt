package com.example.filmsapp.ui.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.filmsapp.R
import com.example.filmsapp.domain.Resource

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsFragmentBinding>() {

    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment

    override fun init() {
        binding.detailsBack.setOnClickListener { onBackPressed() }
        initListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val args = DetailsFragmentArgs.fromBundle(it)
            with(args) {
                binding.posterUrl = posterUrl
                binding.backdropUrl = backdropUrl
                viewModel.loadFilm(filmId)
            }
        }
    }

    private fun initListener() {
        viewModel.film.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.SUCCESS) {
                binding.model = resource.data
            }
        }
    }

    override fun onBackPressed() {
        findNavController().navigateUp()
    }
}