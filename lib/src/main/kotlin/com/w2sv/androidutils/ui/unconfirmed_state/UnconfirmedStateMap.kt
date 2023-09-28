package com.w2sv.androidutils.ui.unconfirmed_state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import slimber.log.i

abstract class KeyedUnconfirmedState<K> : UnconfirmedState() {
    val dissimilarKeys: Set<K>
        get() = _dissimilarKeys

    /**
     * Tracking of keys which correspond to values, differing between [appliedStateMap] and this
     * for efficient syncing/resetting.
     */
    protected val _dissimilarKeys = mutableSetOf<K>()

    protected fun resetDissimilarityTrackers() {
        _dissimilarKeys.clear()
        _statesDissimilar.value = false
    }
}

open class UnconfirmedStateMap<K, V>(
    private val map: MutableMap<K, V>,
    private val appliedStateFlowMap: Map<K, StateFlow<V>>,
    private val syncState: suspend (Map<K, V>) -> Unit,
    private val onStateSynced: suspend (Map<K, V>) -> Unit = {}
) : KeyedUnconfirmedState<K>(),
    MutableMap<K, V> by map {

    constructor(
        appliedStateFlowMap: Map<K, StateFlow<V>>,
        makeMap: (Map<K, Flow<V>>) -> MutableMap<K, V>,
        syncState: suspend (Map<K, V>) -> Unit,
        onStateSynced: suspend (Map<K, V>) -> Unit = {}
    ) : this(
        map = makeMap(appliedStateFlowMap),
        appliedStateFlowMap = appliedStateFlowMap,
        syncState = syncState,
        onStateSynced = onStateSynced
    )

    constructor(
        coroutineScope: CoroutineScope,
        appliedFlowMap: Map<K, Flow<V>>,
        getDefaultValue: (K) -> V,
        makeMap: (Map<K, Flow<V>>) -> MutableMap<K, V>,
        syncState: suspend (Map<K, V>) -> Unit,
        onStateSynced: suspend (Map<K, V>) -> Unit = {}
    ) : this(
        appliedStateFlowMap = appliedFlowMap.mapValues { (k, v) ->
            v.stateIn(
                coroutineScope,
                SharingStarted.Eagerly,
                getDefaultValue(k)
            )
        },
        makeMap = makeMap,
        syncState = syncState,
        onStateSynced = onStateSynced
    )

    // ==============
    // Modification
    // ==============

    /**
     * Inherently updates [_dissimilarKeys] and [statesDissimilar] in an asynchronous fashion.
     */
    override fun put(key: K, value: V): V? =
        map.put(key, value)
            .also {
                when (value == appliedStateFlowMap.getValue(key).value) {
                    true -> _dissimilarKeys.remove(key)
                    false -> _dissimilarKeys.add(key)
                }
                _statesDissimilar.value = _dissimilarKeys.isNotEmpty()
            }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (k, v) ->
            put(k, v)
        }
    }

    // =======================
    // Syncing / Resetting
    // =======================

    override suspend fun sync() {
        i { "Syncing $logIdentifier" }

        syncState(filterKeys { it in _dissimilarKeys })
        resetDissimilarityTrackers()

        onStateSynced(this)
    }

    override fun reset() {
        i { "Resetting $logIdentifier" }

        _dissimilarKeys
            .forEach {
                // Call map.put rather than this.put, to prevent unnecessary state updates
                map[it] = appliedStateFlowMap.getValue(it).value
            }
        resetDissimilarityTrackers()
    }
}

open class UnconfirmedStateFlowMap<K, V>(
    private val map: Map<K, MutableStateFlow<V>>,
    private val appliedStateFlowMap: Map<K, StateFlow<V>>,
    private val syncState: suspend (Map<K, V>) -> Unit,
    private val onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {}
) : KeyedUnconfirmedState<K>(),
    Map<K, StateFlow<V>> by map {

    companion object {
        fun <K, V> fromAppliedFlowMap(
            appliedFlowMap: Map<K, Flow<V>>,
            getDefaultValue: (K) -> V,
            coroutineScope: CoroutineScope,
            syncState: suspend (Map<K, V>) -> Unit,
            onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {}
        ): UnconfirmedStateFlowMap<K, V> {
            val appliedStateFlowMap = appliedFlowMap.mapValues { (k, v) ->
                v.stateIn(coroutineScope, SharingStarted.Eagerly, getDefaultValue(k))
            }

            return UnconfirmedStateFlowMap(
                map = appliedStateFlowMap.mapValues { (_, v) -> MutableStateFlow(v.value) },
                appliedStateFlowMap = appliedStateFlowMap,
                syncState = syncState,
                onStateSynced = onStateSynced
            )
        }
    }

    // ==============
    // Modification
    // ==============

    fun changeValue(key: K, value: V) {
        map.getValue(key).value = value

        when (value == appliedStateFlowMap.getValue(key).value) {
            true -> _dissimilarKeys.remove(key)
            false -> _dissimilarKeys.add(key)
        }
        _statesDissimilar.value = _dissimilarKeys.isNotEmpty()
    }

    // =======================
    // Syncing / Resetting
    // =======================

    override suspend fun sync() {
        i { "Syncing $logIdentifier" }

        syncState(filterKeys { it in _dissimilarKeys }.mapValues { it.value.value })
        resetDissimilarityTrackers()

        onStateSynced(this)
    }

    override fun reset() {
        i { "Resetting $logIdentifier" }

        _dissimilarKeys
            .forEach {
                // Call map.put rather than this.put, to prevent unnecessary state updates
                map.getValue(it).value = appliedStateFlowMap.getValue(it).value
            }
        resetDissimilarityTrackers()
    }
}