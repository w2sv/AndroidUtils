package com.w2sv.androidutils.ui.unconfirmed_state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import slimber.log.i

typealias UnconfirmedStates = List<UnconfirmedState<*>>

open class UnconfirmedStatesComposition(
    unconfirmedStates: UnconfirmedStates,
    private val coroutineScope: CoroutineScope,
    private val onStateSynced: suspend () -> Unit = {}
) : UnconfirmedStates by unconfirmedStates,
    UnconfirmedState<UnconfirmedStates>() {

    private val changedStateInstanceIndices = mutableSetOf<Int>()
    private val changedStateInstances get() = changedStateInstanceIndices.map { this[it] }

    init {
        // Update [changedStateInstanceIndices] and [_statesDissimilar] upon change of one of
        // the held element's [statesDissimilar]
        coroutineScope.launch {
            mapIndexed { i, it -> it.statesDissimilar.transform { emit(it to i) } }
                .merge()
                .collect { (statesDissimilar, i) ->
                    if (statesDissimilar) {
                        changedStateInstanceIndices.add(i)
                    } else {
                        changedStateInstanceIndices.remove(i)
                    }

                    _statesDissimilar.value = changedStateInstanceIndices.isNotEmpty()
                }
        }
    }

    override suspend fun sync() {
        i { "Syncing $logIdentifier" }

        changedStateInstances.forEach {
            it.sync()
        }
        onStateSynced()
    }

    override suspend fun reset() {
        i { "Resetting $logIdentifier" }

        changedStateInstances.forEach {
            it.reset()
        }
    }

    fun launchSync(): Job =
        coroutineScope.launch { sync() }

    fun launchReset(): Job =
        coroutineScope.launch { reset() }
}