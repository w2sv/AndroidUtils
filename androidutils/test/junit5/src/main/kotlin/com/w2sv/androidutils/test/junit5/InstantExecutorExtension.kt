package com.w2sv.androidutils.test.junit5

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Runs Architecture Components tasks synchronously in JUnit 5 tests.
 *
 * This provides the JUnit 5 equivalent of AndroidX's JUnit 4 instant executor
 * rule, making LiveData and related components deterministic in unit tests.
 *
 * Based on https://jeroenmols.com/blog/2019/01/17/livedatajunit5/
 */
@SuppressLint("RestrictedApi")
class InstantExecutorExtension :
    BeforeEachCallback,
    AfterEachCallback {

    override fun beforeEach(context: ExtensionContext) {
        ArchTaskExecutor.getInstance()
            .setDelegate(
                object : TaskExecutor() {
                    override fun executeOnDiskIO(runnable: Runnable) =
                        runnable.run()

                    override fun postToMainThread(runnable: Runnable) =
                        runnable.run()

                    override fun isMainThread(): Boolean =
                        true
                }
            )
    }

    override fun afterEach(context: ExtensionContext) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}
