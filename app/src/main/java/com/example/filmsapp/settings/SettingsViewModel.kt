package com.example.filmsapp.settings

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.base.BaseViewModel
import com.example.filmsapp.base.Event
import com.example.filmsapp.base.prefs.SPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsViewModel(
    dispatcherProvider: DispatcherProvider,
    private val sharedPreferences: SPreferences
) : BaseViewModel<SettingsState, SettingsIntents>(dispatcherProvider, SettingsState()) {

    override suspend fun processIntention(intent: SettingsIntents) {
        super.processIntention(intent)
        when (intent) {
            is SettingsIntents.Initial -> reduce {
                it.copy(
                    currentTheme = getCurrentTheme()
                )
            }
            is SettingsIntents.SaveTheme -> reduce {
                saveChosenTheme(intent.themeId)
                it.copy(
                    currentTheme = intent.themeId,
                    uiEvent = Event(SettingsUiEvent.ChangeTheme(intent.themeId))
                )
            }
            is SettingsIntents.Back -> reduce {
                it.copy(uiEvent = Event(SettingsUiEvent.Back))
            }
        }
    }

    private fun getCurrentTheme(): Int = sharedPreferences.getCurrentTheme()

    private fun saveChosenTheme(themeId: Int) {
        sharedPreferences.saveTheme(themeId)
    }
}
