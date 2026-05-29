@file:Suppress("unused")

package com.w2sv.androidutils.backpress

import com.w2sv.kotlinutils.coroutines.launchDelayed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Handles "press back twice to confirm" flows.
 *
 * This keeps the temporary confirmation state outside an Activity or Fragment,
 * avoiding ad-hoc boolean flags and delayed reset jobs in UI code.
 */
class BackPressHandler(private val scope: CoroutineScope, private val confirmationWindowMillis: Long = 2_000) {
    private var resetJob: Job? = null
    private var pressedOnce: Boolean = false
        set(value) {
            resetJob?.cancel()
            if (value) {
                resetJob = scope.launchDelayed(confirmationWindowMillis) { field = false }
            }
            field = value
        }

    /**
     * Runs [onFirstPress] for the first press and [onSecondPress] when called
     * again within the configured confirmation window.
     */
    operator fun invoke(onFirstPress: () -> Unit, onSecondPress: () -> Unit) {
        when (pressedOnce) {
            false -> {
                pressedOnce = true
                onFirstPress()
            }

            true -> {
                pressedOnce = false
                onSecondPress()
            }
        }
    }
}
