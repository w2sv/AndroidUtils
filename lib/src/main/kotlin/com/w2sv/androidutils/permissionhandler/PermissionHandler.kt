@file:Suppress("unused")

package com.w2sv.androidutils.permissionhandler

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.edit
import com.w2sv.androidutils.ActivityCallContractHandler
import com.w2sv.androidutils.extensions.getAppPreferences
import slimber.log.i

/**
 * @param activity the [ActivityResultRegistry] of which the [ActivityResultContract] will be registered to
 * @param permission the handled permission(s)
 * @param resultContract corresponding to respective [PermissionHandler] descendant
 * @param registryKey used for both the [ActivityResultRegistry] & SharedPreferences
 */
abstract class PermissionHandler<I, O>(
    protected val activity: ComponentActivity,
    protected val permission: I,
    resultContract: ActivityResultContract<I, O>,
    override val registryKey: String
) : ActivityCallContractHandler.Impl<I, O>(activity, resultContract) {

    /**
     *  [permissionPreviouslyRequested] retrieval
     */

    private val sharedPreferencesKey by ::registryKey

    protected var permissionPreviouslyRequested: Boolean =
        activity.getAppPreferences().getBoolean(sharedPreferencesKey, false)
            .also {
                i { "Retrieved $sharedPreferencesKey.permissionPreviouslyRequested=$it" }
            }

    /**
     * Sets [permissionPreviouslyRequested] & writes to default preferences.
     */
    private fun onFirstEverRequestDismissed(){
        permissionPreviouslyRequested = true
        activity.getAppPreferences().edit {
            putBoolean(sharedPreferencesKey, true)
            i { "Wrote $sharedPreferencesKey=true to sharedPreferences" }
        }
    }

    /**
     * Runs [onGranted] if [permission] already granted OR
     * Calls [onPermissionRationalSuppressed] if applicable OR
     * launches permission request after setting passed result callbacks.
     *
     * @param onGranted Invoked upon permission already or newly granted
     * @param onDenied Invoked upon permission requested and denied
     * @param onRequestDismissed Invoked upon permission request being dismissed, regardless of its outcome
     *
     * @return Boolean, indicating whether user has been prompted with request
     */
    open fun requestPermissionIfRequired(
        onGranted: () -> Unit,
        onDenied: (() -> Unit)? = null,
        onRequestDismissed: (() -> Unit)? = null
    ): Boolean =
        when {
            permissionGranted() -> {
                onGranted()
                false
            }

            permissionRationalSuppressed() -> {
                onPermissionRationalSuppressed()
                false
            }

            else -> {
                this.onGranted = onGranted
                this.onDenied = onDenied
                this.onRequestDismissed = onRequestDismissed

                resultLauncher.launch(permission)
                true
            }
        }

    abstract fun permissionGranted(): Boolean
    abstract fun permissionRationalSuppressed(): Boolean
    protected abstract val requiredByAndroidSdk: Boolean

    /**
     * Override to provide callback for permission request being attempted to launch,
     * however is suppressed.
     */
    open fun onPermissionRationalSuppressed() {}

    /**
     * Temporary callables set before, and cleared on exiting of [resultCallback]
     */

    private var onGranted: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null
    private var onRequestDismissed: (() -> Unit)? = null

    /**
     * Invokes ([onGranted] OR [onDenied]) AND [onRequestDismissed], if respectively set;
     * Resets [onGranted], [onDenied], [onRequestDismissed];
     *
     * Invokes [onFirstEverRequestDismissed] if applicable.
     */
    override val resultCallback: (O) -> Unit = { activityResult ->
        i { "Request result: $activityResult" }

        when (permissionNewlyGranted(activityResult)) {
            false -> onDenied?.invoke()
            true -> onGranted?.invoke()
        }

        onRequestDismissed?.invoke()

        onRequestDismissed = null
        onDenied = null
        onGranted = null

        if (!permissionPreviouslyRequested) {
            onFirstEverRequestDismissed()
        }
    }

    protected abstract fun permissionNewlyGranted(activityResult: O): Boolean
}