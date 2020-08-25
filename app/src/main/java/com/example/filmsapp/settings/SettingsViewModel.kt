package com.example.filmsapp.settings

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.prefs.SPreferences

class SettingsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val sharedPreferences: SPreferences
) : BaseViewModel(dispatcherProvider) {

    fun getCurrentTheme(): Int = sharedPreferences.getCurrentTheme()

    fun saveChosenTheme(themeId: Int) {
        sharedPreferences.saveTheme(themeId)
    }
}
