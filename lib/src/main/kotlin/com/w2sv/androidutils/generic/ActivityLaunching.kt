package com.w2sv.androidutils.generic

import android.content.Context
import android.content.Intent
import android.net.Uri
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

val ActivityResult.uris: List<Uri>?
    get() =
        data?.let { intent ->
            intent.clipData?.let { clipData ->
                (0 until clipData.itemCount).map { clipData.getItemAt(it).uri }
            }
        }