@file:Suppress("unused")

package com.w2sv.androidutils.view

import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Shows this [DialogFragment] with its class name as tag.
 *
 * This avoids repeating stable tag strings when calling
 * [DialogFragment.show].
 */
@MainThread
fun DialogFragment.show(fragmentManager: FragmentManager) =
    show(fragmentManager, this::class.java.name)
