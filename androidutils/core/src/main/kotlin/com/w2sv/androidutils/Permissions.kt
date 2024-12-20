@file:Suppress("unused")

package com.w2sv.androidutils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/**
 * @see [PackageInfo.requestedPermissions]
 */
fun Context.getPackagePermissions(): Array<String>? =
    packageManager
        .getPackageInfoCompat(packageName)
        .requestedPermissions

fun PackageManager.getPackageInfoCompat(packageName: String): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(
            packageName,
            PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
        )
    } else {
        getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
    }
