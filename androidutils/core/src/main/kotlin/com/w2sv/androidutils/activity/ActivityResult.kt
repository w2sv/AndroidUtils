package com.w2sv.androidutils.activity

import android.content.ClipData
import android.net.Uri
import androidx.activity.result.ActivityResult

/**
 * Returns all [ClipData.Item]s from [ActivityResult.data], or null when the
 * result has no intent or no clip data.
 */
val ActivityResult.clipDataItems: List<ClipData.Item>?
    get() = data?.let { intent ->
        intent.clipData?.let { clipData ->
            (0 until clipData.itemCount).map { clipData.getItemAt(it) }
        }
    }

/**
 * Returns the [Uri] of each item in [clipDataItems], or null when the result
 * has no intent or no clip data.
 *
 * Items without a URI are represented as null entries because [ClipData.Item.uri]
 * is nullable.
 */
val ActivityResult.clipDataItemUris: List<Uri>?
    get() = clipDataItems?.map { it.uri }
