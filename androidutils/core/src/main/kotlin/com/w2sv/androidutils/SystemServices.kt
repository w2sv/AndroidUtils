@file:Suppress("unused")

package com.w2sv.androidutils

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.core.location.LocationManagerCompat

@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.isServiceRunning() =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }

fun LocationManager.isLocationEnabledCompat(): Boolean =
    LocationManagerCompat.isLocationEnabled(this)

fun Context.getLocationManager(): LocationManager =
    getSystemService(LocationManager::class.java)

fun Context.getWifiManager(): WifiManager =
    getSystemService(WifiManager::class.java)

fun Context.getNotificationManager(): NotificationManager =
    getSystemService(NotificationManager::class.java)

fun Context.getConnectivityManager(): ConnectivityManager =
    getSystemService(ConnectivityManager::class.java)
