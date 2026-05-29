package com.w2sv.androidutils.os

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/**
 * Whether Material dynamic colors are available on this device.
 *
 * This names the Android S SDK check and exposes it with
 * [ChecksSdkIntAtLeast] for lint/static analysis.
 */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val dynamicColorsSupported
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * Whether runtime notification permission is required on this device.
 *
 * This names the Android Tiramisu SDK check and exposes it with
 * [ChecksSdkIntAtLeast] for lint/static analysis.
 */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
val postNotificationsPermissionRequired: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

/**
 * Whether all-files external storage access requires runtime handling.
 *
 * This names the Android R SDK check and exposes it with
 * [ChecksSdkIntAtLeast] for lint/static analysis.
 */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
val manageExternalStoragePermissionRequired: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
