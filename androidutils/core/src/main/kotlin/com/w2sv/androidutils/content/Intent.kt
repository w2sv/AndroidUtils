@file:Suppress("unused")

package com.w2sv.androidutils.content

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.content.IntentCompat
import com.w2sv.androidutils.os.toMapString

/**
 * Creates a [ComponentName] for the given component type [T].
 *
 * This provides a type-safe and concise way to reference components
 * (e.g. Activities, Services, Receivers) without manually passing a `Class`.
 */
inline fun <reified T> componentName(context: Context): ComponentName =
    ComponentName(context, T::class.java)

/**
 * Creates an explicit [Intent] targeting the component of type [T].
 *
 * This is a type-safe convenience for creating explicit intents without
 * manually referencing the component class.
 */
inline fun <reified T> intent(context: Context): Intent =
    Intent(context, T::class.java)

inline fun <reified T : Parcelable> Intent.getParcelableCompat(name: String): T? =
    IntentCompat.getParcelableExtra(this, name, T::class.java)

inline fun <reified T : Parcelable> Intent.getParcelableArrayListCompat(name: String): ArrayList<T>? =
    IntentCompat.getParcelableArrayListExtra(this, name, T::class.java)

inline fun <reified T : Parcelable> Intent.getParcelableArrayCompat(name: String): Array<out Parcelable>? =
    IntentCompat.getParcelableArrayExtra(this, name, T::class.java)

/**
 * Returns null instead of [defaultValue] for more convenient handling through
 * built-in null handling mechanisms.
 *
 * @see Intent.getIntExtra
 */
fun Intent.getIntExtraOrNull(name: String, defaultValue: Int): Int? =
    getIntExtra(name, defaultValue).run {
        if (equals(defaultValue)) {
            null
        } else {
            this
        }
    }

/**
 * Returns a compact, human-readable string representation of this [Intent]
 * suitable for logging or debugging.
 *
 * Includes:
 * - [Intent.action]
 * - [Intent.flags] (if not 0)
 * - [Intent.categories] (if not empty)
 * - [Intent.data] (URI)
 * - [Intent.type] (MIME type)
 * - [Intent.component] (explicit target)
 * - [Intent.package] (target package, if set)
 * - [Intent.selector] (if set)
 * - [Intent.extras] (if not empty, converted via [toMapString])
 *
 * Empty or default values are omitted for conciseness.
 */
fun Intent.logString(): String =
    buildString {
        append("Action=$action")
        if (data != null) append(" | Data=$data")
        if (type != null) append(" | Type=$type")
        if (flags != 0) append(" | Flags=$flags")
        if (categories?.isNotEmpty() == true) append(" | Categories=$categories")
        if (component != null) append(" | Component=$component")
        if (`package` != null) append(" | Package=${`package`}")
        if (selector != null) append(" | Selector=${selector.logString()}")
        if (extras?.isEmpty == false) append(" | Extras=${extras?.toMapString()}")
    }
