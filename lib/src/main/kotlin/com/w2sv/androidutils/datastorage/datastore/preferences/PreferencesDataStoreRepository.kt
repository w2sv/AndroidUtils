@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.datastore.preferences

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.w2sv.kotlinutils.extensions.getByOrdinal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import slimber.log.i

abstract class PreferencesDataStoreRepository(
    val dataStore: DataStore<Preferences>
) {

    // ================
    // Plain values
    // ================

    fun <T> getFlow(preferencesKey: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataStore.data.map {
            it[preferencesKey] ?: defaultValue
        }

    fun <T> getFlow(entry: DataStoreEntry<T, T>): Flow<T> =
        getFlow(entry.preferencesKey, entry.defaultValue)

    suspend fun <T> save(preferencesKey: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it.save(preferencesKey, value)
        }
    }

    // ================
    // URIs
    // ================

    fun getUriFlow(
        preferencesKey: Preferences.Key<String>,
        defaultValue: Uri?
    ): Flow<Uri?> =
        dataStore.data.map {
            it[preferencesKey]?.let { string ->
                if (string == DEFAULT_STRING_VALUE)
                    null
                else
                    Uri.parse(string)
            }
                ?: defaultValue
        }

    fun getUriFlow(entry: DataStoreEntry.UriValued): Flow<Uri?> =
        getUriFlow(entry.preferencesKey, entry.defaultValue)

    suspend fun save(preferencesKey: Preferences.Key<String>, value: Uri?) {
        dataStore.edit {
            it.save(preferencesKey, value)
        }
    }

    // ============
    // Enums
    // ============

    inline fun <reified E : Enum<E>> getEnumFlow(
        preferencesKey: Preferences.Key<Int>,
        defaultValue: E
    ): Flow<E> =
        dataStore.data.map {
            it[preferencesKey]
                ?.let { ordinal -> getByOrdinal<E>(ordinal) }
                ?: defaultValue
        }

    inline fun <reified E : Enum<E>> getEnumFlow(
        entry: DataStoreEntry.EnumValued<E>
    ): Flow<E> =
        getEnumFlow(entry.preferencesKey, entry.defaultValue)

    suspend fun save(
        preferencesKey: Preferences.Key<Int>,
        value: Enum<*>
    ) {
        dataStore.edit {
            it.save(preferencesKey, value)
        }
    }

    // ============
    // Simple Maps
    // ============

    fun <DSE : DataStoreEntry.UniType<V>, V> getFlowMap(properties: Iterable<DSE>): Map<DSE, Flow<V>> =
        properties.associateWith { property ->
            getFlow(property.preferencesKey, property.defaultValue)
        }

    suspend fun <DSE : DataStoreEntry.UniType<V>, V> saveMap(
        map: Map<DSE, V>
    ) {
        dataStore.edit {
            map.forEach { (entry, value) ->
                it.save(entry.preferencesKey, value)
            }
        }
    }

    // ============
    // UriValued Maps
    // ============

    fun <DSE : DataStoreEntry.UriValued> getUriFlowMap(entries: Iterable<DSE>): Map<DSE, Flow<Uri?>> =
        entries.associateWith {
            getUriFlow(it.preferencesKey, it.defaultValue)
        }

    suspend fun <DSE : DataStoreEntry.UriValued> saveUriValuedMap(map: Map<DSE, Uri?>) {
        dataStore.edit {
            map.forEach { (entry, value) ->
                it.save(entry.preferencesKey, value)
            }
        }
    }

    // ============
    // EnumValued Maps
    // ============

    inline fun <DSE : DataStoreEntry.EnumValued<V>, reified V : Enum<V>> getEnumValuedFlowMap(
        properties: Iterable<DSE>
    ): Map<DSE, Flow<V>> =
        properties.associateWith { property ->
            getEnumFlow(property.preferencesKey, property.defaultValue)
        }

    suspend fun <DSE : DataStoreEntry.EnumValued<V>, V : Enum<V>> saveEnumValuedMap(
        map: Map<DSE, V>
    ) {
        dataStore.edit {
            map.forEach { (entry, value) ->
                it.save(entry.preferencesKey, value)
            }
        }
    }

    /**
     * Interface for classes interfacing with a [PreferencesDataStoreRepository] via a held [coroutineScope].
     */
    interface Interface {
        val repository: PreferencesDataStoreRepository
        val coroutineScope: CoroutineScope

        fun <T> saveToDataStore(key: Preferences.Key<T>, value: T): Job =
            coroutineScope.launch(Dispatchers.IO) {
                repository.save(key, value)
            }

        fun saveToDataStore(key: Preferences.Key<String>, value: Uri?): Job =
            coroutineScope.launch(Dispatchers.IO) {
                repository.save(key, value)
            }

        fun <E : Enum<E>> saveToDataStore(key: Preferences.Key<Int>, value: E): Job =
            coroutineScope.launch(Dispatchers.IO) {
                repository.save(key, value)
            }

        fun <DSE : DataStoreEntry.UniType<V>, V> saveMapToDataStore(
            map: Map<DSE, V>
        ): Job =
            coroutineScope.launch(Dispatchers.IO) {
                repository.saveMap(map)
            }

        fun <DSE : DataStoreEntry.EnumValued<V>, V : Enum<V>> saveEnumValuedMapToDataStore(
            map: Map<DSE, V>
        ): Job =
            coroutineScope.launch(Dispatchers.IO) {
                repository.saveEnumValuedMap(map)
            }

        fun <DSE : DataStoreEntry.UriValued> saveUriValuedMapToDataStore(
            map: Map<DSE, Uri?>
        ): Job =
            coroutineScope.launch(Dispatchers.IO) {
                repository.saveUriValuedMap(map)
            }
    }

    abstract class ViewModel<R : PreferencesDataStoreRepository>(override val repository: R) :
        androidx.lifecycle.ViewModel(),
        Interface {

        override val coroutineScope: CoroutineScope by ::viewModelScope
    }
}

private const val DEFAULT_STRING_VALUE = ""

private fun <T> MutablePreferences.save(preferencesKey: Preferences.Key<T>, value: T) {
    this[preferencesKey] = value
    i { "Saved ${preferencesKey.name}=$value" }
}

private fun MutablePreferences.save(preferencesKey: Preferences.Key<String>, value: Uri?) {
    save(preferencesKey, value?.toString() ?: DEFAULT_STRING_VALUE)
}

private fun MutablePreferences.save(preferencesKey: Preferences.Key<Int>, value: Enum<*>) {
    save(preferencesKey, value.ordinal)
}