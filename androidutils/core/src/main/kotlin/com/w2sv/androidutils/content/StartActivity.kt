@file:Suppress("unused")

package com.w2sv.androidutils.content

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri
import com.w2sv.androidutils.core.R
import com.w2sv.androidutils.widget.showToast

fun Context.openUrl(
    url: String,
    onActivityNotFoundException: (Context) -> Unit = {
        showToast(
            R.string.on_activity_not_found_exception_message,
            Toast.LENGTH_LONG
        )
    }
) {
    try {
        startActivity(
            Intent(Intent.ACTION_VIEW, url.toUri())
                .addCategory(Intent.CATEGORY_BROWSABLE)
        )
    } catch (e: ActivityNotFoundException) {
        onActivityNotFoundException(this)
    }
}

fun Context.startActivity(intent: Intent, onActivityNotFoundException: (Context) -> Unit) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onActivityNotFoundException(this)
    }
}

fun Context.openAppSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(
                Uri.fromParts(
                    "package",
                    packageName,
                    null
                )
            )
    )
}
