package com.example.filmsapp.ui.details

import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.filmsapp.R

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsFragmentBinding>() {

    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment



}