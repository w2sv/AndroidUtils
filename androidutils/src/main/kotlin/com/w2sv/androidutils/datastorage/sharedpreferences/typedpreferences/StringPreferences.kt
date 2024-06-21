@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.sharedpreferences.typedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class StringPreferences<T : String?>(
    vararg preferenceDefault: Pair<String, T>,
    sharedPreferences: SharedPreferences
) :
    TypedPreferences<T>(
        *preferenceDefault,
        sharedPreferences = sharedPreferences
    ) {

    override fun SharedPreferences.writeValue(key: String, value: T, synchronously: Boolean) {
        edit(synchronously) {
            putString(
                key,
                value
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun SharedPreferences.getValue(key: String, defaultValue: T): T =
        getString(
            key,
            defaultValue
        ) as T
}