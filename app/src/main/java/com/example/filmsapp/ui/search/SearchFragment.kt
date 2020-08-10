package com.example.filmsapp.ui.search

import com.example.filmsapp.R
import com.example.filmsapp.databinding.SearchFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

    override val viewModel: SearchViewModel by viewModel()
    override val layoutRes: Int = R.layout.search_fragment

    override fun init() {

    }
}