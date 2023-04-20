@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.postValue(value: T?) {
    asMutable.postValue(value)
}

fun <T> LiveData<T>.repostValue() {
    asMutable.postValue(value)
}

private val <T> LiveData<T>.asMutable: MutableLiveData<T>
    get() = this as MutableLiveData<T>

// ===========
// Boolean
// ===========

fun LiveData<Boolean>.toggle() {
    postValue(!value!!)
}

// ===========
// Int
// ===========

fun LiveData<Int>.increment() {
    postValue(value!! + 1)
}

fun LiveData<Int>.decrement() {
    postValue(value!! - 1)
}