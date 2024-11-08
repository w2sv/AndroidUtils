@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.repostValue() {
    postValue(value)
}

// ===========
// Boolean
// ===========

fun MutableLiveData<Boolean>.toggle() {
    postValue(value?.let { !it })
}

// ===========
// Int
// ===========

fun MutableLiveData<Int>.increment() {
    postValue(value?.let { it + 1 })
}

fun MutableLiveData<Int>.decrement() {
    postValue(value?.let { it - 1 })
}
