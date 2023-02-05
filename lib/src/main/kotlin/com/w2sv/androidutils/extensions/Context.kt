@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.location.LocationManagerCompat

/**
 * Toast
 */

fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    makeToast(text, duration)
        .show()
}

fun Context.showToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT) {
    makeToast(text, duration)
}

fun Context.makeToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast
        .makeText(this, text, duration)

fun Context.makeToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT): Toast =
    makeToast(resources.getText(text), duration)

/**
 * Drawable
 */

fun Context.getColoredDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable =
    DrawableCompat.wrap(AppCompatResources.getDrawable(this, drawableId)!!).apply {
        setColor(this@getColoredDrawable, colorId)
    }

/**
 * SharedPreferences
 */

fun Context.getDefaultPreferences(): SharedPreferences =
    getSharedPreferences(packageName, Context.MODE_PRIVATE)

/**
 * Activity launching
 */

fun Context.openUrl(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
    )
}

/**
 * Activity retrieval
 */

val Context.activity: Activity? get() = _getActivity()

fun Context.requireActivity(): Activity = activity!!

@Suppress("UNCHECKED_CAST")
fun <A : Activity> Context.requireCastActivity(): A =
    requireActivity() as A

private tailrec fun Context._getActivity(): Activity? =
    this as? Activity ?: (this as? ContextWrapper)?.baseContext?._getActivity()

/**
 * Services
 */

@Suppress("DEPRECATION")
inline fun <reified T : Service> Context.serviceRunning() =
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it.service.className == T::class.java.name }

/**
 * System Services
 */

val Context.locationServicesEnabled: Boolean
    get() = LocationManagerCompat.isLocationEnabled(getSystemService(LocationManager::class.java))

/**
 * .Notifications
 */

fun Context.getNotificationManager(): NotificationManager =
    getSystemService(NotificationManager::class.java)

fun Context.showNotification(id: Int, builder: NotificationCompat.Builder) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
        throw SecurityException("${Manifest.permission.POST_NOTIFICATIONS} required for SDK >= 33")

    getNotificationManager()
        .notify(
            id,
            builder.build()
        )
}

/**
 * Misc
 */

val Context.playStoreUrl: String
    get() = "https://play.google.com/store/apps/details?id=$packageName"