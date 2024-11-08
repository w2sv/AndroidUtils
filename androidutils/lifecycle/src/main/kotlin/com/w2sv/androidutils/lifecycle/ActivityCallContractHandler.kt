@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface ActivityCallContractHandler<I, O> : DefaultLifecycleObserver {
    val resultRegistry: ActivityResultRegistry
    val resultContract: ActivityResultContract<I, O>
    val resultCallback: (O) -> Unit
    val registryKey: String

    var resultLauncher: ActivityResultLauncher<I>

    abstract class Impl<I, O>(
        override val resultRegistry: ActivityResultRegistry,
        override val resultContract: ActivityResultContract<I, O>
    ) : ActivityCallContractHandler<I, O> {
        /**
         * Enable construction from [activity]
         */
        constructor(
            activity: ComponentActivity,
            activityResultContract: ActivityResultContract<I, O>
        ) : this(activity.activityResultRegistry, activityResultContract)

        /**
         * Key by which [resultCallback] will be identified within [resultRegistry]
         */
        override val registryKey: String = this::class.java.name

        override lateinit var resultLauncher: ActivityResultLauncher<I>

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)

            resultLauncher =
                resultRegistry.register(
                    registryKey,
                    owner,
                    resultContract,
                    resultCallback
                )
        }
    }
}
