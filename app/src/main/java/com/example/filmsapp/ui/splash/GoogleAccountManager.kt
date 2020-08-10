package com.example.filmsapp.ui.splash

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes

class GoogleAccountManager(private val context: Context) {

    private val credential: GoogleAccountCredential =
        GoogleAccountCredential.usingOAuth2(context, SCOPES).setBackOff(ExponentialBackOff())

    fun getCredential() = credential

    fun setAccountName(name: String) {
        credential.selectedAccountName = name
    }

    fun hasAccountName() = credential.selectedAccountName != null

    fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        apiAvailability.getErrorDialog(
            context as Activity,
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        ).show()
    }

    fun requestOrSetupAccountName(onAccountNameApplied: () -> Unit) {
        val accountName =
            (context as Activity).getPreferences(Context.MODE_PRIVATE)
                ?.getString(PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            credential.selectedAccountName = accountName
            onAccountNameApplied()
        } else {
            ActivityCompat.startActivityForResult(
                context,
                credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER,
                null
            )
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
