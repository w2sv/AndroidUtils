@file:Suppress("unused")

package com.w2sv.androidutils.view

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import androidx.annotation.Px

/**
 * Expands this view's touch target on all sides by [all] pixels.
 */
fun View.increaseTouchArea(@Px all: Int) {
    increaseTouchArea(all, all, all, all)
}

/**
 * Expands this view's touch target by adjusting the hit rect.
 * Call after the view is attached; runs via `post` to ensure layout.
 */
fun View.increaseTouchArea(
    @Px left: Int = 0,
    @Px top: Int = 0,
    @Px right: Int = 0,
    @Px bottom: Int = 0
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
