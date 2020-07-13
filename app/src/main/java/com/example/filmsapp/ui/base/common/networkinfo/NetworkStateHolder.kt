package com.example.filmsapp.ui.base.common.networkinfo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object NetworkStateHolder : NetworkState {

    private lateinit var holder: NetworkStateImpl

    override var isConnected: Boolean = false
        get() = holder.isConnected
    override var network: Network? = null
        get() = holder.network
    override var networkCapabilities: NetworkCapabilities? = null
        get() = holder.networkCapabilities
    override var linkProperties: LinkProperties? = null
        get() = holder.linkProperties

    fun Application.registerConnectivityMonitor() {
        holder = NetworkStateImpl()
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(NetworkRequest.Builder().build(), NetworkCallbackImpl(holder))
    }
}
