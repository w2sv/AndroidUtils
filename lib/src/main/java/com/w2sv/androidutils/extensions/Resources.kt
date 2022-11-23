package com.w2sv.androidutils.extensions

import android.content.res.Resources
import androidx.annotation.IntegerRes

fun Resources.getLong(@IntegerRes id: Int): Long =
    getInteger(id).toLong()