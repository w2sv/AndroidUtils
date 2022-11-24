@file:Suppress("unused")

package com.w2sv.androidutils.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <R> CoroutineScope.launch(
    f: () -> R,
    onPostExecute: ((R) -> Unit)? = null
) =
    launch {
        val result = withContext(Dispatchers.IO) {
            f()
        }
        onPostExecute?.run { invoke(result) }
    }

fun <P, R> CoroutineScope.launch(
    f: suspend (suspend (P) -> Unit) -> R,
    onProgressUpdate: (P) -> Unit,
    onPostExecute: (R) -> Unit
) =
    launch {
        onPostExecute(
            withContext(Dispatchers.IO) {
                f {
                    withContext(Dispatchers.Main) { onProgressUpdate(it) }
                }
            }
        )
    }

inline fun CoroutineScope.launchDelayed(timeMillis: Long, crossinline f: () -> Unit): Job =
    launch {
        delay(timeMillis)
        f()
    }