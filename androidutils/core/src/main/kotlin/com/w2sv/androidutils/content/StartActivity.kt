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

/**
 * Opens [url] in a browser-capable activity.
 *
 * This builds the browsable view intent and centralizes
 * [ActivityNotFoundException] handling.
 */
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

/**
 * Starts [intent] with fallback handling when no activity can handle it.
 *
 * This keeps call sites from wrapping [Context.startActivity] in repeated
 * [ActivityNotFoundException] try/catch blocks.
 */
fun Context.startActivity(intent: Intent, onActivityNotFoundException: (Context) -> Unit) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        onActivityNotFoundException(this)
    }
}

/**
 * Opens this app's system settings screen.
 *
 * This builds the package-specific [Settings.ACTION_APPLICATION_DETAILS_SETTINGS]
 * intent for you.
 */
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
