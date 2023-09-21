package com.w2sv.androidutils.generic

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.timeDeltaFromNow(): Duration =
    durationBetween(LocalDateTime.now())

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.durationBetween(other: LocalDateTime): Duration =
    Duration.between(
        this, other
    )

@RequiresApi(Build.VERSION_CODES.O)
fun Duration.toSecondsCompat(): Long =
    seconds % 60