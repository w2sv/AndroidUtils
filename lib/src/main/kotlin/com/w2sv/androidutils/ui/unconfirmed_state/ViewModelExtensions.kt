package com.w2sv.androidutils.ui.unconfirmed_state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

fun <T> ViewModel.getUnconfirmedStateFlow(
    appliedFlow: Flow<T>,
    getDefaultValue: () -> T,
    syncState: suspend (T) -> Unit
): UnconfirmedStateFlow<T> =
    UnconfirmedStateFlow(
        coroutineScope = viewModelScope,
        appliedFlow = appliedFlow,
        getDefaultValue = getDefaultValue,
        syncState = syncState
    )

fun <K, V> ViewModel.getUnconfirmedStateMap(
    appliedFlowMap: Map<K, Flow<V>>,
    getDefaultValue: (K) -> V,
    makeMap: (Map<K, Flow<V>>) -> MutableMap<K, V>,
    syncState: suspend (Map<K, V>) -> Unit,
    onStateSynced: suspend (Map<K, V>) -> Unit = {}
): UnconfirmedStateMap<K, V> =
    UnconfirmedStateMap(
        coroutineScope = viewModelScope,
        appliedFlowMap = appliedFlowMap,
        getDefaultValue = getDefaultValue,
        makeMap = makeMap,
        syncState = syncState,
        onStateSynced = onStateSynced
    )

fun <K, V> ViewModel.getUnconfirmedStateFlowMap(
    appliedFlowMap: Map<K, Flow<V>>,
    getDefaultValue: (K) -> V,
    syncState: suspend (Map<K, V>) -> Unit,
    onStateSynced: suspend (Map<K, StateFlow<V>>) -> Unit = {}
): UnconfirmedStateFlowMap<K, V> =
    UnconfirmedStateFlowMap.fromAppliedFlowMap(
        appliedFlowMap = appliedFlowMap,
        getDefaultValue = getDefaultValue,
        coroutineScope = viewModelScope,
        syncState = syncState,
        onStateSynced = onStateSynced
    )

fun ViewModel.getUnconfirmedStatesComposition(
    unconfirmedStates: UnconfirmedStates,
    onStateSynced: suspend () -> Unit = {}
): UnconfirmedStatesComposition =
    UnconfirmedStatesComposition(
        unconfirmedStates = unconfirmedStates,
        coroutineScope = viewModelScope,
        onStateSynced = onStateSynced
    )