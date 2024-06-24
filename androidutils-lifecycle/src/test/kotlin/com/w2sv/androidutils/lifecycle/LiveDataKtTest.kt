package com.w2sv.androidutils.lifecycle

import androidx.lifecycle.MutableLiveData
import com.w2sv.androidutils.test.junit5.InstantExecutorExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class LiveDataKtTest {

    @Test
    fun toggleBooleanLiveData() {
        with(MutableLiveData(true)) {
            toggle()
            Assertions.assertEquals(value, false)
        }
    }
}