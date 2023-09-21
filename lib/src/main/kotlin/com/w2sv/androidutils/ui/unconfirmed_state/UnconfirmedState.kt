package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.getSynchronousMap
import com.w2sv.androidutils.coroutines.getValueSynchronously
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import slimber.log.i

/**
 * Base class for classes, encapsulating states being displayed by the UI but pending
 * confirmation, which in turn triggers the synchronization with the respective repository.
 */
abstract class UnconfirmedState<T> {
    val statesDissimilar: StateFlow<Boolean> get() = _statesDissimilar
    protected val _statesDissimilar = MutableStateFlow(false)

    protected val logIdentifier: String get() = this::class.java.simpleName

    abstract suspend fun sync()
    abstract suspend fun reset()
}

open class UnconfirmedStateMap<K, V>(
    private val coroutineScope: CoroutineScope,
    private val appliedFlowMap: Map<K, Flow<V>>,
    private val map: MutableMap<K, V> = appliedFlowMap
        .getSynchronousMap()
        .toMutableMap(),
    private val syncState: suspend (Map<K, V>) -> Unit
) : UnconfirmedState<Map<K, V>>(),
    MutableMap<K, V> by map {

    constructor(
        coroutineScope: CoroutineScope,
        appliedFlowMap: Map<K, Flow<V>>,
        makeSynchronousMutableMap: (Map<K, Flow<V>>) -> MutableMap<K, V>,
        syncState: suspend (Map<K, V>) -> Unit
    ) : this(coroutineScope, appliedFlowMap, makeSynchronousMutableMap(appliedFlowMap), syncState)

    val dissimilarKeys: Set<K> get() = _dissimilarKeys

    /**
     * Tracking of keys which correspond to values, differing between [appliedFlowMap] and this
     * for efficient syncing/resetting.
     */
    private val _dissimilarKeys = mutableSetOf<K>()

    // ==============
    // Modification
    // ==============

    /**
     * Inherently updates [_dissimilarKeys] and [statesDissimilar] in an asynchronous fashion.
     */
    override fun put(key: K, value: V): V? =
        map.put(key, value)
            .also {
                coroutineScope.launch {
                    when (value == appliedFlowMap.getValue(key).first()) {
                        true -> _dissimilarKeys.remove(key)
                        false -> _dissimilarKeys.add(key)
                    }
                    _statesDissimilar.value = _dissimilarKeys.isNotEmpty()
                }
            }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (k, v) ->
            put(k, v)
        }
    }

    // =======================
    // Syncing / Resetting
    // =======================

    override suspend fun sync() = withSubsequentInternalReset {
        i { "Syncing $logIdentifier" }

        syncState(filterKeys { it in _dissimilarKeys })
    }

    override suspend fun reset() = withSubsequentInternalReset {
        _dissimilarKeys
            .forEach {
                // Call map.put directly to prevent unnecessary state updates
                map[it] = appliedFlowMap.getValue(it).first()
            }
    }

    private inline fun withSubsequentInternalReset(f: () -> Unit) {
        f()

        _dissimilarKeys.clear()
        _statesDissimilar.value = false
    }
}

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

typealias UnconfirmedStates = List<UnconfirmedState<*>>

open class UnconfirmedStatesComposition(
    unconfirmedStates: UnconfirmedStates,
    coroutineScope: CoroutineScope,
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
}