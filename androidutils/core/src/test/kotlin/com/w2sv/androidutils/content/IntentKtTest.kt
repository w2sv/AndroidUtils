package com.w2sv.androidutils.content

import android.content.Intent
import android.os.Bundle
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class IntentKtTest {

    @Test
    fun `getIntExtraOrNull returns actual value equal to default`() {
        val intent = Intent().putExtra("key", 0)

        assertEquals(0, intent.getIntExtraOrNull("key", defaultValue = 0))
    }

    @Test
    fun `getIntExtraOrNull returns null when extra is missing`() {
        assertEquals(null, Intent().getIntExtraOrNull("key", defaultValue = 0))
    }

    @Test
    fun `test intent with action only`() {
        val intent = Intent().apply {
            action = "com.example.ACTION_TEST"
        }
        assertEquals("Action=com.example.ACTION_TEST", intent.toDebugString())
    }

    @Test
    fun `test intent with action and flags`() {
        val intent = Intent().apply {
            action = "com.example.ACTION_TEST"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        assertEquals("Action=com.example.ACTION_TEST | Flags=268435456", intent.toDebugString())
    }

    @Test
    fun `test intent with categories`() {
        val intent = Intent().apply {
            action = "com.example.ACTION_TEST"
            addCategory("com.example.CATEGORY_SAMPLE")
        }
        assertEquals("Action=com.example.ACTION_TEST | Categories={com.example.CATEGORY_SAMPLE}", intent.toDebugString())
    }

    @Test
    fun `test intent with extras`() {
        val bundle = Bundle().apply {
            putString("key1", "value1")
            putInt("key2", 42)
        }
        val intent = Intent().apply {
            action = "com.example.ACTION_TEST"
            putExtras(bundle)
        }
        assertEquals("Action=com.example.ACTION_TEST | Extras={key1=value1, key2=42}", intent.toDebugString())
    }

    @Test
    fun `test intent with action categories flags and extras`() {
        val bundle = Bundle().apply {
            putString("key1", "value1")
            putInt("key2", 42)
        }
        val intent = Intent().apply {
            action = "com.example.ACTION_TEST"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            addCategory("com.example.CATEGORY_SAMPLE")
            putExtras(bundle)
        }
        assertEquals(
            "Action=com.example.ACTION_TEST | Flags=268435456 | Categories={com.example.CATEGORY_SAMPLE} | Extras={key1=value1, key2=42}",
            intent.toDebugString()
        )
    }
}
