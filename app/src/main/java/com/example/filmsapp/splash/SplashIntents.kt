package com.example.filmsapp.splash

import com.example.filmsapp.base.mvi.BaseIntent

sealed class SplashIntents : BaseIntent() {
    object Loading : SplashIntents()
    object Loaded : SplashIntents()
    data class Exception(
        val exception: Throwable? = null,
        val errorMessage: Int? = null
    ) : SplashIntents()
}
