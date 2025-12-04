@file:Suppress("unused")

package com.w2sv.androidutils.view.ext

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner

inline fun <reified VM : ViewModel> View.viewModel(): Lazy<VM> =
    lazy { ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[VM::class.java] }

inline fun <reified VM : ViewModel> View.activityViewModel(): Lazy<VM> =
    lazy {
        ViewModelProvider(
            (context as ViewModelStoreOwner)
        )[VM::class.java]
    }
