@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.sharedpreferences.typedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class StringSetPreferences(
    vararg preferenceDefault: Pair<String, Set<String>>,
    sharedPreferences: SharedPreferences
) :
    TypedPreferences<Set<String>?>(
        *preferenceDefault,
        sharedPreferences = sharedPreferences
    ) {

    override fun SharedPreferences.writeValue(
        key: String,
        value: Set<String>?,
        synchronously: Boolean
    ) {
        edit(synchronously) {
            putStringSet(key, value)
        }
    }

    override fun SharedPreferences.getValue(key: String, defaultValue: Set<String>?): Set<String>? =
        getStringSet(key, defaultValue)
}