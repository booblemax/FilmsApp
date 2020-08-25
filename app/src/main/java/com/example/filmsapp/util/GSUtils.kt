package com.example.filmsapp.util

import android.app.Activity
import android.content.Context
import com.example.filmsapp.details.DetailsFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object GSUtils {

    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    fun acquireGooglePlayServices(context: Context) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(context, connectionStatusCode)
        }
    }

    fun showGooglePlayServicesAvailabilityErrorDialog(context: Context, connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        apiAvailability.getErrorDialog(
            context as Activity,
            connectionStatusCode,
            DetailsFragment.REQUEST_GOOGLE_PLAY_SERVICES
        ).show()
    }
}
