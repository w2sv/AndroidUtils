package com.w2sv.androidutils.datastorage.preferences_datastore.flow

import com.w2sv.androidutils.datastorage.preferences_datastore.DataStoreEntry
import kotlinx.coroutines.flow.StateFlow

class DataStoreStateFlowMap<K, V>(
    map: Map<K, StateFlow<V>>,
    private val keyToDse: Map<K, DataStoreEntry.UniType<V>>,
    private val saveEntry: suspend (DataStoreEntry.UniType<V>, V) -> Unit
) :
    Map<K, StateFlow<V>> by map {

    suspend fun save(map: Map<K, V>) {
        map.forEach { (k, v) ->
            saveEntry(keyToDse.getValue(k), v)
        }
    }
}