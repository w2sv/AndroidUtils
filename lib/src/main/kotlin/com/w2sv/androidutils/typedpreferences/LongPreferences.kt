@file:Suppress("unused")

package com.w2sv.androidutils.typedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class LongPreferences(
    vararg preferenceDefault: Pair<String, Long>,
    sharedPreferences: SharedPreferences
) :
    TypedPreferences<Long>(
        *preferenceDefault,
        sharedPreferences = sharedPreferences
    ) {

    override fun SharedPreferences.writeValue(key: String, value: Long, synchronously: Boolean) {
        edit(synchronously) {
            putLong(key, value)
        }
    }

    override fun SharedPreferences.getValue(key: String, defaultValue: Long): Long =
        getLong(key, defaultValue)
}