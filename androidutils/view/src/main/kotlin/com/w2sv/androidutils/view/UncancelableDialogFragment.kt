@file:Suppress("unused")

package com.w2sv.androidutils.view

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.DialogFragment

/**
 * Base [DialogFragment] that cannot be canceled.
 *
 * This centralizes the `isCancelable = false` setup and provides a matching
 * non-cancelable [AlertDialog.Builder].
 */
abstract class UncancelableDialogFragment : DialogFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        isCancelable = false
    }

    /**
     * Creates a non-cancelable [AlertDialog.Builder].
     *
     * This keeps dialog construction aligned with the fragment's non-cancelable
     * behavior.
     */
    protected fun builder(): AlertDialog.Builder =
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
}
