@file:Suppress("unused")

package com.w2sv.androidutils.ui.reversible_state

import com.w2sv.androidutils.coroutines.collectFromFlow
import com.w2sv.androidutils.datastorage.preferences_datastore.flow.DataStoreFlow
import com.w2sv.androidutils.datastorage.preferences_datastore.flow.DataStoreStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import slimber.log.i

class ReversibleStateFlow<T>(
    private val scope: CoroutineScope,
    val appliedState: StateFlow<T>,
    private val syncState: suspend (T) -> Unit,
    private val onStateReset: (T) -> Unit = {},
    appliedStateBasedStateAlignmentPostInit: Boolean = true
) : ReversibleState(),
    MutableStateFlow<T> by MutableStateFlow(appliedState.value) {

    /**
     * For construction from [DataStoreFlow].
     */
    constructor(
        scope: CoroutineScope,
        dataStoreFlow: DataStoreFlow<T>,
        started: SharingStarted = SharingStarted.Eagerly,
        onStateReset: (T) -> Unit = {},
        appliedStateBasedStateAlignmentPostInit: Boolean = true
    ) : this(
        scope = scope,
        appliedState = dataStoreFlow.stateIn(scope, started),
        syncState = dataStoreFlow.save,
        onStateReset = onStateReset,
        appliedStateBasedStateAlignmentPostInit = appliedStateBasedStateAlignmentPostInit
    )

    /**
     * For construction from [DataStoreStateFlow].
     */
    constructor(
        scope: CoroutineScope,
        dataStoreStateFlow: DataStoreStateFlow<T>,
        onStateReset: (T) -> Unit = {},
        appliedStateBasedStateAlignmentPostInit: Boolean = true
    ) : this(
        scope = scope,
        appliedState = dataStoreStateFlow,
        syncState = dataStoreStateFlow.save,
        onStateReset = onStateReset,
        appliedStateBasedStateAlignmentPostInit = appliedStateBasedStateAlignmentPostInit
    )

    init {
        if (appliedStateBasedStateAlignmentPostInit) {
            scope.collectFromFlow(appliedState) {
                value = it  // Triggers statesDissimilar update
            }
        }

        // Update [statesDissimilar] whenever a new value is collected
        scope.collectFromFlow(this) {
            _statesDissimilar.value = it != appliedState.value
        }
    }

    override suspend fun sync() {
        i { "Syncing $logIdentifier" }

        syncState(value)
        _statesDissimilar.value = false
    }

    fun launchSync(): Job =
        scope.launch { sync() }

    override fun reset() {
        i { "Resetting $logIdentifier" }

        value = appliedState.value  // Triggers statesDissimilar update
        onStateReset(value)
    }
}