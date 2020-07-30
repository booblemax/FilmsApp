package com.example.filmsapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

class FilmsTestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FilmsTestApp)
            modules(emptyList())
        }
    }

    internal fun loadModules(modules: List<Module>, block: () -> Unit) {
        loadKoinModules(modules)
        block()
        unloadKoinModules(modules)
    }

    override fun onTerminate() {
        super.onTerminate()

        stopKoin()
    }
}
