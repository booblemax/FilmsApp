package com.example.filmsapp.ui.settings

import com.example.filmsapp.data.prefs.SPreferences
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.ui.base.BaseViewModel

class SettingsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val sharedPreferences: SPreferences
) : BaseViewModel(dispatcherProvider) {

    fun getCurrentTheme(): Int = sharedPreferences.getCurrentTheme()

    fun saveChosenTheme(themeId: Int) {
        sharedPreferences.saveTheme(themeId)
    }
}
