package com.w2sv.androidutils.ui

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.DialogFragment

abstract class UncancelableDialogFragment : DialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)

        isCancelable = false
    }

    protected fun builder(): AlertDialog.Builder =
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
}