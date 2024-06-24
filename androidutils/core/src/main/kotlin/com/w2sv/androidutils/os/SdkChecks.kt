package com.w2sv.androidutils.os

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val dynamicColorsSupported
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
val postNotificationsPermissionRequired: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
val manageExternalStoragePermissionRequired: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R