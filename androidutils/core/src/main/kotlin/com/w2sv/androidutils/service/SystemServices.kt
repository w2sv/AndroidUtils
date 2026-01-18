@file:Suppress("unused")

package com.w2sv.androidutils.service

import android.content.Context

/**
 * @return a system service of type [T].
 *
 * This is a type-safe, concise alternative to `getSystemService(Class)`, avoiding ::class.java shenanigans.
 *
 * Example:
 * ```
 * val statusBarManager = context.systemService<StatusBarManager>()
 * ```
 *
 * @see Context.getSystemService
 * @throws IllegalStateException if the service is not available on the device.
 */
inline fun <reified T> Context.systemService(): T =
    getSystemService(T::class.java) ?: error("System service ${T::class.java.simpleName} not available")
