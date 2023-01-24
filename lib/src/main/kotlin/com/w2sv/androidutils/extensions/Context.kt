@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Toast
 */

fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    getToast(text, duration)
        .show()
}

fun Context.getToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast
        .makeText(this, text, duration)

/**
 * Notifications
 */

fun Context.notificationManager(): NotificationManager =
    getSystemService(NotificationManager::class.java)

fun Context.showNotification(id: Int, builder: NotificationCompat.Builder) {
    notificationManager()
        .notify(
            id,
            builder.build()
        )
}

/**
 * Resources
 */

fun Context.getColoredIcon(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable =
    DrawableCompat.wrap(AppCompatResources.getDrawable(this, drawableId)!!).apply {
        setColor(this@getColoredIcon, colorId)
    }

/**
 * Activity launching
 */

fun Context.goToWebpage(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
    )
}

/**
 * Attribute retrieval
 */

tailrec fun Context.getActivity(): Activity? =
    this as? Activity ?: (this as? ContextWrapper)?.baseContext?.getActivity()

/**
 * Services
 */

@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.serviceRunning() =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }