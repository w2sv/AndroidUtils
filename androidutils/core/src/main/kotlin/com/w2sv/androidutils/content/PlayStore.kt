@file:Suppress("unused")

package com.w2sv.androidutils.content

import android.content.Context

/**
 * Returns this app's Play Store details URL.
 *
 * This builds the package-specific URL from [Context.packageName] so callers do
 * not repeat the Play Store URI format.
 */
val Context.packagePlayStoreUrl: String
    get() = "https://play.google.com/store/apps/details?id=$packageName"
