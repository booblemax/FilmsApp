package com.example.filmsapp.base.common.networkinfo

import android.net.LinkProperties
import android.net.NetworkCapabilities

sealed class Event {

    val networkState: NetworkState = NetworkStateHolder

    object ConnectivityLost : Event()
    object ConnectivityAvailable : Event()
    data class NetworkCapabilitiesChanged(val old: NetworkCapabilities?) : Event()
    data class LinkPropertyChanged(val old: LinkProperties?) : Event()
}
