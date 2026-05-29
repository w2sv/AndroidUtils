@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Adds all [observers] to this owner's lifecycle.
 *
 * This replaces repeated [androidx.lifecycle.Lifecycle.addObserver] calls when a
 * screen wires up multiple observers at once.
 */
@MainThread
fun LifecycleOwner.addObservers(observers: Iterable<LifecycleObserver>) {
    observers.forEach(lifecycle::addObserver)
}
