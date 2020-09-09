package com.example.filmsapp.imagesCarousel

import com.example.filmsapp.base.mvi.BaseIntent

sealed class ImageCarouselIntents : BaseIntent() {

    data class Initial(val urls: List<String>, val position: Int) : ImageCarouselIntents()
}
