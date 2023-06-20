@file:Suppress("unused")

package com.w2sv.androidutils.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun <T> Flow<T>.getValueSynchronously(): T =
    runBlocking { first() }

fun <K, V> Map<K, Flow<V>>.getSynchronousMap(): Map<K, V> =
    runBlocking {
        mapValues {
            it.value.first()
        }
    }