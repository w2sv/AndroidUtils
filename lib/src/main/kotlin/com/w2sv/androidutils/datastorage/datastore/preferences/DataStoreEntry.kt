package com.w2sv.androidutils.datastorage.datastore.preferences

import android.net.Uri
import androidx.datastore.preferences.core.Preferences

/**
 * Interface for Classes, which are to be managed as map by a [AbstractPreferencesDataStoreRepository].
 */
interface DataStoreEntry<K, V> {
    val preferencesKey: Preferences.Key<K>
    val defaultValue: V

    interface UniType<T> : DataStoreEntry<T, T> {
        abstract class Impl<T>(
            override val preferencesKey: Preferences.Key<T>,
            override val defaultValue: T
        ) : UniType<T>
    }

    interface EnumValued<E : Enum<E>> : DataStoreEntry<Int, E> {
        abstract class Impl<E : Enum<E>>(
            override val preferencesKey: Preferences.Key<Int>,
            override val defaultValue: E
        ) : EnumValued<E>
    }

    interface UriValued : DataStoreEntry<String, Uri?> {
        abstract class Impl(
            override val preferencesKey: Preferences.Key<String>,
            override val defaultValue: Uri?
        ) : UriValued
    }
}