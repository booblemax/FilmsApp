package com.example.filmsapp.di

import com.example.filmsapp.common.SharedViewModel
import com.example.filmsapp.details.DetailsViewModel
import com.example.filmsapp.imagesCarousel.ImagesCarouselViewModel
import com.example.filmsapp.lists.ListsViewModel
import com.example.filmsapp.main.MainViewModel
import com.example.filmsapp.player.PlayerViewModel
import com.example.filmsapp.search.SearchViewModel
import com.example.filmsapp.settings.SettingsViewModel
import com.example.filmsapp.splash.SplashViewModel
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
