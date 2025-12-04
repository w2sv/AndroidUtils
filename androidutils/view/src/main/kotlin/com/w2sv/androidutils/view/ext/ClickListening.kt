@file:Suppress("unused")

package com.w2sv.androidutils.view.ext

import android.view.View

/**
 * Sets a click listener that ignores rapid repeated taps.
 *
 * @param interval Minimum time in milliseconds that must pass between clicks
 *                 for the action to be invoked. Defaults to 500 ms.
 * @param onClick Callback invoked when a click is accepted (i.e., not debounced).
 */
fun View.setDebouncedOnClickListener(interval: Long = 500L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { v ->
        val now = System.currentTimeMillis()
        if (now - lastClickTime >= interval) {
            lastClickTime = now
            onClick(v)
        }
    }
}
