package com.example.filmsapp.ui.player

import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.ui.base.BaseViewModel

class PlayerViewModel(dispatcherProvider: DispatcherProvider) : BaseViewModel(dispatcherProvider) {

    var lastStoppedTime: Float = 0f
}
