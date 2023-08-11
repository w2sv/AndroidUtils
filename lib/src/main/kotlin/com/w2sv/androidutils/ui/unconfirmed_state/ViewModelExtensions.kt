package com.w2sv.androidutils.ui.unconfirmed_state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow

fun <K, V> ViewModel.getUnconfirmedStateMap(
    appliedFlowMap: Map<K, Flow<V>>,
    makeSynchronousMutableMap: (Map<K, Flow<V>>) -> MutableMap<K, V>,
    syncState: suspend (Map<K, V>) -> Unit
): UnconfirmedStateMap<K, V> =
    UnconfirmedStateMap(
        coroutineScope = viewModelScope,
        appliedFlowMap = appliedFlowMap,
        makeSynchronousMutableMap = makeSynchronousMutableMap,
        syncState = syncState
    )

fun <T> ViewModel.getUnconfirmedStateFlow(
    appliedFlow: Flow<T>,
    syncState: suspend (T) -> Unit
): UnconfirmedStateFlow<T> =
    UnconfirmedStateFlow(
        coroutineScope = viewModelScope,
        appliedFlow = appliedFlow,
        syncState = syncState
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