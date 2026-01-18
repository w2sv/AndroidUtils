@file:Suppress("unused")

package com.w2sv.androidutils.service

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
    systemService<ActivityManager>()
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }

fun LocationManager.isLocationEnabledCompat(): Boolean =
    LocationManagerCompat.isLocationEnabled(this)

fun Context.getLocationManager(): LocationManager =
    systemService()

fun Context.getWifiManager(): WifiManager =
    systemService()

fun Context.getNotificationManager(): NotificationManager =
    systemService()

fun Context.getConnectivityManager(): ConnectivityManager =
    systemService()

/**
 * @return a system service of type [T].
 *
 * This is a type-safe, concise alternative to `getSystemService(Class)`, avoiding ::class.java shenanigans.
 *
 * Example:
 * ```
 * val statusBarManager = context.systemService<StatusBarManager>()
 * ```
 *
 * @see Context.getSystemService
 * @throws IllegalStateException if the service is not available on the device.
 */
inline fun <reified T> Context.systemService(): T =
    getSystemService(T::class.java) ?: error("System service ${T::class.java.simpleName} not available")
