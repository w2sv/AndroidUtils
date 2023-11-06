package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.collectFromFlow
import com.w2sv.androidutils.datastorage.datastore.preferences.PersistedValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import slimber.log.i

open class UnconfirmedStateFlow<T>(
    coroutineScope: CoroutineScope,
    private val appliedStateFlow: StateFlow<T>,
    private val syncState: suspend (T) -> Unit
) : UnconfirmedState(),
    MutableStateFlow<T> by MutableStateFlow(appliedStateFlow.value) {

    /**
     * Construct from [PersistedValue].
     */
    constructor(
        coroutineScope: CoroutineScope,
        persistedValue: PersistedValue<*, T>,
        started: SharingStarted
    ) : this(
        coroutineScope = coroutineScope,
        appliedStateFlow = persistedValue.stateIn(coroutineScope, started),
        syncState = persistedValue.save
    )

    init {
        // Update [statesDissimilar] whenever a new value is collected
        coroutineScope.collectFromFlow(this) { newValue ->
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