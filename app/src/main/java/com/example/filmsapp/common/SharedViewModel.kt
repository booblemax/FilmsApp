package com.example.filmsapp.common

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

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
