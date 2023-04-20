package com.w2sv.androidutils.datastorage.sharedpreferences.typedpreferences

import android.content.SharedPreferences
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import slimber.log.i

/**
 * Base for map delegator objects interfacing with [SharedPreferences] designed to reduce
 * number of IO Operations by reading from or writing to [SharedPreferences]
 */
abstract class TypedPreferences<T>(
    vararg preferenceDefault: Pair<String, T>,
    private val sharedPreferences: SharedPreferences
) : MutableMap<String, T> by mutableMapOf(*preferenceDefault),
    DefaultLifecycleObserver {

    /**
     * Keep track of values having changed since last call to [writeChangedValues]
     */
    private val lastDiscSyncState: MutableMap<String, T>

    init {
        forEach { (key, defaultValue) ->
            put(key, sharedPreferences.getValue(key, defaultValue))
        }
        lastDiscSyncState = toMutableMap()

        i { "Initialized ${javaClass.name}: $this" }
    }

    /**
     * Write diff of this & [lastDiscSyncState] to [sharedPreferences] and update [lastDiscSyncState]
     */
    fun writeChangedValues(synchronously: Boolean = false) =
        entries
            .filter { lastDiscSyncState.getValue(it.key) != it.value }
            .forEach {
                sharedPreferences.writeValue(it.key, it.value, synchronously)
                i { "Wrote ${it.key}=${it.value} to shared preferences" }

                lastDiscSyncState[it.key] = it.value
            }

    /**
     * Type-specific value writing to [SharedPreferences]
     */
    protected abstract fun SharedPreferences.writeValue(
        key: String,
        value: T,
        synchronously: Boolean
    )

    /**
     * Type-specific value fetching from [SharedPreferences]
     */
    protected abstract fun SharedPreferences.getValue(key: String, defaultValue: T): T

    /**
     * [writeChangedValues] in [onPause] of [LifecycleOwner], if registered as lifecycle observer.
     *
     * [onPause] is selected here to guarantee for a synchronization taking place even if the app
     * is exited via the home button and subsequently killed by removing it from the 'recent'
     * overview
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        writeChangedValues()
    }
}