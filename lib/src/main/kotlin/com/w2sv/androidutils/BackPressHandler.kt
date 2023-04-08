@file:Suppress("unused")

package com.w2sv.androidutils

import com.w2sv.androidutils.extensions.launchDelayed
import kotlinx.coroutines.CoroutineScope

class BackPressHandler(
    private val coroutineScope: CoroutineScope,
    private val confirmationWindowDuration: Long,
) {
    private var pressedOnce: Boolean = false
        set(value) {
            if (value) {
                coroutineScope.launchDelayed(confirmationWindowDuration) {
                    field = false
                }
            }
            field = value
        }

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