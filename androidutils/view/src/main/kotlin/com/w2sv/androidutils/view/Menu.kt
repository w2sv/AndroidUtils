@file:Suppress("unused")

package com.w2sv.androidutils.view

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.MainThread

@MainThread
inline fun Menu.configureItem(@IdRes id: Int, configure: (MenuItem) -> Unit) {
    configure(findItem(id))
}
