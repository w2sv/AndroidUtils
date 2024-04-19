package com.w2sv.androidutils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

fun <K, V> Map<K, Flow<V>>.synchronous(): Map<K, V> =
    runBlocking {
        mapValues {
            it.value.first()
        }
    }

val <K, V> Map<K, StateFlow<V>>.value: Map<K, V>
    get() = mapValues { it.value.value }

fun <T> Map<T, StateFlow<Boolean>>.valueEnabledKeys(): List<T> =
    keys.filter { getValue(it).value }

suspend fun <K> Map<K, Flow<Boolean>>.enabledKeys(): List<K> =
    keys.filter { getValue(it).first() }

fun <K, V> Map<K, Flow<V>>.stateIn(
    scope: CoroutineScope,
    started: SharingStarted,
    initialValue: V
): Map<K, StateFlow<V>> =
    mapValues { (_, v) ->
        v.stateIn(
            scope,
            started,
            initialValue
        )
    }