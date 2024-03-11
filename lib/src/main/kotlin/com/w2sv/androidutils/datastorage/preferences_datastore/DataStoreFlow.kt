@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.preferences_datastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DataStoreFlow<V>(
    flow: Flow<V>,
    private val default: V,
    val save: suspend (V) -> Unit
) : Flow<V> by flow {

    fun stateIn(
        scope: CoroutineScope,
        started: SharingStarted,
    ): StateFlow<V> =
        stateIn(scope, started, default)
}