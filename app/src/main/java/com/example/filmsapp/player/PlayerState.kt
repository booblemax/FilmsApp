package com.example.filmsapp.player

import com.example.filmsapp.base.mvi.IState

data class PlayerState(
    val videoId: String? = null
) : IState
