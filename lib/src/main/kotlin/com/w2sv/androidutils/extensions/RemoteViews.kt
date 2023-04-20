@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import android.view.View
import android.widget.RemoteViews
import androidx.annotation.IdRes

fun RemoteViews.crossVisualize(@IdRes hideView: Int, @IdRes showView: Int) {
    setViewVisibility(showView, View.VISIBLE)
    setViewVisibility(hideView, View.GONE)
}

fun RemoteViews.setColorFilter(@IdRes id: Int, color: Int) {
    setInt(id, "setColorFilter", color)
}

fun RemoteViews.setBackgroundColor(@IdRes id: Int, color: Int) {
    setInt(id, "setBackgroundColor", color)
}