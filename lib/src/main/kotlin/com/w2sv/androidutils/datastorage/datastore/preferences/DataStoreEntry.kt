@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.datastore.preferences

import android.net.Uri
import androidx.datastore.preferences.core.Preferences
import java.time.LocalDateTime

/**
 * Interfaces for classes which are to be managed as map by a [PreferencesDataStoreRepository].
 */
interface DataStoreEntry<K, V> {
    val preferencesKey: Preferences.Key<K>
    val defaultValue: V

    interface UniType<T> : DataStoreEntry<T, T> {
        data class Impl<T>(
            override val preferencesKey: Preferences.Key<T>,
            override val defaultValue: T
        ) : UniType<T>
    }

    interface EnumValued<E : Enum<E>> : DataStoreEntry<Int, E> {
        data class Impl<E : Enum<E>>(
            override val preferencesKey: Preferences.Key<Int>,
            override val defaultValue: E
        ) : EnumValued<E>
    }

    interface UriValued : DataStoreEntry<String, Uri?> {
        data class Impl(
            override val preferencesKey: Preferences.Key<String>,
            override val defaultValue: Uri?
        ) : UriValued
    }

    interface LocalDateTimeValued : DataStoreEntry<String, LocalDateTime?> {
        data class Impl(
            override val preferencesKey: Preferences.Key<String>,
            override val defaultValue: LocalDateTime?
        ) : LocalDateTimeValued
    }
}