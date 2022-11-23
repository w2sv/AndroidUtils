package com.w2sv.androidutils.extensions

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes

inline fun Menu.configureItem(@IdRes id: Int, f: (MenuItem) -> Unit) {
    f(findItem(id))
}