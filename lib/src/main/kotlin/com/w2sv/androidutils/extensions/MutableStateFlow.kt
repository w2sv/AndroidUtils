@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T?>.reset() {
    value = null
}

fun MutableStateFlow<Boolean>.resetBoolean() {
    value = false
}