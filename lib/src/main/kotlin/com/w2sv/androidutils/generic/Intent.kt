@file:Suppress("unused")

package com.w2sv.androidutils.generic

import android.content.Intent
import android.os.Parcelable
import androidx.core.content.IntentCompat

inline fun <reified T : Parcelable> Intent.getParcelableCompat(name: String): T? =
    IntentCompat.getParcelableExtra(this, name, T::class.java)

inline fun <reified T : Parcelable> Intent.getParcelableArrayListCompat(name: String): ArrayList<T>? =
    IntentCompat.getParcelableArrayListExtra(this, name, T::class.java)

inline fun <reified T : Parcelable> Intent.getParcelableArrayCompat(name: String): Array<out Parcelable>? =
    IntentCompat.getParcelableArrayExtra(this, name, T::class.java)

/**
 * Returns null instead of [defaultValue] for more convenient handling through
 * built-in null handling mechanisms.
 */
fun Intent.getIntExtraOrNull(name: String, defaultValue: Int): Int? =
    getIntExtra(name, defaultValue).run {
        if (equals(defaultValue))
            null
        else
            this
    }