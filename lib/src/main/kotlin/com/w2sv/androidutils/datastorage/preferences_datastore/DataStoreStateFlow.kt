@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.preferences_datastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreStateFlow<V>(
    flow: Flow<V>,
    private val default: V,
    private val scope: CoroutineScope,
    sharingStarted: SharingStarted,
    val save: suspend (V) -> Unit
) : StateFlow<V> by flow.stateIn(scope = scope, started = sharingStarted, initialValue = default) {

    fun saveOnSharingScope(value: V) {
        scope.launch { save(value) }
    }
}