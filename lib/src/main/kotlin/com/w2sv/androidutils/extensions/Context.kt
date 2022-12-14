@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Binder
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getColoredIcon(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable =
    DrawableCompat.wrap(AppCompatResources.getDrawable(this, drawableId)!!).apply {
        setColor(this@getColoredIcon, colorId)
    }

fun Context.getThemedColor(@ColorRes id: Int): Int =
    resources.getColor(id, theme)

fun Context.uriPermissionGranted(
    uri: Uri,
    permissionCode: Int,
    readPermission: String? = null,
    writePermission: String? = null
): Boolean =
    checkUriPermission(
        uri,
        readPermission,
        writePermission,
        Binder.getCallingPid(),
        Binder.getCallingUid(),
        permissionCode
    ) == PackageManager.PERMISSION_GRANTED

tailrec fun Context.getActivity(): Activity? =
    this as? Activity ?: (this as? ContextWrapper)?.baseContext?.getActivity()

@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.serviceRunning() =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }

fun Context.goToWebpage(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
    )
}

fun Context.notificationManager(): NotificationManager =
    getSystemService(NotificationManager::class.java)

fun Context.showNotification(id: Int, builder: NotificationCompat.Builder) {
    notificationManager()
        .notify(
            id,
            builder.build()
        )
}