package com.example.filmsapp.common

import android.content.Context
import android.content.Intent
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes

class GoogleAccountManager(context: Context) {

    private val credential: GoogleAccountCredential =
        GoogleAccountCredential.usingOAuth2(context, SCOPES)
            .setBackOff(ExponentialBackOff())

    fun getCredential() = credential

    fun setAccountName(name: String) {
        credential.selectedAccountName = name
    }

    fun hasAccountName() = credential.selectedAccountName != null

    fun getChooseAccountIntent(): Intent = credential.newChooseAccountIntent()

    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        val SCOPES = listOf(YouTubeScopes.YOUTUBE_READONLY)
    }
}
