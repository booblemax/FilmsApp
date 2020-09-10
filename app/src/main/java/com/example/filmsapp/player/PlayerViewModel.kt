package com.example.filmsapp.player

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PlayerViewModel(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<PlayerState, PlayerIntents>(dispatcherProvider, PlayerState()) {

    var lastStoppedTime: Float = 0f

    override suspend fun processIntention(intent: PlayerIntents) {
        super.processIntention(intent)
        when (intent) {
            is PlayerIntents.Initial -> reduce { it.copy(videoId = intent.videoId) }
        }
    }
}
