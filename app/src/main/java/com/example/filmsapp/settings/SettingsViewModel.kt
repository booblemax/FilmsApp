package com.example.filmsapp.settings

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.mvi.EmptyState
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import com.example.filmsapp.base.prefs.SPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val sharedPreferences: SPreferences
) : BaseViewModel<IState, Intention>(dispatcherProvider, EmptyState()) {

    fun getCurrentTheme(): Int = sharedPreferences.getCurrentTheme()

    fun saveChosenTheme(themeId: Int) {
        sharedPreferences.saveTheme(themeId)
    }
}
