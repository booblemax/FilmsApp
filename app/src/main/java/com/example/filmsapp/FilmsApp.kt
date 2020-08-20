package com.example.filmsapp

import android.app.Application
import com.example.filmsapp.data.di.dataModule
import com.example.filmsapp.domain.di.domainModule
import com.example.filmsapp.ui.base.common.networkinfo.NetworkStateHolder.registerConnectivityMonitor
import com.example.filmsapp.ui.di.commonModule
import com.example.filmsapp.ui.di.vmModule
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@ExperimentalCoroutinesApi
class FilmsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
        registerConnectivityMonitor()

        startKoin {
            androidContext(this@FilmsApp)

            modules(dataModule + domainModule + vmModule + commonModule)
        }
    }
}
