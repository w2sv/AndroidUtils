package com.w2sv.androidutils.datastorage.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

fun Context.getSharedPreferences(name: String = packageName): SharedPreferences =
    getSharedPreferences(name, Context.MODE_PRIVATE)