package com.w2sv.androidutils.datastorage.preferences_datastore.flow

import com.w2sv.androidutils.datastorage.preferences_datastore.DataStoreEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DataStoreFlowMap<K, V>(
    map: Map<K, Flow<V>>,
    private val keyToDse: Map<K, DataStoreEntry.UniType<V>>,
    private val saveEntry: suspend (DataStoreEntry.UniType<V>, V) -> Unit
) :
    Map<K, Flow<V>> by map {

    /**
     * Converts values to [StateFlow]s with [DataStoreEntry] default values.
     */
    fun stateIn(scope: CoroutineScope, started: SharingStarted): Map<K, StateFlow<V>> =
        mapValues { (k, v) -> v.stateIn(scope, started, keyToDse.getValue(k).defaultValue) }

    /**
     * Converts values to [StateFlow]s.
     */
    fun stateIn(
        scope: CoroutineScope,
        started: SharingStarted,
        getDefault: (K) -> V
    ): Map<K, StateFlow<V>> =
        mapValues { (k, v) -> v.stateIn(scope, started, getDefault(k)) }

    suspend fun save(map: Map<K, V>) {
        map.forEach { (k, v) ->
            saveEntry(keyToDse.getValue(k), v)
        }
    }
}