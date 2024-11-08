@file:Suppress("unused")

package com.w2sv.androidutils.res

import android.content.res.Resources
import androidx.annotation.ArrayRes
import androidx.annotation.IntRange
import androidx.annotation.IntegerRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat

fun Resources.getLong(@IntegerRes id: Int): Long =
    getInteger(id).toLong()

/**
 * Create a formatted CharSequence from a string resource containing arguments and HTML formatting.
 *
 * The string resource must be wrapped in a CDATA section so that the HTML formatting is conserved.
 *
 * Example of an HTML formatted string resource:
 * <string name="html_formatted"><![CDATA[ bold text: <B>%1$s</B> ]]></string>
 *
 * Taken from https://stackoverflow.com/a/56944152/12083276
 */
fun Resources.getText(@StringRes id: Int, vararg args: Any?): CharSequence =
    HtmlCompat.fromHtml(getString(id, *args), HtmlCompat.FROM_HTML_MODE_COMPACT)

/**
 * Create a formatted CharSequence from a plurals resource containing arguments and HTML formatting.
 *
 * The plurals resource must be wrapped in a CDATA section so that the HTML formatting is conserved.
 *
 * Example of an HTML formatted plurals resource:
 * <plurals name="html_formatted">
 *         <item quantity="one"><![CDATA[bold text: <B>%1$s</B>]]></item>
 *         <item quantity="other"><![CDATA[bold texts: <B>%1$s</B>]]></item>
 *     </plurals>
 */
fun Resources.getQuantityText(
    @PluralsRes id: Int,
    @IntRange(from = 0) quantity: Int,
    vararg args: Any?
): CharSequence =
    HtmlCompat.fromHtml(getQuantityString(id, quantity, *args), HtmlCompat.FROM_HTML_MODE_COMPACT)

fun Resources.getNestedStringArray(@ArrayRes id: Int, index: Int): List<String> =
    obtainTypedArray(id).run {
        getTextArray(index).map { it.toString() }
            .also { recycle() }
    }
