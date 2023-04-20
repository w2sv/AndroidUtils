package com.w2sv.androidutils.notifying

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat

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