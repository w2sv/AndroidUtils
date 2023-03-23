@file:Suppress("unused")

package com.w2sv.permissionhandler

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

abstract class CoupledPermissionsHandler(
    activity: ComponentActivity,
    permissions: Array<String>,
    classKey: String
) : PermissionHandler<Array<String>, Map<String, Boolean>>(
    activity,
    permissions,
    resultContract = ActivityResultContracts.RequestMultiplePermissions(),
    registryKey = "$classKey.${permissions.toList()}"
) {

    override fun permissionGranted(): Boolean = !requiredByAndroidSdk || permission.all {
        ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun permissionRationalSuppressed(): Boolean =
        permissionPreviouslyRequested && permission.all {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

    override val requiredByAndroidSdk: Boolean =
        activity.getPackageUsedPermissions().let { requestedPermissions ->
            permissions.any { requestedPermissions.contains(it) }
        }

    override fun permissionNewlyGranted(activityResult: Map<String, Boolean>): Boolean =
        activityResult.values.all { it }
}