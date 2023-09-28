package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.getSynchronousMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import slimber.log.i

open class UnconfirmedStateMap<K, V>(
    private val coroutineScope: CoroutineScope,
    private val appliedFlowMap: Map<K, Flow<V>>,
    private val map: MutableMap<K, V> = appliedFlowMap
        .getSynchronousMap()
        .toMutableMap(),
    private val syncState: suspend (Map<K, V>) -> Unit,
    private val onStateSynced: suspend (Map<K, V>) -> Unit = {}
) : UnconfirmedState<Map<K, V>>(),
    MutableMap<K, V> by map {

    constructor(
        coroutineScope: CoroutineScope,
        appliedFlowMap: Map<K, Flow<V>>,
        makeSynchronousMutableMap: (Map<K, Flow<V>>) -> MutableMap<K, V>,
        syncState: suspend (Map<K, V>) -> Unit
    ) : this(coroutineScope, appliedFlowMap, makeSynchronousMutableMap(appliedFlowMap), syncState)

    val dissimilarKeys: Set<K>
        get() = _dissimilarKeys

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
        onStateSynced(this)
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