@file:Suppress("unused")

package com.w2sv.androidutils.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * @return the nearest [Activity] associated with this [Context].
 *
 * If this context is an [Activity], it is returned directly. If it is a
 * [ContextWrapper], the base context chain is traversed until an [Activity]
 * is found.
 *
 * @throws IllegalStateException if no [Activity] can be found in the context chain.
 */
@Throws(IllegalStateException::class)
fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> error("Couldn't find activity - context isn't derived from one")
    }
