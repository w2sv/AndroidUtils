@file:Suppress("unused")

package com.w2sv.androidutils.appwidget

import android.view.View
import android.widget.RemoteViews
import androidx.annotation.IdRes

fun RemoteViews.hide(@IdRes view: Int, visibility: Int = View.GONE) {
    setViewVisibility(view, visibility)
}

fun RemoteViews.makeVisible(@IdRes view: Int) {
    setViewVisibility(view, View.VISIBLE)
}

fun RemoteViews.crossVisualize(@IdRes hideView: Int, @IdRes showView: Int) {
    hide(hideView)
    makeVisible(showView)
}

fun RemoteViews.setColorFilter(@IdRes id: Int, color: Int) {
    setInt(id, "setColorFilter", color)
}

fun RemoteViews.setBackgroundColor(@IdRes id: Int, color: Int) {
    setInt(id, "setBackgroundColor", color)
}
