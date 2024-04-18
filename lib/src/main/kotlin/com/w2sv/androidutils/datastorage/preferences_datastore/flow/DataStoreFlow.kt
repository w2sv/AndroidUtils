@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.preferences_datastore.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted

/**
 * A [Flow] holding a [default] value for conversion to a [DataStoreStateFlow] through [stateIn] and exposing a [save] method.
 */
class DataStoreFlow<V>(
    flow: Flow<V>,
    private val default: V,
    val save: suspend (V) -> Unit
) : Flow<V> by flow {

    fun stateIn(
        scope: CoroutineScope,
        started: SharingStarted,
    ): DataStoreStateFlow<V> =
        DataStoreStateFlow(
            flow = this,
            default = default,
            scope = scope,
            started = started,
            save = save
        )
}