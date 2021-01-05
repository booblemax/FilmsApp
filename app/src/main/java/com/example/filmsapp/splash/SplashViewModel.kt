package com.example.filmsapp.splash

import com.example.domain.dispatchers.DispatcherProvider
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseViewModel
import com.github.terrakok.cicerone.Router
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
class SplashViewModel(
    router: Router,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SplashState, SplashIntents>(router, dispatcherProvider, SplashState()) {

    override suspend fun processIntention(intent: SplashIntents) {
        super.processIntention(intent)
        when (intent) {
            SplashIntents.Loading -> reduce { it.copy(loading = true) }
            SplashIntents.Loaded -> reduce { it.copy(loading = false) }
            is SplashIntents.Exception -> reduce {
                it.copy(error = intent.exception, errorMessage = intent.errorMessage)
            }
        }
    }

    override fun handleException(exception: Throwable) {
        when (exception) {
            is GooglePlayServicesAvailabilityIOException, is UserRecoverableAuthIOException ->
                pushIntent(SplashIntents.Exception(exception))
            else -> {
                Timber.e(exception)
                pushIntent(SplashIntents.Exception(errorMessage = R.string.error))
            }
        }
    }
}
