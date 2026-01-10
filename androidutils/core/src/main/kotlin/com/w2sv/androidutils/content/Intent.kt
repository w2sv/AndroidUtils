@file:Suppress("unused")

package com.w2sv.androidutils.content

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.content.IntentCompat
import com.w2sv.androidutils.os.toMapString

inline fun <reified T> intent(context: Context): Intent =
    Intent(context, T::class.java)

/**
 * @see [Intent.makeRestartActivityTask]
 */
inline fun <reified T> restartActivityTaskIntent(context: Context): Intent =
    Intent.makeRestartActivityTask(
        ComponentName(
            context,
            T::class.java
        )
    )

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
        if (equals(defaultValue)) {
            null
        } else {
            this
        }
    }

fun Intent.logString(): String =
    buildString {
        append("Action=$action")
        if (flags != 0) {
            append(" | Flags=$flags")
        }
        if (categories?.isNotEmpty() == true) {
            append(" | Categories=$categories")
        }
        if (extras?.isEmpty == false) {
            append(" | Extras=${extras?.toMapString()}")
        }
    }
