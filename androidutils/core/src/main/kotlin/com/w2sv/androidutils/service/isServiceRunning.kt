package com.w2sv.androidutils.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context

/**
 * Checks whether a service of type [T], owned by the calling app, is currently running.
 *
 * Relies on [ActivityManager.getRunningServices], which reliably returns services
 * owned by the calling app, even on Android O (API 26) and above.
 *
 * **Note**: This does **not** detect services from other apps and should not be
 * used to determine system-wide service state.
 *
 * @throws SecurityException when trying to query a service's state that is not owned by the calling app.
 */
@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.isServiceRunning() =
    systemService<ActivityManager>()
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }
