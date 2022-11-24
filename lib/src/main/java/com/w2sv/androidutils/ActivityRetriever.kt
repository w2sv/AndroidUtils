@file:Suppress("unused")

package com.w2sv.androidutils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.w2sv.androidutils.extensions.getActivity

interface ActivityRetriever {
    val activity: Activity
    val fragmentActivity: FragmentActivity

    @Suppress("UNCHECKED_CAST")
    fun <A : Activity> castActivity(): A =
        activity as A

    open class Implementation(private val context: Context) : ActivityRetriever {

        override val activity: Activity by lazy {
            context.getActivity()!!
        }

        override val fragmentActivity: FragmentActivity
            get() = activity as FragmentActivity
    }
}