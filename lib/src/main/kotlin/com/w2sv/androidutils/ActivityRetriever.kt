@file:Suppress("unused")

package com.w2sv.androidutils

import android.app.Activity
import android.content.Context
import com.w2sv.androidutils.extensions.requireActivity

interface ActivityRetriever {
    val activity: Activity

    @Suppress("UNCHECKED_CAST")
    fun <A : Activity> castActivity(): A =
        activity as A

    open class Impl(private val context: Context) : ActivityRetriever {

        override val activity: Activity by lazy {
            context.requireActivity()
        }
    }
}