@file:Suppress("unused", "DEPRECATION")

package com.w2sv.androidutils.lifecycle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * [BroadcastReceiver] registering itself upon instantiation and unregistering itself in [onDestroy].
 */
abstract class SelfManagingLocalBroadcastReceiver(
    private val broadcastManager: LocalBroadcastManager,
    intentFilter: IntentFilter
) : BroadcastReceiver(),
    DefaultLifecycleObserver {

    constructor(
        context: Context, intentFilter: IntentFilter
    ) : this(LocalBroadcastManager.getInstance(context), intentFilter)

    init {
        @Suppress("LeakingThis")
        broadcastManager
            .registerReceiver(
                this,
                intentFilter
            )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        broadcastManager
            .unregisterReceiver(this)
    }

    open class Impl(
        broadcastManager: LocalBroadcastManager,
        intentFilter: IntentFilter,
        private val callback: (Context?, Intent?) -> Unit
    ) : SelfManagingLocalBroadcastReceiver(broadcastManager, intentFilter) {

        constructor(
            context: Context, intentFilter: IntentFilter, callback: (Context?, Intent?) -> Unit
        ) : this(LocalBroadcastManager.getInstance(context), intentFilter, callback)

        override fun onReceive(context: Context?, intent: Intent?) {
            callback(context, intent)
        }
    }
}