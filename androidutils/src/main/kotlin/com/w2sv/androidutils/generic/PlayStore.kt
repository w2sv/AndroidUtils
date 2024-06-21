@file:Suppress("unused")

package com.w2sv.androidutils.generic

import android.content.Context

fun appPlayStoreUrl(context: Context): String =
    "https://play.google.com/store/apps/details?id=${context.packageName}"
