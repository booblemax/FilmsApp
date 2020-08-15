package com.example.filmsapp.ui.common

import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.ui.base.BaseViewModel

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
