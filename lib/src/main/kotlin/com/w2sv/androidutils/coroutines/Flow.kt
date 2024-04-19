@file:Suppress("unused")

package com.w2sv.androidutils.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

fun <T> Flow<T>.firstBlocking(): T =
    runBlocking { first() }

fun <T> Flow<T>.stateInWithSynchronousInitial(
    scope: CoroutineScope,
    started: SharingStarted
): StateFlow<T> =
    stateIn(scope = scope, started = started, initialValue = firstBlocking())