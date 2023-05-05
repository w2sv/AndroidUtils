@file:Suppress("unused")

package com.w2sv.androidutils.generic

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.w2sv.androidutils.R
import com.w2sv.androidutils.notifying.showToast

fun Context.openUrl(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
            .addCategory(Intent.CATEGORY_BROWSABLE)
    )
}

fun Context.openUrlWithActivityNotFoundHandling(
    url: String,
    onActivityNotFoundException: (Context) -> Unit = {
        showToast(
            getString(R.string.couldn_t_find_a_browser_to_open_the_url_with),
            Toast.LENGTH_LONG
        )
    }
) {
    try {
        openUrl(url)
    } catch (e: ActivityNotFoundException) {
        onActivityNotFoundException(this)
    }
}

val ActivityResult.uris: List<Uri>?
    get() =
        data?.let { intent ->
            intent.clipData?.let { clipData ->
                (0 until clipData.itemCount).map { clipData.getItemAt(it).uri }
            }
        }