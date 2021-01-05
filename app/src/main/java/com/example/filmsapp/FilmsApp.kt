package com.example.filmsapp

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.filmsapp.base.common.networkinfo.NetworkStateHolder.registerConnectivityMonitor
import com.example.filmsapp.di.commonModule
import com.example.filmsapp.di.vmModule
import com.example.filmsapp.navigation.navigationModule
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class FilmsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
        registerConnectivityMonitor()

        startKoin {
            androidContext(this@FilmsApp)

            modules(
                dataModule +
                        domainModule +
                        vmModule +
                        commonModule +
                        navigationModule
            )
        }
    }
}
