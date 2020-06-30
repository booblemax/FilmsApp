package com.example.filmsapp.ui.base.common.networkinfo

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities

interface NetworkState {
    var isConnected: Boolean
    var network: Network?
    var networkCapabilities: NetworkCapabilities?
    var linkProperties: LinkProperties?
}