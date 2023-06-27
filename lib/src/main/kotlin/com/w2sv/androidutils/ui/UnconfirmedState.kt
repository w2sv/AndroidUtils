package com.w2sv.androidutils.ui

import android.net.Uri
import androidx.datastore.preferences.core.Preferences
import com.w2sv.androidutils.coroutines.getSynchronousMap
import com.w2sv.androidutils.coroutines.getValueSynchronously
import com.w2sv.androidutils.datastorage.datastore.preferences.AbstractPreferencesDataStoreRepository
import com.w2sv.androidutils.datastorage.datastore.preferences.DataStoreEntry
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

open class UnconfirmedStateMap<DSE : DataStoreEntry<*, V>, V>(
    private val coroutineScope: CoroutineScope,
    private val appliedFlowMap: Map<DSE, Flow<V>>,
    private val map: MutableMap<DSE, V> = appliedFlowMap
        .getSynchronousMap()
        .toMutableMap(),
    private val syncState: suspend (Map<DSE, V>) -> Unit
) : UnconfirmedState<Map<DSE, V>>(),
    MutableMap<DSE, V> by map {

    /**
     * Tracking of keys which correspond to values, differing between [appliedFlowMap] and this
     * for efficient syncing/resetting.
     */
    private val dissimilarKeys = mutableSetOf<DSE>()

    // ==============
    // Modification
    // ==============

    /**
     * Inherently updates [dissimilarKeys] and [statesDissimilar] in an asynchronous fashion.
     */
    override fun put(key: DSE, value: V): V? =
        map.put(key, value)
            .also {
                coroutineScope.launch {
                    when (value == appliedFlowMap.getValue(key).first()) {
                        true -> dissimilarKeys.remove(key)
                        false -> dissimilarKeys.add(key)
                    }
                    _statesDissimilar.value = dissimilarKeys.isNotEmpty()
                }
            }

    override fun putAll(from: Map<out DSE, V>) {
        from.forEach { (k, v) ->
            put(k, v)
        }
    }

    // =======================
    // Syncing / Resetting
    // =======================

    override suspend fun sync() = withSubsequentInternalReset {
        i { "Syncing $logIdentifier" }

        syncState(filterKeys { it in dissimilarKeys })
    }

    override suspend fun reset() = withSubsequentInternalReset {
        dissimilarKeys
            .forEach {
                // Call map.put directly to prevent unnecessary state updates
                map[it] = appliedFlowMap.getValue(it).first()
            }
    }

    private inline fun withSubsequentInternalReset(f: () -> Unit) {
        f()

        dissimilarKeys.clear()
        _statesDissimilar.value = false
    }
}

open class UnconfirmedStateFlow<T>(
    coroutineScope: CoroutineScope,
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
    private val onStateSynced: () -> Unit = {}
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

abstract class PreferencesDataStoreBackedUnconfirmedStatesViewModel<R : AbstractPreferencesDataStoreRepository>(
    dataStoreRepository: R
) : AbstractPreferencesDataStoreRepository.ViewModel<R>(dataStoreRepository) {

    // =======================
    // Instance creation
    // =======================

    fun <K : DataStoreEntry.UniType<V>, V> makeUnconfirmedStateMap(
        appliedFlowMap: Map<K, Flow<V>>,
        makeMutableMap: (Map<K, Flow<V>>) -> MutableMap<K, V> = {
            it.getSynchronousMap().toMutableMap()
        },
        onStateSynced: () -> Unit = {}
    ): UnconfirmedStateMap<K, V> =
        UnconfirmedStateMap(
            coroutineScope = coroutineScope,
            appliedFlowMap = appliedFlowMap,
            map = makeMutableMap(appliedFlowMap),
            syncState = {
                dataStoreRepository.saveMap(it)
                onStateSynced()
            }
        )

    fun <K : DataStoreEntry.EnumValued<V>, V : Enum<V>> makeUnconfirmedEnumValuedStateMap(
        appliedFlowMap: Map<K, Flow<V>>,
        makeMutableMap: (Map<K, Flow<V>>) -> MutableMap<K, V> = {
            it.getSynchronousMap().toMutableMap()
        },
        onStateSynced: () -> Unit = {}
    ): UnconfirmedStateMap<K, V> =
        UnconfirmedStateMap(
            coroutineScope = coroutineScope,
            appliedFlowMap = appliedFlowMap,
            map = makeMutableMap(appliedFlowMap),
            syncState = {
                dataStoreRepository.saveEnumValuedMap(it)
                onStateSynced()
            }
        )

    fun <DSE : DataStoreEntry.UriValued> makeUnconfirmedUriValuedStateMap(
        appliedFlowMap: Map<DSE, Flow<Uri?>>,
        makeMutableMap: (Map<DSE, Flow<Uri?>>) -> MutableMap<DSE, Uri?> = {
            it.getSynchronousMap().toMutableMap()
        },
        onStateSynced: () -> Unit = {}
    ): UnconfirmedStateMap<DSE, Uri?> =
        UnconfirmedStateMap(
            coroutineScope = coroutineScope,
            appliedFlowMap = appliedFlowMap,
            map = makeMutableMap(appliedFlowMap),
            syncState = {
                dataStoreRepository.saveUriValuedMap(it)
                onStateSynced()
            }
        )

    fun <T> makeUnconfirmedStateFlow(
        appliedFlow: Flow<T>,
        preferencesKey: Preferences.Key<T>,
        onStateSynced: () -> Unit = {}
    ): UnconfirmedStateFlow<T> =
        UnconfirmedStateFlow(coroutineScope, appliedFlow) {
            dataStoreRepository.save(preferencesKey, it)
            onStateSynced()
        }

    inline fun <reified T : Enum<T>> makeUnconfirmedEnumValuedStateFlow(
        appliedFlow: Flow<T>,
        preferencesKey: Preferences.Key<Int>,
        crossinline onStateSynced: () -> Unit = {}
    ): UnconfirmedStateFlow<T> =
        UnconfirmedStateFlow(coroutineScope, appliedFlow) {
            dataStoreRepository.save(preferencesKey, it)
            onStateSynced()
        }

    fun makeUnconfirmedUriValuedStateFlow(
        appliedFlow: Flow<Uri?>,
        preferencesKey: Preferences.Key<String>,
        onStateSynced: () -> Unit = {}
    ): UnconfirmedStateFlow<Uri?> =
        UnconfirmedStateFlow(coroutineScope, appliedFlow) {
            dataStoreRepository.save(preferencesKey, it)
            onStateSynced()
        }

    fun makeUnconfirmedStatesComposition(
        unconfirmedStates: UnconfirmedStates,
        onStateSynced: () -> Unit = {}
    ): UnconfirmedStatesComposition =
        UnconfirmedStatesComposition(
            unconfirmedStates,
            coroutineScope = coroutineScope,
            onStateSynced = onStateSynced
        )

    // =======================
    // Syncing / resetting on coroutine scope
    // =======================

    fun UnconfirmedState<*>.launchSync(): Job =
        coroutineScope.launch {
            sync()
        }

    fun UnconfirmedState<*>.launchReset(): Job =
        coroutineScope.launch {
            reset()
        }
}