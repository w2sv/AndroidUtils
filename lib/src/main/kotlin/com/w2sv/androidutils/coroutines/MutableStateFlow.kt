@file:Suppress("unused")

package com.w2sv.androidutils.coroutines

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T?>.reset() {
    value = null
}