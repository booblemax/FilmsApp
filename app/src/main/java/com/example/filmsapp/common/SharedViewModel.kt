package com.example.filmsapp.common

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel

class SharedViewModel(dispatcherProvider: DispatcherProvider) : BaseViewModel(dispatcherProvider) {

    var backdropCarouselPosition: Int =
        DEFAULT_POSITION

    fun clearBackdropCarouselPosition() {
        backdropCarouselPosition =
            DEFAULT_POSITION
    }

    companion object {
        const val DEFAULT_POSITION = -1
    }
}
