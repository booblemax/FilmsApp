package com.example.filmsapp.data.prefs

import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SPreferencesTest {

    private val prefs: SPreferences =SPreferences(ApplicationProvider.getApplicationContext())

    @Test
    fun `save theme night return night`() {
        val savingTheme = AppCompatDelegate.MODE_NIGHT_YES

        prefs.saveTheme(savingTheme)

        assertThat(prefs.getCurrentTheme(), `is`(AppCompatDelegate.MODE_NIGHT_YES))
    }

    @Test
    fun `get theme return default light`() {
        val theme = prefs.getCurrentTheme()

        assertThat(theme, `is`(AppCompatDelegate.MODE_NIGHT_NO))
    }

    @Test
    fun `save theme and clear return default light`() {
        val savingTheme = AppCompatDelegate.MODE_NIGHT_YES

        prefs.saveTheme(savingTheme)
        prefs.clearPrefs()

        assertThat(prefs.getCurrentTheme(), `is`(AppCompatDelegate.MODE_NIGHT_NO))
    }

    @After
    fun tearDown() {
        prefs.clearPrefs()
    }
}