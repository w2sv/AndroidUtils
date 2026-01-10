@file:Suppress("unused")

package com.w2sv.androidutils.content

import android.content.Context

val Context.packagePlayStoreUrl: String
    get() = "https://play.google.com/store/apps/details?id=$packageName"
