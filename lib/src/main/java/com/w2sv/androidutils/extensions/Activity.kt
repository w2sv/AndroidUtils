package com.w2sv.androidutils.extensions

import android.app.Activity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.hideSystemBars() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        hide(WindowInsetsCompat.Type.systemBars())
    }
}

fun Activity.showSystemBars() {
    WindowCompat.getInsetsController(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.systemBars())
    }
}