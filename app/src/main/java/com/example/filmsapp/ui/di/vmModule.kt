package com.example.filmsapp.ui.di

import com.example.filmsapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    viewModel { MainViewModel(get()) }

}