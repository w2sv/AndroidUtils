@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.sharedpreferences.typedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class FloatPreferences(
    vararg preferenceDefault: Pair<String, Float>,
    sharedPreferences: SharedPreferences
) :
    TypedPreferences<Float>(
        *preferenceDefault,
        sharedPreferences = sharedPreferences
    ) {

    override fun SharedPreferences.writeValue(key: String, value: Float, synchronously: Boolean) {
        edit(synchronously) {
            putFloat(key, value)
        }
    }

    override fun SharedPreferences.getValue(key: String, defaultValue: Float): Float =
        getFloat(key, defaultValue)
}