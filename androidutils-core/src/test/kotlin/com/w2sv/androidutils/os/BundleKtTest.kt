package com.w2sv.androidutils.os

import android.os.Bundle
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class BundleKtTest {

    @Test
    fun `test empty bundle`() {
        val bundle = Bundle()
        assertEquals("{}", bundle.toMapString())
    }

    @Test
    fun `test bundle with primitive values`() {
        val bundle = Bundle().apply {
            putString("key1", "value1")
            putInt("key2", 42)
            putBoolean("key3", true)
        }
        assertEquals("{key1=value1, key2=42, key3=true}", bundle.toMapString())
    }

    @Test
    fun `test bundle with array values`() {
        val bundle = Bundle().apply {
            putIntArray("key1", intArrayOf(1, 2, 3))
            putStringArray("key2", arrayOf("a", "b"))
        }
        assertEquals("{key1=[1, 2, 3], key2=[a, b]}", bundle.toMapString())
    }

    @Test
    fun `test bundle with nested bundle`() {
        val nestedBundle = Bundle().apply {
            putString("nestedKey", "nestedValue")
        }
        val bundle = Bundle().apply {
            putBundle("key1", nestedBundle)
        }
        assertEquals("{key1={nestedKey=nestedValue}}", bundle.toMapString())
    }
}
