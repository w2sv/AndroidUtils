@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job

inline fun Job?.invokeOnCompletion(crossinline block: () -> Unit): DisposableHandle? =
    if (this != null) {
        invokeOnCompletion {
            block()
        }
    } else {
        block()
        null
    }