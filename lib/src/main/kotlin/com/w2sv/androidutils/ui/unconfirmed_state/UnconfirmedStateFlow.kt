package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.collectFromFlow
import com.w2sv.androidutils.datastorage.datastore.preferences.DataStoreFlow
import com.w2sv.androidutils.datastorage.datastore.preferences.DataStoreStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import slimber.log.i

class UnconfirmedStateFlow<T>(
    scope: CoroutineScope,
    val appliedStateFlow: StateFlow<T>,
    private val syncState: suspend (T) -> Unit
) : UnconfirmedState(),
    MutableStateFlow<T> by MutableStateFlow(appliedStateFlow.value) {

    /**
     * For construction from [DataStoreFlow].
     */
    constructor(
        coroutineScope: CoroutineScope,
        dataStoreFlow: DataStoreFlow<T>,
        started: SharingStarted = SharingStarted.Eagerly
    ) : this(
        scope = coroutineScope,
        appliedStateFlow = dataStoreFlow.stateIn(coroutineScope, started),
        syncState = dataStoreFlow.save
    )

    /**
     * For construction from [DataStoreFlow].
     */
    constructor(
        coroutineScope: CoroutineScope,
        dataStoreStateFlow: DataStoreStateFlow<T>,
    ) : this(
        scope = coroutineScope,
        appliedStateFlow = dataStoreStateFlow,
        syncState = dataStoreStateFlow.save
    )

    init {
        // Update [statesDissimilar] whenever a new value is collected
        scope.collectFromFlow(this) { newValue ->
            _statesDissimilar.value = newValue != appliedStateFlow.value
        }
    }

    override suspend fun sync() {
        i { "Syncing $logIdentifier" }

        syncState(value)
        _statesDissimilar.value = false
    }

    override fun reset() {
        i { "Resetting $logIdentifier" }

        value =
            appliedStateFlow.value  // Triggers [statesDissimilar] updating flow collector anyways
    }
}