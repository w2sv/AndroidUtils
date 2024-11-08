@file:Suppress("unused")

package com.w2sv.androidutils.view

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner

// =======================
// Visibility Alteration
// =======================

fun View.show() {
    visibility = View.VISIBLE
}

fun View.remove() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun crossVisualize(hideView: View, showView: View) {
    hideView.hide()
    showView.show()
}

fun crossVisualize(hideViews: Iterable<View>, showViews: Iterable<View>) {
    hideViews.forEach {
        it.hide()
    }
    showViews.forEach {
        it.show()
    }
}

// ================
// Accessibility
// ================

fun View.increaseTouchArea(pixels: Int) {
    post {
        (parent as View).touchDelegate =
            TouchDelegate(
                getHitRect()
                    .apply {
                        top -= pixels
                        bottom += pixels
                        left -= pixels
                        right += pixels
                    },
                this
            )
    }
}

/**
 * Convenience wrapper for View.getHitRect(outRect: Rect!!).
 */
fun View.getHitRect(): Rect =
    Rect()
        .apply {
            getHitRect(this)
        }

// =======================
// ViewModel Retrieval
// =======================

inline fun <reified VM : ViewModel> View.viewModel(): Lazy<VM> =
    lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[VM::class.java] }

inline fun <reified VM : ViewModel> View.activityViewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            (context as ViewModelStoreOwner)
        )[VM::class.java]
    }
