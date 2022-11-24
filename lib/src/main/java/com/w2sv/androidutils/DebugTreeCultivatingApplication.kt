@file:Suppress("unused")

package com.w2sv.androidutils

import android.app.Application
import timber.log.Timber

class DebugTreeCultivatingApplication : Application() {

    /**
     * Plants [Timber] tree if debug mode active
     */
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}