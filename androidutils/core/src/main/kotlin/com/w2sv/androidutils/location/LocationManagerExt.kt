package com.w2sv.androidutils.location

import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

/**
 * Returns whether location is enabled for this manager.
 *
 * This exposes [LocationManagerCompat.isLocationEnabled] as an extension so
 * call sites do not pass the receiver back into AndroidX.
 *
 * @see LocationManagerCompat.isLocationEnabled
 */
fun LocationManager.isLocationEnabledCompat(): Boolean =
    LocationManagerCompat.isLocationEnabled(this)
