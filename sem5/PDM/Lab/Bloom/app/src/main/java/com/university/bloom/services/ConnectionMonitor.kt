package com.university.bloom.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

class ConnectionMonitor(
    @ApplicationContext appContext: Context
) {
    val isOnline: Flow<Boolean> = callbackFlow {
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                channel.trySend(false)
            }
        }
        val connectivityManager = appContext.getSystemService<ConnectivityManager>()
        connectivityManager?.registerNetworkCallback(
            Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            callback
        )
        channel.trySend(connectivityManager.isCurrentlyConnected())
        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }.conflate()


}

@Suppress("DEPRECATION")
private fun ConnectivityManager?.isCurrentlyConnected(): Boolean =
    when (this) {
        null -> false
        else -> run {
            activeNetwork
                ?.let(::getNetworkCapabilities)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }
    }