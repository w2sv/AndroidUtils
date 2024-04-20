package com.w2sv.androidutils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

fun <K, V> Map<K, Flow<V>>.mapValuesToFirstBlocking(): Map<K, V> =
    runBlocking {
        mapValues {
            it.value.first()
        }
    }

fun <K, V> Map<K, StateFlow<V>>.mapValuesToCurrentValue(): Map<K, V> =
    mapValues { it.value.value }

fun <T> Map<T, StateFlow<Boolean>>.valueEnabledKeys(): Set<T> =
    keys.filter { getValue(it).value }

suspend fun <K> Map<K, Flow<Boolean>>.enabledKeys(): Set<K> =
    keys.filter { getValue(it).first() }

private inline fun <T> Set<T>.filter(predicate: (T) -> Boolean): Set<T> =
    filterTo(mutableSetOf(), predicate)

fun <K, V> Map<K, Flow<V>>.statesIn(
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