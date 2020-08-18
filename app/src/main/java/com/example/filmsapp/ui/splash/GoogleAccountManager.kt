package com.example.filmsapp.ui.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes

class GoogleAccountManager(
    private val context: Context,
    private val apiAvailability: GoogleApiAvailability
) {

    private val credential: GoogleAccountCredential =
        GoogleAccountCredential.usingOAuth2(context, SCOPES).setBackOff(ExponentialBackOff())

    fun getCredential() = credential

    fun setAccountName(name: String) {
        credential.selectedAccountName = name
    }

    fun hasAccountName() = credential.selectedAccountName != null

    fun isGooglePlayServicesAvailable(): Boolean {
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    fun acquireGooglePlayServices() {
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        apiAvailability.getErrorDialog(
            context as Activity,
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        ).show()
    }

    fun requestOrSetupAccountName(onSuccess: () -> Unit, onError: (Intent) -> Unit) {
        val accountName =
            (context as AppCompatActivity).getPreferences(Context.MODE_PRIVATE)
                ?.getString(PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            credential.selectedAccountName = accountName
            onSuccess()
        } else {
            onError(credential.newChooseAccountIntent())
        }
    }

    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        const val PREF_ACCOUNT_NAME = "accountName"
        val SCOPES = listOf(YouTubeScopes.YOUTUBE_READONLY)
    }
}
