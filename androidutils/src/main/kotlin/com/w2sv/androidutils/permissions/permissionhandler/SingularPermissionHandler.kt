@file:Suppress("unused")

package com.w2sv.androidutils.permissions.permissionhandler

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.w2sv.androidutils.permissions.getPackagePermissions

abstract class SingularPermissionHandler(
    activity: ComponentActivity,
    permission: String,
    classKey: String
) : PermissionHandler<String, Boolean>(
    activity,
    permission,
    resultContract = ActivityResultContracts.RequestPermission(),
    registryKey = "$classKey.$permission"
) {

    override fun permissionGranted(): Boolean =
        !requiredByAndroidSdk || ActivityCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    override fun permissionRationalSuppressed(): Boolean =
        permissionPreviouslyRequested && !ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permission
        )

    override val requiredByAndroidSdk: Boolean = activity.getPackagePermissions()
        .contains(permission)

    override fun permissionNewlyGranted(activityResult: Boolean): Boolean =
        activityResult
}