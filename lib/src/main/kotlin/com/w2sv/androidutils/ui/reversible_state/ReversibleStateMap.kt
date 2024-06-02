@file:Suppress("unused")

package com.w2sv.androidutils.ui.reversible_state

import com.w2sv.androidutils.coroutines.collectFromFlow
import com.w2sv.androidutils.coroutines.stateInWithSynchronousInitial
import com.w2sv.androidutils.coroutines.mapValuesToCurrentValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import slimber.log.i

/**
 * @param map Delegate. Represents the non-persisted, temporary state.
 * @param appliedStateMap Persisted state. Holds the same keys as [map].
 * @param syncState To save the temporary state, such that [appliedStateMap] and [map] are in sync. Receives only [map] key-value pairs, whose values have changed with respect to the persisted state, held by [appliedStateMap].
 * @param onStateSynced Possibility to invoke a callback upon the temporary and the persisted states having been synced. Receives contrarily to [syncState] the entire map.
 * @param onStateReset Callback invoked after state having been reset. Receives the reset map.
 */
open class ReversibleStateMap<K, V>(
    private val map: MutableMap<K, V>,
    val appliedStateMap: Map<K, StateFlow<V>>,
    private val syncState: suspend (Map<K, V>) -> Unit,
    private val onStateSynced: suspend (Map<K, V>) -> Unit = {},
    private val onStateReset: (Map<K, V>) -> Unit = {},
    appliedStateMapBasedStateAlignmentScope: CoroutineScope? = null
) : MappedReversibleState<K>(),
    MutableMap<K, V> by map {

    /**
     * Enables passing of [makeMap] function instead of having to invoke [appliedStateMap] twice, which would require a capturing of it as val or scope function receiver.
     */
    constructor(
        appliedStateMap: Map<K, StateFlow<V>>,
        makeMap: (Map<K, V>) -> MutableMap<K, V>,
        syncState: suspend (Map<K, V>) -> Unit,
        onStateSynced: suspend (Map<K, V>) -> Unit = {},
        onStateReset: (Map<K, V>) -> Unit = {},
        appliedStateMapBasedStateAlignmentScope: CoroutineScope? = null
    ) : this(
        map = makeMap(appliedStateMap.mapValuesToCurrentValue()),
        appliedStateMap = appliedStateMap,
        syncState = syncState,
        onStateSynced = onStateSynced,
        onStateReset = onStateReset,
        appliedStateMapBasedStateAlignmentScope = appliedStateMapBasedStateAlignmentScope
    )

    companion object {
        fun <K, V> fromAppliedFlowMapWithSynchronousInitial(
            appliedFlowMap: Map<K, Flow<V>>,
            scope: CoroutineScope,
            makeMap: (Map<K, V>) -> MutableMap<K, V>,
            syncState: suspend (Map<K, V>) -> Unit,
            onStateSynced: suspend (Map<K, V>) -> Unit = {},
            onStateReset: (Map<K, V>) -> Unit = {},
            appliedStateMapBasedStateAlignmentScope: CoroutineScope? = null
        ): ReversibleStateMap<K, V> {
            val appliedStateFlowMap = appliedFlowMap.mapValues { (_, v) ->
                v.stateInWithSynchronousInitial(scope)
            }

            return ReversibleStateMap(
                map = makeMap(appliedStateFlowMap.mapValuesToCurrentValue()),
                appliedStateMap = appliedStateFlowMap,
                syncState = syncState,
                onStateSynced = onStateSynced,
                onStateReset = onStateReset,
                appliedStateMapBasedStateAlignmentScope = appliedStateMapBasedStateAlignmentScope
            )
        }
    }

    init {
        appliedStateMapBasedStateAlignmentScope?.let { scope ->
            appliedStateMap.forEach { (k, v) ->
                scope.collectFromFlow(v) {
                    put(k, it)
                }
            }
        }
    }

    // ==============
    // Modification
    // ==============

    /**
     * Inherently updates [_dissimilarKeys] and [statesDissimilar].
     */
    override fun put(key: K, value: V): V? =
        map.put(key, value)
            .also {
                when (value == appliedStateMap.getValue(key).value) {
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

        syncState(_dissimilarKeys.associateWith { getValue(it) })
        resetDissimilarityTrackers()

        onStateSynced(this)
    }

    override fun reset() {
        i { "Resetting $logIdentifier" }

        _dissimilarKeys
            .forEach {
                // Call map.put rather than this.put, to prevent unnecessary state updates
                map[it] = appliedStateMap.getValue(it).value
            }
        resetDissimilarityTrackers()
        onStateReset(this)
    }
}

///**
// * @param map Delegate. Represents the non-persisted, temporary state.
// * @param persistedStateFlowMap Representation of the persisted state. Holds the same keys as [map].
// * @param syncState To save the temporary state, such that [persistedStateFlowMap] and [map] are in sync. Receives only [map] key-value pairs, whose values have changed with respect to the persisted state, held by [persistedStateFlowMap].
// * @param onStateSynced Possibility to invoke a callback upon the temporary and the persisted states having been synced. Receives contrarily to [syncState] the entire map.
// * @param onStateReset Callback invoked after state having been reset. Receives the reset map.
// */
//open class ReversibleStateFlowMap<K, V>(
//    private val map: Map<K, MutableStateFlow<V>>,
//    private val persistedStateFlowMap: Map<K, StateFlow<V>>,
//    private val syncState: suspend (Map<K, V>) -> Unit,
//    private val onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {},
//    private val onStateReset: (Map<K, StateFlow<V>>) -> Unit = {}
//) : MappedReversibleState<K>(),
//    Map<K, MutableStateFlow<V>> by map {
//
//    companion object {
//        fun <K, V> fromPersistedFlowMap(
//            persistedFlowMap: Map<K, Flow<V>>,
//            getDefaultValue: (K) -> V,
//            coroutineScope: CoroutineScope,
//            syncState: suspend (Map<K, V>) -> Unit,
//            onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {},
//            onStateReset: (Map<K, StateFlow<V>>) -> Unit = {}
//        ): ReversibleStateFlowMap<K, V> =
//            fromPersistedStateFlowMap(
//                persistedStateFlowMap = persistedFlowMap.mapValues { (k, v) ->
//                    v.stateIn(coroutineScope, SharingStarted.Eagerly, getDefaultValue(k))
//                },
//                syncState = syncState,
//                onStateSynced = onStateSynced,
//                onStateReset = onStateReset
//            )
//
//        fun <K, V> fromPersistedStateFlowMap(
//            persistedStateFlowMap: Map<K, StateFlow<V>>,
//            syncState: suspend (Map<K, V>) -> Unit,
//            onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {},
//            onStateReset: (Map<K, StateFlow<V>>) -> Unit = {}
//        ): ReversibleStateFlowMap<K, V> =
//            ReversibleStateFlowMap(
//                map = persistedStateFlowMap.mapValues { (_, v) -> MutableStateFlow(v.value) },
//                persistedStateFlowMap = persistedStateFlowMap,
//                syncState = syncState,
//                onStateSynced = onStateSynced,
//                onStateReset = onStateReset
//            )
//    }
//
//    // ==============
//    // Modification
//    // ==============
//
//    operator fun set(key: K, value: V) {
//        map.mapValuesToCurrentValue(key).value = value
//
//        when (value == persistedStateFlowMap.mapValuesToCurrentValue(key).value) {
//            true -> _dissimilarKeys.remove(key)
//            false -> _dissimilarKeys.add(key)
//        }
//        _statesDissimilar.value = _dissimilarKeys.isNotEmpty()
//    }
//
//    // =======================
//    // Syncing / Resetting
//    // =======================
//
//    override suspend fun sync() {
//        i { "Syncing $logIdentifier" }
//
//        syncState(filterKeys { it in _dissimilarKeys }.mapValues { it.value.value })
//        resetDissimilarityTrackers()
//
//        onStateSynced(this)
//    }
//
//    override fun reset() {
//        i { "Resetting $logIdentifier" }
//
//        _dissimilarKeys
//            .forEach {
//                // Call map.put rather than this.put, to prevent unnecessary state updates
//                map.mapValuesToCurrentValue(it).value = persistedStateFlowMap.mapValuesToCurrentValue(it).value
//            }
//        resetDissimilarityTrackers()
//        onStateReset(this)
//    }
//}