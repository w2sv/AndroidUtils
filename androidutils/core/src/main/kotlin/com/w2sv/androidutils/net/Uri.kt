@file:Suppress("unused")

package com.w2sv.androidutils.net

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder

fun Uri.hasPermission(
    context: Context,
    permissionCode: Int,
    readPermission: String? = null,
    writePermission: String? = null,
    pid: Int = Binder.getCallingPid(),
    uid: Int = Binder.getCallingUid()
): Boolean =
    context.checkUriPermission(
        this,
        readPermission,
        writePermission,
        pid,
        uid,
        permissionCode
    ) == PackageManager.PERMISSION_GRANTED
