@file:Suppress("unused")

package com.w2sv.androidutils.widget

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Shows a short-lived toast with [text].
 *
 * This combines [Toast.makeText] and [Toast.show] for the common fire-and-forget
 * case.
 */
fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    makeToast(text, duration)
        .show()
}

/**
 * Shows a short-lived toast from a string resource.
 *
 * This combines string-resource lookup, [Toast.makeText], and [Toast.show].
 */
fun Context.showToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT) {
    makeToast(text, duration)
        .show()
}

/**
 * Creates a [Toast] from a string resource.
 *
 * This resolves the resource before delegating to [Toast.makeText], while still
 * returning the toast for further customization.
 */
fun Context.makeToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT): Toast =
    makeToast(resources.getText(text), duration)

/**
 * Creates a [Toast] from [text].
 *
 * This keeps toast construction concise while returning the toast when callers
 * need to configure it before showing.
 */
fun Context.makeToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(this, text, duration)
