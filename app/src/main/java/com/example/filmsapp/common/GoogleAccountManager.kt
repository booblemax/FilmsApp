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
        val SCOPES = listOf(YouTubeScopes.YOUTUBE_READONLY)
    }
}
