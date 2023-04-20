@file:Suppress("unused")

package com.w2sv.androidutils.utils

import android.content.Context

fun appPlayStoreUrl(context: Context): String =
    "https://play.google.com/store/apps/details?id=${context.packageName}"
