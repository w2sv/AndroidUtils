package com.w2sv.androidutils.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Base [Service] for services that never support binding.
 *
 * This removes the repeated `onBind() = null` boilerplate required by the
 * Android [Service] API.
 */
abstract class UnboundService : Service() {
    override fun onBind(intent: Intent?): IBinder? =
        null
}
