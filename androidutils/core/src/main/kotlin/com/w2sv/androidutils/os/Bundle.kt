@file:Suppress("unused")

package com.w2sv.androidutils.os

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import androidx.core.os.BundleCompat

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(name: String): T? =
    BundleCompat.getParcelable(this, name, T::class.java)

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompat(name: String): ArrayList<T>? =
    BundleCompat.getParcelableArrayList(this, name, T::class.java)

inline fun <reified T : Parcelable> Bundle.getSparseParcelableArrayCompat(name: String): SparseArray<T>? =
    BundleCompat.getSparseParcelableArray(this, name, T::class.java)

inline fun <reified T : Parcelable> Bundle.getParcelableArrayCompat(name: String): Array<out Parcelable>? =
    BundleCompat.getParcelableArray(this, name, T::class.java)

/**
 * For readable logging of the [Bundle] content.
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
