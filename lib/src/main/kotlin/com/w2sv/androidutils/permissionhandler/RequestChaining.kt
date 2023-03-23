@file:Suppress("unused")

package com.w2sv.permissionhandler

fun Iterable<PermissionHandler<*, *>>.requestPermissions(
    onGranted: () -> Unit,
    onDenied: (() -> Unit)? = null,
    onRequestDismissed: (() -> Unit)? = null
) {
    iterator().requestPermissions(onGranted, onDenied, onRequestDismissed)
}

private fun Iterator<PermissionHandler<*, *>>.requestPermissions(
    onGranted: () -> Unit,
    onDenied: (() -> Unit)? = null,
    onRequestDismissed: (() -> Unit)? = null
) {
    if (!hasNext()) {
        onGranted()
        onRequestDismissed?.invoke()
    } else {
        next().requestPermissionIfRequired(
            onGranted = { requestPermissions(onGranted, onDenied, onRequestDismissed) },
            onDenied = onDenied
        )
    }
}