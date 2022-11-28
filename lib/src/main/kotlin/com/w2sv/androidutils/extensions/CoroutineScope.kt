@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun CoroutineScope.launchDelayed(timeMillis: Long, crossinline f: () -> Unit): Job =
    launch {
        delay(timeMillis)
        f()
    }