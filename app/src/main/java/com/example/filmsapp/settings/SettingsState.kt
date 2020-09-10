package com.example.filmsapp.settings

import com.example.filmsapp.base.Event
import com.example.filmsapp.base.mvi.IState

data class SettingsState(
    val currentTheme: Int? = null,
    val uiEvent: Event<SettingsUiEvent>? = null
) : IState {

    override fun toString(): String {
        return "SettingsState(currentTheme=$currentTheme, uiEvent=${uiEvent})"
    }
}

sealed class SettingsUiEvent {

    data class ChangeTheme(val themeId: Int) : SettingsUiEvent()
    object Back : SettingsUiEvent()
}
