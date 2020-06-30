package com.example.filmsapp.ui.base.common.networkinfo

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

internal class NetworkCallbackImpl(private val state: NetworkStateImpl) : ConnectivityManager.NetworkCallback() {

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        state.networkCapabilities = networkCapabilities
    }

    override fun onLost(network: Network) {
        state.network = network
        state.isConnected = false
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        state.linkProperties = linkProperties
    }

    override fun onAvailable(network: Network) {
        state.network = network
        state.isConnected = true
    }
}