package com.w2sv.androidutils.utils

import android.util.Log.d
import kotlin.system.measureTimeMillis

inline fun <T> measured(methodLabel: String, tag: String? = null, f: () -> T): T {
    var result: T
    measureTimeMillis {
        result = f()
    }
        .also {
            d(tag, "$methodLabel completed in ${it}ms")
        }

    return result
}
