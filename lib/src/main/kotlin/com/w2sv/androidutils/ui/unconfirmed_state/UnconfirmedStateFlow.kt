package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.getValueSynchronously
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import slimber.log.i

open class UnconfirmedStateFlow<T>(
    private val coroutineScope: CoroutineScope,
    private val appliedFlow: Flow<T>,
    initialValue: T = appliedFlow.getValueSynchronously(),
    private val syncState: suspend (T) -> Unit
) : UnconfirmedState<T>(),
    MutableStateFlow<T> by MutableStateFlow(initialValue) {

    init {
        // Update [statesDissimilar] whenever a new value is collected
        coroutineScope.launch {
            collect { newValue ->
                _statesDissimilar.value = newValue != appliedFlow.first()
            }
        }
    }

    suspend fun edit(action: (T) -> Unit) {
        action(value)
        _statesDissimilar.value = value != appliedFlow.first()
    }

    fun launchEdit(action: (T) -> Unit): Job =
        coroutineScope.launch {
            edit(action)
        }

    override suspend fun sync() {
        i { "Syncing $logIdentifier" }

        syncState(value)
        _statesDissimilar.value = false
    }

    override suspend fun reset() {
        i { "Resetting $logIdentifier" }

        value = appliedFlow.first()  // Triggers [statesDissimilar] updating flow collector anyways
    }
}