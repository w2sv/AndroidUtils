@file:Suppress("unused")

package com.w2sv.androidutils.ui

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.hideSystemBars(systemBarsBehavior: Int = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE) {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        this.systemBarsBehavior = systemBarsBehavior
        hide(WindowInsetsCompat.Type.systemBars())
    }
}

fun Activity.showSystemBars() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.systemBars())
    }
}