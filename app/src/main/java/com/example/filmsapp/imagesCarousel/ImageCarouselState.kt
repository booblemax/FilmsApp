package com.example.filmsapp.imagesCarousel

import com.example.filmsapp.base.mvi.IState

data class ImageCarouselState(
    val urls: List<String> = listOf(),
    val position: Int = 0
) : IState {

    override fun toString(): String {
        return "ImageCarouselState(urls=${urls::class.java}, position=$position)"
    }
}
