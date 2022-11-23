package com.w2sv.androidutils.extensions

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import dagger.hilt.android.internal.managers.ViewComponentManager

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun View.ifNotInEditMode(f: () -> Unit) {
    if (!isInEditMode)
        f()
}

inline fun <reified VM : ViewModel> View.viewModel(): Lazy<VM> =
    lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[VM::class.java] }

inline fun <reified VM : ViewModel> View.activityViewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            (context as ViewModelStoreOwner)
        )[VM::class.java]
    }

inline fun <reified VM : ViewModel> View.hiltActivityViewModel(): Lazy<VM> =
    lazy {
        val activityContext =
            (context as? ViewComponentManager.FragmentContextWrapper)?.baseContext ?: context
        ViewModelProvider((activityContext as ViewModelStoreOwner))[VM::class.java]
    }