@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.preferences_datastore.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * A [StateFlow] exposing a [save] method.
 */
class DataStoreStateFlow<V>(
    stateFlow: StateFlow<V>,
    val save: suspend (V) -> Unit
) : StateFlow<V> by stateFlow {

    constructor(
        flow: Flow<V>,
        default: V,
        scope: CoroutineScope,
        started: SharingStarted,
        save: suspend (V) -> Unit
    ) : this(
        stateFlow = flow.stateIn(scope = scope, started = started, initialValue = default),
        save = save
    )
}