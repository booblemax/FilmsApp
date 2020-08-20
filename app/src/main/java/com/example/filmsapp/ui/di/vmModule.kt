package com.example.filmsapp.ui.di

import com.example.filmsapp.ui.common.SharedViewModel
import com.example.filmsapp.ui.details.DetailsViewModel
import com.example.filmsapp.ui.imagesCarousel.ImagesCarouselViewModel
import com.example.filmsapp.ui.lists.ListsViewModel
import com.example.filmsapp.ui.main.MainViewModel
import com.example.filmsapp.ui.player.PlayerViewModel
import com.example.filmsapp.ui.search.SearchViewModel
import com.example.filmsapp.ui.settings.SettingsViewModel
import com.example.filmsapp.ui.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val vmModule = module {

    viewModel { SharedViewModel(get()) }

    viewModel { ListsViewModel(get(), get()) }

    viewModel { MainViewModel(get(), get()) }

    viewModel { DetailsViewModel(get(), get(), get()) }

    viewModel { ImagesCarouselViewModel(get()) }

    viewModel { PlayerViewModel(get()) }

    viewModel { SettingsViewModel(get(), get()) }

    viewModel { SplashViewModel(get()) }

    viewModel { SearchViewModel(get(), get()) }
}
