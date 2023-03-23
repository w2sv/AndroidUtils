package com.w2sv.permissionhandler

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

internal fun Context.getPackageUsedPermissions(): Set<String> =
    packageManager.getPackageInfoCompat(packageName)
        .requestedPermissions
        .toSet()

private fun PackageManager.getPackageInfoCompat(packageName: String): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getPackageInfo(
            packageName,
            PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
        )
    else
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)