package com.example.filmsapp.ui.di

import com.example.filmsapp.ui.details.DetailsViewModel
import com.example.filmsapp.ui.imagesCarousel.ImagesCarouselViewModel
import com.example.filmsapp.ui.main.MainViewModel
import com.example.filmsapp.ui.main.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    viewModel { SharedViewModel() }

    viewModel { MainViewModel(get()) }

    viewModel { DetailsViewModel(get()) }

    viewModel { ImagesCarouselViewModel() }
}
