package com.w2sv.androidutils.ui.reversible_state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import slimber.log.i

typealias ReversibleStates = List<ReversibleState>

open class ReversibleStatesComposition(
    private val reversibleStates: ReversibleStates,
    private val coroutineScope: CoroutineScope,
    private val onStateSynced: suspend (ReversibleStates) -> Unit = {},
    private val onStateReset: (ReversibleStates) -> Unit = {}
) : ReversibleStates by reversibleStates,
    ReversibleState() {

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
        onStateSynced(this)
    }

    override fun reset() {
        i { "Resetting $logIdentifier" }

        changedStateInstances.forEach {
            it.reset()
        }

        onStateReset(this)
    }

    fun launchSync(): Job =
        coroutineScope.launch { sync() }
}