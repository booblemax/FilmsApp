package com.example.filmsapp.splash

import com.example.filmsapp.base.mvi.IState

data class SplashState(
    val loading: Boolean = true,
    val error: Throwable? = null,
    val errorMessage: Int? = null
) : IState
