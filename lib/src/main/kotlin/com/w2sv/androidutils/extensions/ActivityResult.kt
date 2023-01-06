@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import android.net.Uri
import androidx.activity.result.ActivityResult

val ActivityResult.uris: List<Uri>? get() =
    data?.let { intent ->
        intent.clipData?.let { clipData ->
            (0 until clipData.itemCount).map { clipData.getItemAt(it).uri }
        }
    }