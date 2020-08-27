package com.example.filmsapp.di

import com.example.filmsapp.base.prefs.SPreferences
import com.example.filmsapp.common.GoogleAccountManager
import org.koin.dsl.module

val commonModule = module {

    factory { SPreferences(get()) }

    single { GoogleAccountManager(get()) }
}
