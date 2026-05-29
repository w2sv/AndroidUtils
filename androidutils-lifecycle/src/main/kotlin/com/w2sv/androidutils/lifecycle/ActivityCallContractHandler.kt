@file:Suppress("unused")

package com.w2sv.androidutils.lifecycle

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Lifecycle-aware holder for an [ActivityResultContract] launcher.
 *
 * This packages result registration into a [DefaultLifecycleObserver], reducing
 * the boilerplate around [ActivityResultRegistry.register].
 */
interface ActivityCallContractHandler<I, O> : DefaultLifecycleObserver {
    /**
     * Registry used to create the launcher.
     *
     * Exposing it lets implementations register through an activity-provided or
     * test-provided registry instead of hard-coding an Activity dependency.
     */
    val resultRegistry: ActivityResultRegistry

    /**
     * Contract that converts inputs and outputs for the activity call.
     *
     * Keeping it on the handler makes launcher setup reusable for any stock or
     * custom [ActivityResultContract].
     */
    val resultContract: ActivityResultContract<I, O>

    /**
     * Callback invoked with the contract output.
     *
     * This keeps result handling colocated with registration instead of split
     * across Activity Result API call sites.
     */
    val resultCallback: (O) -> Unit

    /**
     * Key used when registering with [resultRegistry].
     *
     * Naming it explicitly avoids scattering string keys around call sites.
     */
    val registryKey: String

    /**
     * Launcher created during lifecycle registration.
     *
     * This stores the [ActivityResultLauncher] produced by the stock Activity
     * Result API so callers can launch through the handler.
     */
    var resultLauncher: ActivityResultLauncher<I>

    /**
     * Base implementation that registers [resultContract] during [onCreate].
     *
     * This lets subclasses provide only the registry key/callback behavior they
     * need instead of repeating launcher registration code.
     */
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

        @MainThread
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
