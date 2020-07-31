package com.example.filmsapp.ui.details

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff

class GoogleAccountManager(private val context: Context) {

    private val credential: GoogleAccountCredential =
        GoogleAccountCredential.usingOAuth2(context, DetailsFragment.SCOPES)
            .setBackOff(ExponentialBackOff())

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
            DetailsFragment.REQUEST_GOOGLE_PLAY_SERVICES
        ).show()
    }

    fun requestOrSetupAccountName(onAccountNameApplied: () -> Unit) {
        val accountName =
            (context as Activity).getPreferences(Context.MODE_PRIVATE)
                ?.getString(DetailsFragment.PREF_ACCOUNT_NAME, null)
        if (accountName != null) {
            credential.selectedAccountName = accountName
            onAccountNameApplied()
        } else {
            ActivityCompat.startActivityForResult(
                context,
                credential.newChooseAccountIntent(),
                DetailsFragment.REQUEST_ACCOUNT_PICKER,
                null
            )
        }
    }
}
