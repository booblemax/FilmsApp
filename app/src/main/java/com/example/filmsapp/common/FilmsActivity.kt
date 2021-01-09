package com.example.filmsapp.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.R
import com.example.filmsapp.base.prefs.SPreferences
import com.example.filmsapp.navigation.FilmScreen.SplashScreen
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Replace
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject

class FilmsActivity : AppCompatActivity() {

    private val sharedPreferences: SPreferences by inject()

    private val navigatorHolder: NavigatorHolder by inject()

    private val navigator: Navigator by lazy { AppNavigator(this, R.id.fragment_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getCurrentTheme())
        navigator.applyCommands(arrayOf(Replace(SplashScreen())))
    }

    override fun onResumeFragments() {
        navigatorHolder.setNavigator(navigator)
        super.onResume()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
