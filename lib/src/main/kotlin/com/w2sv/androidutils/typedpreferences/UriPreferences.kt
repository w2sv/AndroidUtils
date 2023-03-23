@file:Suppress("unused")

package com.w2sv.androidutils.typedpreferences

import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.edit

abstract class UriPreferences<T : Uri?>(
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
                value?.toString()
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun SharedPreferences.getValue(key: String, defaultValue: T): T =
        getString(
            key,
            defaultValue?.toString()
        )
            ?.run { Uri.parse(this) } as T
}