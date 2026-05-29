@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

@MainThread
fun LifecycleOwner.addObservers(observers: Iterable<LifecycleObserver>) {
    observers.forEach(lifecycle::addObserver)
}
