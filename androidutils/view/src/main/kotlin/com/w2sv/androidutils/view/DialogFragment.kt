@file:Suppress("unused")

package com.w2sv.androidutils.view

import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Show [DialogFragment] with class name as tag.
 */
@MainThread
fun DialogFragment.show(fragmentManager: FragmentManager) =
    show(fragmentManager, this::class.java.name)
