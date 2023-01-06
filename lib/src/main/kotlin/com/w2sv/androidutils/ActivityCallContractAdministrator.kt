@file:Suppress("unused")

package com.w2sv.androidutils

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface ActivityCallContractAdministrator<I, O> : DefaultLifecycleObserver {
    val activityResultRegistry: ActivityResultRegistry
    val activityResultContract: ActivityResultContract<I, O>
    val activityResultCallback: (O) -> Unit
    var activityResultLauncher: ActivityResultLauncher<I>

    val registryKey: String

    abstract class Impl<I, O>(
        override val activityResultRegistry: ActivityResultRegistry,
        override val activityResultContract: ActivityResultContract<I, O>
    ) : ActivityCallContractAdministrator<I, O> {

        /**
         * Enable construction with [activity]
         */
        constructor(
            activity: ComponentActivity,
            activityResultContract: ActivityResultContract<I, O>
        ) : this(activity.activityResultRegistry, activityResultContract)

        /**
         * Key by which [activityResultCallback] will be identified within [activityResultRegistry]
         */
        override val registryKey: String = this::class.java.name

        override lateinit var activityResultLauncher: ActivityResultLauncher<I>

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)

            activityResultLauncher =
                activityResultRegistry.register(
                    registryKey,
                    owner,
                    activityResultContract,
                    activityResultCallback
                )
        }
    }
}