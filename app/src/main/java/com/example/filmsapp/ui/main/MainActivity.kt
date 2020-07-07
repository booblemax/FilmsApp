package com.example.filmsapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.R
import com.example.filmsapp.data.prefs.SPreferences
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val sharedPreferences: SPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getCurrentTheme())
    }
}
