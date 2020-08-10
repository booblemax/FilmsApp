package com.example.filmsapp.ui.splash

import com.example.filmsapp.R
import com.example.filmsapp.databinding.SplashFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>() {

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.splash_fragment

    override fun init() {

    }
}
