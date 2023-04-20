@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun <T> Flow<T>.getValueSynchronously(): T =
    runBlocking { first() }