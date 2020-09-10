package com.example.filmsapp.settings

import com.example.filmsapp.base.mvi.BaseIntent

sealed class SettingsIntents : BaseIntent() {

    object Initial : SettingsIntents()
    data class SaveTheme(val themeId: Int) : SettingsIntents()
    object Back : SettingsIntents()
}
