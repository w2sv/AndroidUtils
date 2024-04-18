package com.w2sv.androidutils.datastorage.preferences_datastore.flow

import com.w2sv.androidutils.datastorage.preferences_datastore.DataStoreEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DataStoreFlowMap<Key, Value, DSE : DataStoreEntry<*, Value>>(
    map: Map<Key, Flow<Value>>,
    private val keyToDse: Map<Key, DSE>,
    private val saveEntry: suspend (DSE, Value) -> Unit
) :
    Map<Key, Flow<Value>> by map {

    fun stateIn(scope: CoroutineScope, started: SharingStarted): Map<Key, StateFlow<Value>> =
        mapValues { (k, v) -> v.stateIn(scope, started, keyToDse.getValue(k).defaultValue) }

    suspend fun save(map: Map<Key, Value>) {
        map.forEach { (k, v) ->
            saveEntry(keyToDse.getValue(k), v)
        }
    }
}