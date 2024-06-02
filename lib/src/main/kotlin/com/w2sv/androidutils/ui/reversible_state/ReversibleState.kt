package com.w2sv.androidutils.ui.reversible_state

import kotlinx.coroutines.flow.StateFlow

interface ReversibleState {
    val statesDissimilar: StateFlow<Boolean>

    suspend fun sync()
    fun reset()
}