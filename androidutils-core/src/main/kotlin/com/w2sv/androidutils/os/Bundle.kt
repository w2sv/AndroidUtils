@file:Suppress("unused")

package com.w2sv.androidutils.os

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import androidx.core.os.BundleCompat

/**
 * Reads a typed [Parcelable] from this [Bundle].
 *
 * This delegates to [BundleCompat] so callers get a single API that handles the
 * typed Android 13+ overload and older platform versions.
 */
inline fun <reified T : Parcelable> Bundle.getParcelableCompat(name: String): T? =
    BundleCompat.getParcelable(this, name, T::class.java)

/**
 * Reads a typed parcelable array list from this [Bundle].
 *
 * This delegates to [BundleCompat] so callers do not need SDK-version branches
 * around parcelable extra APIs.
 */
inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompat(name: String): ArrayList<T>? =
    BundleCompat.getParcelableArrayList(this, name, T::class.java)

/**
 * Reads a typed sparse parcelable array from this [Bundle].
 *
 * This delegates to [BundleCompat] so callers avoid deprecated platform APIs on
 * older Android versions.
 */
inline fun <reified T : Parcelable> Bundle.getSparseParcelableArrayCompat(name: String): SparseArray<T>? =
    BundleCompat.getSparseParcelableArray(this, name, T::class.java)

/**
 * Reads a typed parcelable array from this [Bundle].
 *
 * This delegates to [BundleCompat] and keeps type lookup concise with a reified
 * type parameter.
 */
inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(name: String): Array<out Parcelable>? =
    BundleCompat.getParcelableArray(this, name, T::class.java)

/**
 * Returns a readable representation of this [Bundle]'s contents.
 *
 * This expands nested bundles and primitive arrays, which makes log output more
 * useful than Android's default object-oriented bundle string.
 */
@Suppress("DEPRECATION")
fun Bundle.toMapString(): String =
    keySet().joinToString(prefix = "{", postfix = "}") { key ->
        "$key=${
            get(key).run {
                when (this) {
                    is Bundle -> toMapString()
                    is Array<*> -> toList()
                    is IntArray -> toList()
                    is LongArray -> toList()
                    is FloatArray -> toList()
                    is DoubleArray -> toList()
                    is BooleanArray -> toList()
                    else -> this
                }
            }
        }"
    }
