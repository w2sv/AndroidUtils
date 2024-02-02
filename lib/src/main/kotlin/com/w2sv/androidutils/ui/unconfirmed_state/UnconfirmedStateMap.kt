@file:Suppress("unused")

package com.w2sv.androidutils.ui.unconfirmed_state

import com.w2sv.androidutils.coroutines.stateInWithSynchronousInitial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import slimber.log.i

abstract class MappedUnconfirmedState<K> : UnconfirmedState() {

    /**
     * Keys whose values have changed.
     */
    protected val _dissimilarKeys = mutableSetOf<K>()

    val dissimilarKeys: Set<K>
        get() = _dissimilarKeys

    protected fun resetDissimilarityTrackers() {
        _dissimilarKeys.clear()
        _statesDissimilar.value = false
    }
}

/**
 * @param map Delegate. Represents the non-persisted, temporary state.
 * @param persistedStateFlowMap Representation of the persisted state. Holds the same keys as [map].
 * @param syncState To save the temporary state, such that [persistedStateFlowMap] and [map] are in sync. Receives only [map] key-value pairs, whose values have changed with respect to the persisted state, held by [persistedStateFlowMap].
 * @param onStateSynced Possibility to invoke a callback upon the temporary and the persisted states having been synced. Receives contrarily to [syncState] the entire map.
 */
open class UnconfirmedStateMap<K, V>(
    private val map: MutableMap<K, V>,
    val persistedStateFlowMap: Map<K, StateFlow<V>>,
    private val syncState: suspend (Map<K, V>) -> Unit,
    private val onStateSynced: suspend (Map<K, V>) -> Unit = {}
) : MappedUnconfirmedState<K>(),
    MutableMap<K, V> by map {

    /**
     * Enables passing of [makeMap] function instead of having to invoke [persistedStateFlowMap] twice, which would require a capturing of it as val or scope function receiver.
     */
    constructor(
        persistedStateFlowMap: Map<K, StateFlow<V>>,
        makeMap: (Map<K, StateFlow<V>>) -> MutableMap<K, V>,
        syncState: suspend (Map<K, V>) -> Unit,
        onStateSynced: suspend (Map<K, V>) -> Unit = {}
    ) : this(
        map = makeMap(persistedStateFlowMap),
        persistedStateFlowMap = persistedStateFlowMap,
        syncState = syncState,
        onStateSynced = onStateSynced
    )

    companion object {
        fun <K, V> fromPersistedFlowMapWithSynchronousInitial(
            persistedFlowMap: Map<K, Flow<V>>,
            scope: CoroutineScope,
            makeMap: (Map<K, V>) -> MutableMap<K, V>,
            syncState: suspend (Map<K, V>) -> Unit,
            onStateSynced: suspend (Map<K, V>) -> Unit = {}
        ): UnconfirmedStateMap<K, V> {
            val persistedStateFlowMap = persistedFlowMap.mapValues { (_, v) ->
                v.stateInWithSynchronousInitial(
                    scope,
                    SharingStarted.Eagerly
                )
            }

            return UnconfirmedStateMap(
                map = makeMap(persistedStateFlowMap.mapValues { (_, v) -> v.value }),
                persistedStateFlowMap = persistedStateFlowMap,
                syncState = syncState,
                onStateSynced = onStateSynced
            )
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
                when (value == persistedStateFlowMap.getValue(key).value) {
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
                map[it] = persistedStateFlowMap.getValue(it).value
            }
        resetDissimilarityTrackers()
    }
}

/**
 * @param map Delegate. Represents the non-persisted, temporary state.
 * @param persistedStateFlowMap Representation of the persisted state. Holds the same keys as [map].
 * @param syncState To save the temporary state, such that [persistedStateFlowMap] and [map] are in sync. Receives only [map] key-value pairs, whose values have changed with respect to the persisted state, held by [persistedStateFlowMap].
 * @param onStateSynced Possibility to invoke a callback upon the temporary and the persisted states having been synced. Receives contrarily to [syncState] the entire map.
 */
open class UnconfirmedStateFlowMap<K, V>(
    private val map: Map<K, MutableStateFlow<V>>,
    private val persistedStateFlowMap: Map<K, StateFlow<V>>,
    private val syncState: suspend (Map<K, V>) -> Unit,
    private val onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {}
) : MappedUnconfirmedState<K>(),
    Map<K, MutableStateFlow<V>> by map {

    companion object {
        fun <K, V> fromPersistedFlowMap(
            persistedFlowMap: Map<K, Flow<V>>,
            getDefaultValue: (K) -> V,
            coroutineScope: CoroutineScope,
            syncState: suspend (Map<K, V>) -> Unit,
            onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {}
        ): UnconfirmedStateFlowMap<K, V> =
            fromPersistedStateFlowMap(
                persistedStateFlowMap = persistedFlowMap.mapValues { (k, v) ->
                    v.stateIn(coroutineScope, SharingStarted.Eagerly, getDefaultValue(k))
                },
                syncState = syncState,
                onStateSynced = onStateSynced
            )

        fun <K, V> fromPersistedStateFlowMap(
            persistedStateFlowMap: Map<K, StateFlow<V>>,
            syncState: suspend (Map<K, V>) -> Unit,
            onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {}
        ): UnconfirmedStateFlowMap<K, V> =
            UnconfirmedStateFlowMap(
                map = persistedStateFlowMap.mapValues { (_, v) -> MutableStateFlow(v.value) },
                persistedStateFlowMap = persistedStateFlowMap,
                syncState = syncState,
                onStateSynced = onStateSynced
            )
    }

    // ==============
    // Modification
    // ==============

    operator fun set(key: K, value: V) {
        map.getValue(key).value = value

        when (value == persistedStateFlowMap.getValue(key).value) {
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
                map.getValue(it).value = persistedStateFlowMap.getValue(it).value
            }
        resetDissimilarityTrackers()
    }
}