package com.w2sv.androidutils.ui.reversible_state

abstract class MappedReversibleState<K, V> : AbstractReversibleState() {

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