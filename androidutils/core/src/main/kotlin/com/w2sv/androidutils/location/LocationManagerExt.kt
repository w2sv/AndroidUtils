package com.w2sv.androidutils.location

import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

/**
 * Shortcut for
 * ```
 * LocationManagerCompat.isLocationEnabled(locationManager)
 * ```
 *
 * @see LocationManagerCompat.isLocationEnabled
 */
fun LocationManager.isLocationEnabledCompat(): Boolean =
    LocationManagerCompat.isLocationEnabled(this)
