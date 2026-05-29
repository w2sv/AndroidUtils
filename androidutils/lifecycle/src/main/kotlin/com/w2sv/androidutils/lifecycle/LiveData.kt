@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.lifecycle.MutableLiveData

/**
 * Posts the current value again.
 *
 * This gives observers a simple re-notification hook without manually reading
 * and passing [MutableLiveData.getValue] back to [MutableLiveData.postValue].
 */
fun <T> MutableLiveData<T>.repostValue() {
    postValue(value)
}

// ===========
// Boolean
// ===========

/**
 * Toggles a nullable boolean live data value.
 *
 * This avoids repeating the read, negate, and [MutableLiveData.postValue] steps
 * for boolean UI state.
 */
fun MutableLiveData<Boolean>.toggle() {
    postValue(value?.let { !it })
}

// ===========
// Int
// ===========

/**
 * Increments a nullable integer live data value.
 *
 * This avoids repeating the read, arithmetic, and [MutableLiveData.postValue]
 * steps for counters.
 */
fun MutableLiveData<Int>.increment() {
    postValue(value?.let { it + 1 })
}

/**
 * Decrements a nullable integer live data value.
 *
 * This avoids repeating the read, arithmetic, and [MutableLiveData.postValue]
 * steps for counters.
 */
fun MutableLiveData<Int>.decrement() {
    postValue(value?.let { it - 1 })
}
