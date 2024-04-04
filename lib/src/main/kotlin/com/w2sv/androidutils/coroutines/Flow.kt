@file:Suppress("unused")

package com.w2sv.androidutils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

fun <T> Flow<T>.getValueSynchronously(): T =
    runBlocking { first() }

fun <K, V> Map<K, Flow<V>>.getSynchronousMap(): Map<K, V> =
    runBlocking {
        mapValues {
            it.value.first()
        }
    }

fun <T> Flow<T>.stateInWithSynchronousInitial(
    scope: CoroutineScope,
    started: SharingStarted
): StateFlow<T> =
    stateIn(scope = scope, started = started, initialValue = getValueSynchronously())

/**
 * Does not produce the same value in a row, so respect "distinct until changed emissions"
 *
 * Source: https://github.com/Kotlin/kotlinx.coroutines/issues/2631#issuecomment-870565860
 */
class DerivedStateFlow<T>(
    private val getValue: () -> T,
    private val flow: Flow<T>
) : StateFlow<T> {

    override val replayCache: List<T>
        get() = listOf(value)

    override val value: T
        get() = getValue()

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        coroutineScope { flow.distinctUntilChanged().stateIn(this).collect(collector) }
    }
}

fun <T1, R> StateFlow<T1>.mapState(transform: (a: T1) -> R): StateFlow<R> =
    DerivedStateFlow(
        getValue = { transform(value) },
        flow = map { a -> transform(a) }
    )

fun <T1, T2, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    transform: (a: T1, b: T2) -> R
): StateFlow<R> =
    DerivedStateFlow(
        getValue = { transform(flow1.value, flow2.value) },
        flow = combine(flow1, flow2) { a, b -> transform(a, b) }
    )

fun <T1, T2, T3, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    transform: (a: T1, b: T2, c: T3) -> R
): StateFlow<R> =
    DerivedStateFlow(
        getValue = { transform(flow1.value, flow2.value, flow3.value) },
        flow = combine(flow1, flow2, flow3) { a, b, c -> transform(a, b, c) }
    )

fun <T1, T2, T3, T4, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    transform: (a: T1, b: T2, c: T3, d: T4) -> R
): StateFlow<R> =
    DerivedStateFlow(
        getValue = { transform(flow1.value, flow2.value, flow3.value, flow4.value) },
        flow = combine(flow1, flow2, flow3, flow4) { a, b, c, d -> transform(a, b, c, d) }
    )

fun <T1, T2, T3, T4, T5, R> combineStates(
    flow1: StateFlow<T1>,
    flow2: StateFlow<T2>,
    flow3: StateFlow<T3>,
    flow4: StateFlow<T4>,
    flow5: StateFlow<T5>,
    transform: (a: T1, b: T2, c: T3, d: T4, e: T5) -> R
): StateFlow<R> =
    DerivedStateFlow(
        getValue = { transform(flow1.value, flow2.value, flow3.value, flow4.value, flow5.value) },
        flow = combine(flow1, flow2, flow3, flow4, flow5) { a, b, c, d, e ->
            transform(
                a,
                b,
                c,
                d,
                e
            )
        }
    )

inline fun <reified T, R> combineStates(
    vararg flows: StateFlow<T>,
    crossinline transform: (Array<T>) -> R
): StateFlow<R> = DerivedStateFlow(
    getValue = { transform(flows.map { it.value }.toTypedArray()) },
    flow = combine(*flows) {
        transform(it)
    }
)