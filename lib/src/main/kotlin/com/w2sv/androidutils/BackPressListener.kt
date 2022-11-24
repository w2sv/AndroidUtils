package com.w2sv.androidutils

import com.w2sv.androidutils.extensions.launchDelayed
import kotlinx.coroutines.CoroutineScope

open class BackPressListener(
    private val coroutineScope: CoroutineScope,
    private val confirmationWindowDuration: Long = 2500,
) {
    private var pressedOnce: Boolean = false

    operator fun invoke(onFirstPress: () -> Unit, onSecondPress: () -> Unit) {
        if (pressedOnce) {
            pressedOnce = false
            onSecondPress()
        } else {
            pressedOnce = true
            onFirstPress()

            coroutineScope.launchDelayed(confirmationWindowDuration) {
                pressedOnce = false
            }
        }
    }
}