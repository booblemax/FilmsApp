package com.example.filmsapp.ui.di

import com.example.filmsapp.ui.details.DetailsViewModel
import com.example.filmsapp.ui.imagesCarousel.ImagesCarouselViewModel
import com.example.filmsapp.ui.lists.ListsViewModel
import com.example.filmsapp.ui.main.MainViewModel
import com.example.filmsapp.ui.main.SharedViewModel
import com.example.filmsapp.ui.player.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {

    viewModel { SharedViewModel() }

    viewModel { ListsViewModel(get()) }

    viewModel { MainViewModel(get()) }

    viewModel { DetailsViewModel(get(), get()) }

    viewModel { ImagesCarouselViewModel() }

    viewModel { PlayerViewModel() }
}
