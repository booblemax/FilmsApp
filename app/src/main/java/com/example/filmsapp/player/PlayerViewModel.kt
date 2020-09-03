package com.example.filmsapp.player

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.mvi.EmptyState
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PlayerViewModel(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<IState, Intention>(dispatcherProvider, EmptyState()) {

    var lastStoppedTime: Float = 0f
}
