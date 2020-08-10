package com.example.filmsapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.R
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.ui.base.BaseViewModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import timber.log.Timber

class SplashViewModel(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    private val _requestAuthorizationPermission = MutableLiveData<UserRecoverableAuthIOException>()
    val requestAuthorizationPermission: LiveData<UserRecoverableAuthIOException> get() = _requestAuthorizationPermission

    private val _displayGpsUnavailable = MutableLiveData<Int>()
    val displayGpsUnavailable: LiveData<Int> get() = _displayGpsUnavailable

    override fun handleException(exception: Throwable) {
        when (exception) {
            is GooglePlayServicesAvailabilityIOException ->
                _displayGpsUnavailable.value =
                    exception.connectionStatusCode
            is UserRecoverableAuthIOException -> _requestAuthorizationPermission.value = exception
            else -> {
                Timber.e(exception)
                postMessage(R.string.error)
            }
        }
    }
}
