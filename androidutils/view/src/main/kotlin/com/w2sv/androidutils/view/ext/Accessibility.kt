@file:Suppress("unused")

package com.w2sv.androidutils.view.ext

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

/**
 * Expands this view's touch target on all sides by [all] pixels.
 */
fun View.increaseTouchArea(all: Int) {
    increaseTouchArea(all, all, all, all)
}

/**
 * Expands this view's touch target by adjusting the hit rect.
 * Call after the view is attached; runs via `post` to ensure layout.
 */
fun View.increaseTouchArea(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
) {
    post {
        val parentView = parent as? View ?: return@post

        val rect = Rect().also { getHitRect(it) }
        rect.set(
            rect.left - left,
            rect.top - top,
            rect.right + right,
            rect.bottom + bottom
        )

        parentView.touchDelegate = TouchDelegate(rect, this)
    }
}
