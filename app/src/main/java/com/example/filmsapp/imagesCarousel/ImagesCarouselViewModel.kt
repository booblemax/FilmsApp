package com.example.filmsapp.imagesCarousel

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ImagesCarouselViewModel(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<ImageCarouselState, ImageCarouselIntents>(
        dispatcherProvider,
        ImageCarouselState()
    ) {

    override suspend fun processIntention(intent: ImageCarouselIntents) {
        super.processIntention(intent)
        when (intent) {
            is ImageCarouselIntents.Initial -> reduce {
                it.copy(
                    urls = intent.urls,
                    position = intent.position
                )
            }
        }
    }
}
