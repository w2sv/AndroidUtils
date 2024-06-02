package com.w2sv.androidutils.ui.reversible_state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Base class for classes, encapsulating states being displayed by the UI but pending
 * confirmation, which in turn triggers the synchronization with the respective repository.
 */
abstract class AbstractReversibleState : ReversibleState {
    override val statesDissimilar: StateFlow<Boolean>
        get() = _statesDissimilar
    protected val _statesDissimilar = MutableStateFlow(false)

    protected val logIdentifier: String
        get() = this::class.java.simpleName
}