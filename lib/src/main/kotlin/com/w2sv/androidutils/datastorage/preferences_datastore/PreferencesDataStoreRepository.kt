@file:Suppress("unused")

package com.w2sv.androidutils.datastorage.preferences_datastore

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.w2sv.kotlinutils.extensions.getByOrdinal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import slimber.log.i
import java.time.LocalDateTime

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

    fun <T> getNullableFlow(preferencesKey: Preferences.Key<T>, defaultValue: T?): Flow<T?> =
        dataStore.data.map {
            it[preferencesKey] ?: defaultValue
        }

    fun <T> getFlow(entry: DataStoreEntry<T, T>): Flow<T> =
        getFlow(entry.preferencesKey, entry.defaultValue)

    suspend fun <T> save(preferencesKey: Preferences.Key<T>, value: T) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.save(preferencesKey, value)
            }
        }
    }

    suspend fun <T> saveNullable(preferencesKey: Preferences.Key<T?>, value: T?) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.save(preferencesKey, value)
            }
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

    // ================
    // LocalDateTime
    // ================

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocalDateTimeFlow(
        preferencesKey: Preferences.Key<String>,
        defaultValue: LocalDateTime?
    ): Flow<LocalDateTime?> =
        dataStore.data.map {
            it[preferencesKey]?.let { string ->
                if (string == DEFAULT_STRING_VALUE)
                    null
                else
                    LocalDateTime.parse(string)
            }
                ?: defaultValue
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocalDateTimeFlow(entry: DataStoreEntry.LocalDateTimeValued): Flow<LocalDateTime?> =
        getLocalDateTimeFlow(entry.preferencesKey, entry.defaultValue)

    suspend fun <T> saveStringRepresentation(preferencesKey: Preferences.Key<String>, value: T?) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.saveStringRepresentation(preferencesKey, value)
            }
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
                ?.let { ordinal ->
                    try {
                        getByOrdinal<E>(ordinal)
                    } catch (e: IndexOutOfBoundsException) {
                        defaultValue
                    }
                }
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
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.save(preferencesKey, value.ordinal)
            }
        }
    }

    // ============
    // Simple Maps
    // ============

    fun <DSE : DataStoreEntry.UniType<V>, V> getFlowMap(properties: Iterable<DSE>): Map<DSE, Flow<V>> =
        properties.associateWith { property ->
            getFlow(property.preferencesKey, property.defaultValue)
        }

    fun <DSE : DataStoreEntry.UniType<V>, V> getStateFlowMap(
        properties: Iterable<DSE>,
        scope: CoroutineScope,
        sharingStarted: SharingStarted
    ): Map<DSE, StateFlow<V>> =
        properties.associateWith { property ->
            getFlow(
                preferencesKey = property.preferencesKey,
                defaultValue = property.defaultValue
            )
                .stateIn(
                    scope = scope,
                    started = sharingStarted,
                    initialValue = property.defaultValue
                )
        }

    suspend fun <DSE : DataStoreEntry.UniType<V>, V> saveMap(
        map: Map<DSE, V>
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                map.forEach { (entry, value) ->
                    it.save(entry.preferencesKey, value)
                }
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

    suspend fun <DSE : DataStoreEntry.UriValued> saveStringRepresentations(map: Map<DSE, Any?>) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                map.forEach { (entry, value) ->
                    it.saveStringRepresentation(entry.preferencesKey, value)
                }
            }
        }
    }

    // ============
    // LocalDateTime Maps
    // ============

    @RequiresApi(Build.VERSION_CODES.O)
    fun <DSE : DataStoreEntry.LocalDateTimeValued> getLocalDateTimeFlowMap(entries: Iterable<DSE>): Map<DSE, Flow<LocalDateTime?>> =
        entries.associateWith {
            getLocalDateTimeFlow(it.preferencesKey, it.defaultValue)
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
        withContext(Dispatchers.IO) {
            dataStore.edit {
                map.forEach { (entry, value) ->
                    it.save(entry.preferencesKey, value.ordinal)
                }
            }
        }
    }

    // ============
    // DataStoreFlow
    // ============

    protected fun <T> dataStoreFlow(
        key: Preferences.Key<T>,
        default: T
    ): DataStoreFlow<T> =
        DataStoreFlow(
            default = default,
            flow = getFlow(key, default),
            save = { save(key, it) }
        )

    protected inline fun <reified E : Enum<E>> dataStoreFlow(
        key: Preferences.Key<Int>,
        default: E
    ): DataStoreFlow<E> =
        DataStoreFlow(
            default = default,
            flow = getEnumFlow<E>(key, default),
            save = { save(key, it) }
        )

    protected fun dataStoreUriFlow(
        key: Preferences.Key<String>,
        default: Uri?
    ): DataStoreFlow<Uri?> =
        DataStoreFlow(
            default = default,
            flow = getUriFlow(key, default),
            save = { saveStringRepresentation(key, it) }
        )

    @RequiresApi(Build.VERSION_CODES.O)
    protected fun dataStoreLocalDateTimeFlow(
        key: Preferences.Key<String>,
        default: LocalDateTime?
    ): DataStoreFlow<LocalDateTime?> =
        DataStoreFlow(
            default = default,
            flow = getLocalDateTimeFlow(key, default),
            save = { saveStringRepresentation(key, it) }
        )

    // ============
    // DataStoreStateFlow
    // ============

    protected fun <T> dataStoreStateFlow(
        key: Preferences.Key<T>,
        default: T,
        scope: CoroutineScope,
        sharingStarted: SharingStarted,
    ): DataStoreStateFlow<T> =
        DataStoreStateFlow(
            flow = getFlow(key, default),
            default = default,
            scope = scope,
            sharingStarted = sharingStarted,
            save = { save(key, it) }
        )

    protected fun <S, T> dataStoreStateFlow(
        toSaveValue: (T) -> S,
        fromSaveValue: (S) -> T,
        key: Preferences.Key<S>,
        default: T,
        scope: CoroutineScope,
        sharingStarted: SharingStarted,
    ): DataStoreStateFlow<T> =
        DataStoreStateFlow(
            flow = getFlow(key, toSaveValue(default)).map { fromSaveValue(it) },
            default = default,
            scope = scope,
            sharingStarted = sharingStarted,
            save = { save(key, toSaveValue(it)) }
        )

    protected inline fun <reified E : Enum<E>> dataStoreStateFlow(
        key: Preferences.Key<Int>,
        default: E,
        scope: CoroutineScope,
        sharingStarted: SharingStarted,
    ): DataStoreStateFlow<E> =
        DataStoreStateFlow(
            flow = getEnumFlow<E>(key, default),
            default = default,
            scope = scope,
            sharingStarted = sharingStarted,
            save = { save(key, it) }
        )

    protected fun dataStoreUriStateFlow(
        key: Preferences.Key<String>,
        default: Uri?,
        scope: CoroutineScope,
        sharingStarted: SharingStarted,
    ): DataStoreStateFlow<Uri?> =
        DataStoreStateFlow(
            flow = getUriFlow(key, default),
            default = default,
            scope = scope,
            sharingStarted = sharingStarted,
            save = { saveStringRepresentation(key, it) }
        )

    @RequiresApi(Build.VERSION_CODES.O)
    protected fun dataStoreLocalDateTimeStateFlow(
        key: Preferences.Key<String>,
        default: LocalDateTime?,
        scope: CoroutineScope,
        sharingStarted: SharingStarted,
    ): DataStoreStateFlow<LocalDateTime?> =
        DataStoreStateFlow(
            default = default,
            flow = getLocalDateTimeFlow(key, default),
            scope = scope,
            sharingStarted = sharingStarted,
            save = { saveStringRepresentation(key, it) }
        )
}

private const val DEFAULT_STRING_VALUE = ""

private fun <T> MutablePreferences.save(preferencesKey: Preferences.Key<T>, value: T) {
    this[preferencesKey] = value
    i { "Saved ${preferencesKey.name}=$value" }
}

private fun MutablePreferences.saveStringRepresentation(
    preferencesKey: Preferences.Key<String>,
    value: Any?
) {
    save(preferencesKey, value?.toString() ?: DEFAULT_STRING_VALUE)
}