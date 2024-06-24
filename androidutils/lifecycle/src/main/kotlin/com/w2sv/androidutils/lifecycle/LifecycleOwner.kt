@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.addObservers(observers: Iterable<LifecycleObserver>) {
    observers.forEach(lifecycle::addObserver)
}