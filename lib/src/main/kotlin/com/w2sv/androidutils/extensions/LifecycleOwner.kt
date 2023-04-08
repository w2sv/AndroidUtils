@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.addObservers(observers: Iterable<LifecycleObserver>){
    observers.forEach(lifecycle::addObserver)
}