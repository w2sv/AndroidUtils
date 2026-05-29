@file:Suppress("unused")

package com.w2sv.androidutils.view

import android.view.View
import androidx.annotation.MainThread

/**
 * Sets this view to [View.VISIBLE].
 *
 * This is the concise counterpart to assigning [View.setVisibility] manually.
 */
@MainThread
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Sets this view to [View.GONE].
 *
 * This names the "gone" visibility state and avoids repeating the constant.
 */
@MainThread
fun View.remove() {
    visibility = View.GONE
}

/**
 * Sets this view to [View.INVISIBLE].
 *
 * This names the invisible-but-still-laid-out state and avoids repeating the
 * constant.
 */
@MainThread
fun View.hide() {
    visibility = View.INVISIBLE
}

/**
 * Hides [hideView] and shows [showView].
 *
 * This combines two visibility assignments for simple view swaps.
 */
@MainThread
fun crossVisualize(hideView: View, showView: View) {
    hideView.hide()
    showView.show()
}

/**
 * Hides [hideViews] and shows [showViews].
 *
 * This combines multiple visibility assignments for grouped view swaps.
 */
@MainThread
fun crossVisualize(hideViews: Iterable<View>, showViews: Iterable<View>) {
    hideViews.forEach { it.hide() }
    showViews.forEach { it.show() }
}
