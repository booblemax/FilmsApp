package com.example.filmsapp.data.prefs

import androidx.appcompat.app.AppCompatDelegate
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.filmsapp.FilmsTestApp
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(application = FilmsTestApp::class, sdk = [28])
@RunWith(AndroidJUnit4::class)
class SPreferencesTest {

    private val prefs: SPreferences = SPreferences(ApplicationProvider.getApplicationContext())

    @Test
    fun `given mode night when save theme should return night`() {
        val savingTheme = AppCompatDelegate.MODE_NIGHT_YES

        prefs.saveTheme(savingTheme)

        assertThat(prefs.getCurrentTheme(), `is`(AppCompatDelegate.MODE_NIGHT_YES))
    }

    @Test
    fun `when get theme should return default light theme`() {
        val theme = prefs.getCurrentTheme()

        assertThat(theme, `is`(AppCompatDelegate.MODE_NIGHT_NO))
    }

    @Test
    fun `when save theme and clear should return default light theme`() {
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
