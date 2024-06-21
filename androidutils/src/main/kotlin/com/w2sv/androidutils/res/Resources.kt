@file:Suppress("unused")

package com.w2sv.androidutils.res

import android.content.res.Resources
import android.text.Spanned
import androidx.annotation.ArrayRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat

fun Resources.getLong(@IntegerRes id: Int): Long =
    getInteger(id).toLong()

fun Resources.getHtmlText(@StringRes id: Int, vararg formatArgs: Any): Spanned =
    HtmlCompat.fromHtml(
        getString(
            id,
            *formatArgs
        ),
        HtmlCompat.FROM_HTML_MODE_COMPACT
    )

fun Resources.getNestedStringArray(@ArrayRes id: Int, index: Int): List<String> =
    obtainTypedArray(id).run {
        getTextArray(index).map { it.toString() }
            .also { recycle() }
    }