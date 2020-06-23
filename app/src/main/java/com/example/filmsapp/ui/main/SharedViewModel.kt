package com.example.filmsapp.ui.main

import com.example.filmsapp.ui.base.BaseViewModel

class SharedViewModel : BaseViewModel() {

    var backdropCarouselPosition: Int = DEFAULT_POSITION

    fun clearBackdropCarouselPosition() {
        backdropCarouselPosition = DEFAULT_POSITION
    }

    companion object {
        const val DEFAULT_POSITION = -1
    }
}
