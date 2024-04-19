@file:Suppress("unused")

package com.w2sv.androidutils.ui.reversible_state

import com.w2sv.androidutils.coroutines.collectFromFlow
import com.w2sv.androidutils.datastorage.preferences_datastore.flow.DataStoreFlow
import com.w2sv.androidutils.datastorage.preferences_datastore.flow.DataStoreStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import slimber.log.i

class ReversibleStateFlow<T>(
    scope: CoroutineScope,
    val appliedStateFlow: StateFlow<T>,
    private val syncState: suspend (T) -> Unit,
    private val onStateReset: (T) -> Unit = {},
    assureAppliedStateFlowIsUpToDate: Boolean = true
) : ReversibleState(),
    MutableStateFlow<T> by MutableStateFlow(appliedStateFlow.value) {

    /**
     * For construction from [DataStoreFlow].
     */
    constructor(
        scope: CoroutineScope,
        dataStoreFlow: DataStoreFlow<T>,
        started: SharingStarted = SharingStarted.Eagerly,
        onStateReset: (T) -> Unit = {},
        assureAppliedStateFlowIsUpToDate: Boolean = true
    ) : this(
        scope = scope,
        appliedStateFlow = dataStoreFlow.stateIn(scope, started),
        syncState = dataStoreFlow.save,
        onStateReset = onStateReset,
        assureAppliedStateFlowIsUpToDate = assureAppliedStateFlowIsUpToDate
    )

    /**
     * For construction from [DataStoreStateFlow].
     */
    constructor(
        scope: CoroutineScope,
        dataStoreStateFlow: DataStoreStateFlow<T>,
        onStateReset: (T) -> Unit = {},
        assureAppliedStateFlowIsUpToDate: Boolean = true
    ) : this(
        scope = scope,
        appliedStateFlow = dataStoreStateFlow,
        syncState = dataStoreStateFlow.save,
        onStateReset = onStateReset,
        assureAppliedStateFlowIsUpToDate = assureAppliedStateFlowIsUpToDate
    )

    init {
        if (assureAppliedStateFlowIsUpToDate) {
            scope.collectFromFlow(appliedStateFlow.take(2)) {
                value = it
            }
        }

        // Update [statesDissimilar] whenever a new value is collected
        scope.collectFromFlow(this) {
            _statesDissimilar.value = it != appliedStateFlow.value
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
        onStateReset(value)
    }
}