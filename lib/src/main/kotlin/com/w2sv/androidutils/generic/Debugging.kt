@file:Suppress("unused")

package com.w2sv.androidutils.generic

import timber.log.Timber
import kotlin.system.measureTimeMillis

inline fun <T> measured(methodLabel: String, tag: String? = null, f: () -> T): T {
    var result: T
    measureTimeMillis {
        result = f()
    }
        .also {
            Timber.d(tag, "$methodLabel completed in ${it}ms")
        }

    return result
}
