package com.w2sv.androidutils.ui.unconfirmed_state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import slimber.log.i

typealias UnconfirmedStates = List<UnconfirmedState>

open class UnconfirmedStatesComposition(
    private val unconfirmedStates: UnconfirmedStates,
    private val coroutineScope: CoroutineScope,
    private val onStateSynced: suspend () -> Unit = {}
) : UnconfirmedStates by unconfirmedStates,
    UnconfirmedState() {

    private val changedStateInstanceIndices = mutableSetOf<Int>()
    private val changedStateInstances
        get() = changedStateInstanceIndices.map(::get)

    init {
        // Update [changedStateInstanceIndices] and [_statesDissimilar] upon change of one of
        // the held element's [statesDissimilar]
        coroutineScope.launch {
            mapIndexed { i, instance ->
                instance.statesDissimilar.map { i to it }
            }
                .merge()
                .collect { (i, statesDissimilar) ->
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

    override fun reset() {
        i { "Resetting $logIdentifier" }

        changedStateInstances.forEach {
            it.reset()
        }
    }

    fun launchSync(): Job =
        coroutineScope.launch { sync() }
}