@file:Suppress("unused")

package com.w2sv.androidutils.res

import android.content.res.Configuration

/**
 * Whether the current configuration is in night mode.
 *
 * This hides the bitmask comparison required by Android's raw [Configuration]
 * API.
 */
val Configuration.isNightModeActiveCompat: Boolean
    get() = uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
