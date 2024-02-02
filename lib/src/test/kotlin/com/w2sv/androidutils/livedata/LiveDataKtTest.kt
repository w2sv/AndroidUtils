package com.w2sv.androidutils.livedata

import androidx.lifecycle.MutableLiveData
import com.w2sv.androidutils.lifecycle.extensions.toggle
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import utils.InstantExecutorExtension

@ExtendWith(InstantExecutorExtension::class)
internal class LiveDataKtTest {
    @Test
    fun toggleBooleanLiveData() {
        with(MutableLiveData(true)){
            toggle()
            Assertions.assertEquals(value, false)
        }
    }
}