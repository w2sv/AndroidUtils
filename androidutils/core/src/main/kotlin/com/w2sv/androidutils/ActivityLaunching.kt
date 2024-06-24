@file:Suppress("unused")

package com.w2sv.androidutils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult

fun Context.openUrl(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
            .addCategory(Intent.CATEGORY_BROWSABLE)
    )
}

fun Context.openUrl(
    url: String,
    onActivityNotFoundException: (Context) -> Unit
) {
    try {
        openUrl(url)
    } catch (e: ActivityNotFoundException) {
        onActivityNotFoundException(this)
    }
}

fun Context.startActivity(
    intent: Intent,
    onActivityNotFoundException: (Context) -> Unit
) {
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

val ActivityResult.uris: List<Uri>?
    get() =
        data?.let { intent ->
            intent.clipData?.let { clipData ->
                (0 until clipData.itemCount).map { clipData.getItemAt(it).uri }
            }
        }