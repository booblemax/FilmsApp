package com.example.filmsapp.ui.base.prefs

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class SPreferences(private val context: Context) {

    fun getCurrentTheme(): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(THEME_NAME, AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun saveTheme(themeId: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            putInt(THEME_NAME, themeId)
        }
    }

    fun clearPrefs() {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
            clear()
        }
    }

    companion object {
        private const val PREF_NAME = "films_prefs"
        private const val THEME_NAME = "theme"
    }
}
