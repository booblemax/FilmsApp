package com.example.filmsapp.ui.base.common.networkinfo

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

class NetworkStateImpl : NetworkState {

    override var isConnected: Boolean = false
        set(value) {
            field = value
            NetworkLiveState.notify(
                if (value) Event.ConnectivityAvailable
                else Event.ConnectivityLost
            )
        }

    override var network: Network? = null

    override var networkCapabilities: NetworkCapabilities? = null
        set(value) {
            val event = Event.NetworkCapabilitiesChanged(field)
            field = value
            NetworkLiveState.notify(event)
        }

    override var linkProperties: LinkProperties? = null
        set(value) {
            val event = Event.LinkPropertyChanged(field)
            field = value
            NetworkLiveState.notify(event)
        }
}
