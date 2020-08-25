package com.example.filmsapp.ui.settings

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.prefs.SPreferences

class SettingsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val sharedPreferences: SPreferences
) : BaseViewModel(dispatcherProvider) {

    fun getCurrentTheme(): Int = sharedPreferences.getCurrentTheme()

    fun saveChosenTheme(themeId: Int) {
        sharedPreferences.saveTheme(themeId)
    }
}
