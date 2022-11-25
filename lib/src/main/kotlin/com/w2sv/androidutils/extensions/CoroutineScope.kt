@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

inline fun <R> CoroutineScope.launchWithOnFinishedListener(
    crossinline f: () -> R,
    crossinline onFinishedListener: ((R) -> Unit)
) =
    launch {
        onFinishedListener(
            withContext(Dispatchers.IO) {
                f()
            }
        )
    }

inline fun <P, R> CoroutineScope.launchWithOnProgressOnFinishedListener(
    crossinline f: suspend (suspend (P) -> Unit) -> R,
    crossinline onProgressListener: (P) -> Unit,
    crossinline onFinishedListener: (R) -> Unit
) =
    launch {
        onFinishedListener(
            withContext(Dispatchers.IO) {
                f {
                    withContext(Dispatchers.Main) {
                        onProgressListener(it)
                    }
                }
            }
        )
    }

inline fun CoroutineScope.launchDelayed(timeMillis: Long, crossinline f: () -> Unit): Job =
    launch {
        delay(timeMillis)
        f()
    }