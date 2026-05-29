@file:Suppress("unused")

package com.w2sv.androidutils.appwidget

import android.view.View
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.IdRes

/**
 * Sets a remote view to [visibility], defaulting to [View.GONE].
 *
 * This gives [RemoteViews.setViewVisibility] the same concise shape as regular
 * view visibility helpers.
 */
fun RemoteViews.hide(@IdRes view: Int, visibility: Int = View.GONE) {
    setViewVisibility(view, visibility)
}

/**
 * Sets a remote view to [View.VISIBLE].
 *
 * This avoids repeating [RemoteViews.setViewVisibility] with the visible
 * constant at app widget call sites.
 */
fun RemoteViews.makeVisible(@IdRes view: Int) {
    setViewVisibility(view, View.VISIBLE)
}

/**
 * Hides one remote view and shows another.
 *
 * This combines two [RemoteViews.setViewVisibility] calls for the common
 * app-widget content swap case.
 */
fun RemoteViews.crossVisualize(@IdRes hideView: Int, @IdRes showView: Int) {
    hide(hideView)
    makeVisible(showView)
}

/**
 * Applies a color filter to an image-like remote view.
 *
 * This wraps the reflective [RemoteViews.setInt] call and names the underlying
 * `setColorFilter` method for you.
 */
fun RemoteViews.setColorFilter(@IdRes id: Int, @ColorInt color: Int) {
    setInt(id, "setColorFilter", color)
}

/**
 * Sets a remote view background color.
 *
 * This wraps the reflective [RemoteViews.setInt] call and names the underlying
 * `setBackgroundColor` method for you.
 */
fun RemoteViews.setBackgroundColor(@IdRes id: Int, @ColorInt color: Int) {
    setInt(id, "setBackgroundColor", color)
}
