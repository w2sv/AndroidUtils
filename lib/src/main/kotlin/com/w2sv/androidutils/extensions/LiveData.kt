@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Boolean
 */

fun LiveData<Boolean>.toggle() {
    postValue(!value!!)
}

/**
 * Int
 */

fun LiveData<Int>.increment(){
    postValue(value!! + 1)
}

fun LiveData<Int>.decrement(){
    postValue(value!! - 1)
}

/**
 * Generic
 */

fun <T> LiveData<T>.postValue(value: T?) {
    asMutable.postValue(value)
}

private val <T> LiveData<T>.asMutable: MutableLiveData<T>
    get() = this as MutableLiveData<T>