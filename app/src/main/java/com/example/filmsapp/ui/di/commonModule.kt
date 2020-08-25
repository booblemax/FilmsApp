package com.example.filmsapp.ui.di

import com.example.filmsapp.ui.base.prefs.SPreferences
import org.koin.dsl.module

val commonModule = module {

    factory { SPreferences(get()) }
}
