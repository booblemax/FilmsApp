package com.example.filmsapp.ui.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.R
import com.example.filmsapp.ui.base.prefs.SPreferences
import org.koin.android.ext.android.inject

class FilmsActivity : AppCompatActivity() {

    private val sharedPreferences: SPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getCurrentTheme())
    }
}
