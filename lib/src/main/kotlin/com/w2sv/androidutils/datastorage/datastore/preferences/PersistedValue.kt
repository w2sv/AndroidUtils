package com.w2sv.androidutils.datastorage.datastore.preferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class PersistedValue<K, V>(
    flow: Flow<V>,
    private val default: V,
    val save: suspend (V) -> Unit
) : Flow<V> by flow {

    fun stateIn(
        scope: CoroutineScope,
        started: SharingStarted,
    ): StateFlow<V> =
        stateIn(scope, started, default)

    fun launchSave(value: V, scope: CoroutineScope): Job =
        scope.launch {
            save(value)
        }

    class UniTyped<T>(default: T, flow: Flow<T>, save: suspend (T) -> Unit) :
        PersistedValue<T, T>(
            flow = flow,
            default = default,
            save = save
        )

    class EnumValued<E : Enum<E>>(default: E, flow: Flow<E>, save: suspend (E) -> Unit) :
        PersistedValue<Int, E>(
            flow = flow,
            default = default,
            save = save
        )

    class StringRepresentationPersisted<T>(default: T?, flow: Flow<T?>, save: suspend (T?) -> Unit) :
        PersistedValue<String, T?>(
            flow = flow,
            default = default,
            save = save
        )
}