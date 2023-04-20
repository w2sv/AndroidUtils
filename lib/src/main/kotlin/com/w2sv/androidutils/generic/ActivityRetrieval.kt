@file:Suppress("unused")

package com.w2sv.androidutils.generic

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

val Context.activity: Activity?
    get() =
        this as? Activity ?: (this as? ContextWrapper)?.baseContext?.activity

fun Context.requireActivity(): Activity = activity!!

@Suppress("UNCHECKED_CAST")
fun <A : Activity> Context.requireCastActivity(): A =
    requireActivity() as A