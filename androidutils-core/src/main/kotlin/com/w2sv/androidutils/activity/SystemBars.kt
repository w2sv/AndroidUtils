@file:Suppress("unused")

package com.w2sv.androidutils.activity

import android.app.Activity
import androidx.annotation.MainThread
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Hides system bars for this [Activity].
 *
 * This wraps the AndroidX window insets controller setup required before
 * calling `hide(systemBars())`.
 */
@MainThread
fun Activity.hideSystemBars(systemBarsBehavior: Int = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE) {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        this.systemBarsBehavior = systemBarsBehavior
        hide(WindowInsetsCompat.Type.systemBars())
    }
}

/**
 * Shows system bars for this [Activity].
 *
 * This wraps the AndroidX window insets controller lookup required before
 * calling `show(systemBars())`.
 */
@MainThread
fun Activity.showSystemBars() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.systemBars())
    }
}
