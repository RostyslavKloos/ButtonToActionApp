package ua.rodev.buttontoactionapp.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest.Builder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

interface NetworkMonitor {

    fun isOnlineFlow(): Flow<Boolean>
    fun isOnline(): Boolean

    class Main(context: Context) : NetworkMonitor {

        private val connectivityManager = context.getSystemService<ConnectivityManager>()

        override fun isOnlineFlow(): Flow<Boolean> = callbackFlow {

            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    channel.trySend(true)
                }

                override fun onLost(network: Network) {
                    channel.trySend(false)
                }
            }

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
        }
            .conflate()

        override fun isOnline(): Boolean = connectivityManager.isCurrentlyConnected()

        @Suppress("DEPRECATION")
        private fun ConnectivityManager?.isCurrentlyConnected() = when (this) {
            null -> false
            else -> when {
                VERSION.SDK_INT >= VERSION_CODES.M ->
                    activeNetwork
                        ?.let(::getNetworkCapabilities)
                        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        ?: false
                else -> activeNetworkInfo?.isConnected ?: false
            }
        }
    }
}