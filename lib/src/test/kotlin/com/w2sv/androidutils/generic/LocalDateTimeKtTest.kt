package com.w2sv.androidutils.generic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class LocalDateTimeTest {

    @Test
    fun testMilliSecondsTo() {
        val dateTime1 = LocalDateTime.of(2023, 1, 1, 12, 0)
        val dateTime2 = LocalDateTime.of(2023, 1, 1, 12, 0, 5)
        val difference = dateTime1.milliSecondsTo(dateTime2)

        assertEquals(5000, difference)
    }

    @Test
    fun testLocalDateTimeFromUnixTimeStamp() {
        val unixTimestamp = 1699041284L
        val expectedDateTime = LocalDateTime.of(2023, 11, 3, 20, 54, 44)
        val dateTime = localDateTimeFromUnixTimeStamp(unixTimestamp)

        assertEquals(expectedDateTime, dateTime)
    }
}