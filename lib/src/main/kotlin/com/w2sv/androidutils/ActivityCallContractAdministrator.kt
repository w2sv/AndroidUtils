@file:Suppress("unused")

package com.w2sv.androidutils

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class ActivityCallContractAdministrator<I, O>(
    private val registry: ActivityResultRegistry,
    private val activityResultContract: ActivityResultContract<I, O>
) : DefaultLifecycleObserver {

    constructor(
        activity: ComponentActivity,
        activityResultContract: ActivityResultContract<I, O>
    ) : this(activity.activityResultRegistry, activityResultContract)

    protected lateinit var activityResultLauncher: ActivityResultLauncher<I>

    abstract val activityResultCallback: (O) -> Unit

    protected open val key = this::class.java.name

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        activityResultLauncher = registry.register(key, owner, activityResultContract, activityResultCallback)
    }
}