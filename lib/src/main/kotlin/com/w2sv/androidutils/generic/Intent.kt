@file:Suppress("unused")

package com.w2sv.androidutils.generic

import android.content.Intent

fun Intent.getIntExtraOrNull(name: String, defaultValue: Int): Int? =
    getIntExtra(name, defaultValue).run {
        if (equals(defaultValue))
            null
        else
            this
    }