@file:Suppress("unused")

package com.w2sv.androidutils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

@Throws(IllegalStateException::class)
fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Couldn't get Activity")
}

@Suppress("UNCHECKED_CAST")
@Throws(ClassCastException::class, IllegalStateException::class)
fun <A : Activity> Context.requireCastActivity(): A =
    findActivity() as A