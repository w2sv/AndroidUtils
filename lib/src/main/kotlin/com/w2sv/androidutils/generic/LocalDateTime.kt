@file:Suppress("unused")
@file:RequiresApi(Build.VERSION_CODES.O)

package com.w2sv.androidutils.generic

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun localDateTimeFromSecondsUnixTimeStamp(
    secondsTimestamp: Long,
    zoneId: ZoneId = ZoneId.systemDefault()
): LocalDateTime =
    LocalDateTime.ofInstant(
        Instant.ofEpochSecond(secondsTimestamp),
        zoneId
    )

private fun localDateTimeFromUnixMilliSecondsTimeStamp(
    msTimeStamp: Long,
    zoneId: ZoneId = ZoneId.systemDefault()
): LocalDateTime =
    LocalDateTime.ofInstant(
        Instant.ofEpochMilli(msTimeStamp),
        zoneId
    )

fun LocalDateTime.timeDeltaToNow(): Duration =
    timeDeltaTo(LocalDateTime.now())

fun LocalDateTime.timeDeltaTo(other: LocalDateTime): Duration =
    Duration.between(
        this, other
    )

fun LocalDateTime.milliSecondsTo(other: LocalDateTime): Long =
    Duration.between(this, other).toMillis()

fun LocalDateTime.milliSecondsToNow(): Long =
    milliSecondsTo(LocalDateTime.now())

fun Duration.toSecondsCompat(): Long =
    seconds % 60