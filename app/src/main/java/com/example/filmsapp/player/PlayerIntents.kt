package com.example.filmsapp.player

import com.example.filmsapp.base.mvi.Intention

sealed class PlayerIntents : Intention {

    data class Initial(val videoId: String) : PlayerIntents()
}
