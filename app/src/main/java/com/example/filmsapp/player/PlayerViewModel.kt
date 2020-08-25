package com.example.filmsapp.player

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel

class PlayerViewModel(dispatcherProvider: DispatcherProvider) : BaseViewModel(dispatcherProvider) {

    var lastStoppedTime: Float = 0f
}
