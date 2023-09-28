package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.collectFromFlow
import com.w2sv.androidutils.coroutines.getValueSynchronously
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import slimber.log.i

open class UnconfirmedStateFlow<T>(
    coroutineScope: CoroutineScope,
    private val appliedStateFlow: StateFlow<T>,
    initialValue: T = appliedStateFlow.getValueSynchronously(),
    private val syncState: suspend (T) -> Unit
) : UnconfirmedState(),
    MutableStateFlow<T> by MutableStateFlow(initialValue) {

    constructor(
        coroutineScope: CoroutineScope,
        appliedFlow: Flow<T>,
        getDefaultValue: () -> T,
        syncState: suspend (T) -> Unit
    ) : this(
        coroutineScope = coroutineScope,
        appliedStateFlow = appliedFlow.stateIn(
            coroutineScope,
            SharingStarted.Eagerly,
            getDefaultValue()
        ),
        syncState = syncState
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