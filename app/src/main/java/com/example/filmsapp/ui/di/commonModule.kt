package com.example.filmsapp.ui.di

import com.example.filmsapp.ui.splash.GoogleAccountManager
import com.google.android.gms.common.GoogleApiAvailability
import org.koin.dsl.module

val commonModule = module {

    single { GoogleAccountManager(get(), get(), GoogleApiAvailability.getInstance()) }
}
