@file:Suppress("unused")

package com.w2sv.androidutils.generic

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    makeToast(text, duration)
        .show()
}

fun Context.showToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT) {
    makeToast(text, duration)
        .show()
}

fun Context.makeToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT): Toast =
    makeToast(resources.getText(text), duration)

fun Context.makeToast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast
        .makeText(this, text, duration)
