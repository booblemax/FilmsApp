package com.example.filmsapp.imagesCarousel

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.mvi.EmptyState
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ImagesCarouselViewModel(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<IState, Intention>(dispatcherProvider, EmptyState())
