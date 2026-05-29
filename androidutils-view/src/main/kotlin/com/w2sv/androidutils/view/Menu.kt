@file:Suppress("unused")

package com.w2sv.androidutils.view

import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.MainThread

/**
 * Finds a menu item by [id] and configures it.
 *
 * This keeps [Menu.findItem] lookup close to the configuration block and avoids
 * temporary variables at call sites.
 */
@MainThread
inline fun Menu.configureItem(@IdRes id: Int, configure: (MenuItem) -> Unit) {
    configure(findItem(id))
}
