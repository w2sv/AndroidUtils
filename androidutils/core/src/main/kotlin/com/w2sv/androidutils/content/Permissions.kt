@file:Suppress("unused")

package com.w2sv.androidutils.content

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * Returns whether this context has [permission].
 *
 * This reads more directly than comparing [ContextCompat.checkSelfPermission]
 * with [PackageManager.PERMISSION_GRANTED] at every call site.
 */
fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/**
 * Returns the permissions requested by this app package.
 *
 * This hides the package-name lookup and SDK-specific [PackageInfo] API needed
 * to inspect [PackageInfo.requestedPermissions].
 *
 * @see [PackageInfo.requestedPermissions]
 */
fun Context.getPackagePermissions(): Array<String>? =
    packageManager
        .getPackageInfoCompat(packageName)
        .requestedPermissions

/**
 * Returns [PackageInfo] with requested permissions included.
 *
 * This hides the Android 13 flag API split behind one call.
 */
fun PackageManager.getPackageInfoCompat(packageName: String): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(
            packageName,
            PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
        )
    } else {
        getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
    }
