@file:Suppress("unused")

package com.w2sv.androidutils.ui.dialogs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * Show [DialogFragment] with class name as tag.
 */
fun DialogFragment.show(fragmentManager: FragmentManager) =
    show(fragmentManager, this::class.java.name)