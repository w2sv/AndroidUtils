@file:Suppress("unused")

package com.w2sv.androidutils.view.ext

import android.view.View
import androidx.annotation.MainThread

@MainThread
fun View.show() {
    visibility = View.VISIBLE
}

@MainThread
fun View.remove() {
    visibility = View.GONE
}

@MainThread
fun View.hide() {
    visibility = View.INVISIBLE
}

@MainThread
fun crossVisualize(hideView: View, showView: View) {
    hideView.hide()
    showView.show()
}

@MainThread
fun crossVisualize(hideViews: Iterable<View>, showViews: Iterable<View>) {
    hideViews.forEach { it.hide() }
    showViews.forEach { it.show() }
}
