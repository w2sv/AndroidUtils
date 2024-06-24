@file:Suppress("unused")

package com.w2sv.androidutils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder

fun Uri.hasPermission(
    context: Context,
    permissionCode: Int,
    readPermission: String? = null,
    writePermission: String? = null
): Boolean =
    context.checkUriPermission(
        this,
        readPermission,
        writePermission,
        Binder.getCallingPid(),
        Binder.getCallingUid(),
        permissionCode
    ) == PackageManager.PERMISSION_GRANTED