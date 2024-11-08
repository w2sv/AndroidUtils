@file:Suppress("unused")

package com.w2sv.androidutils.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context

fun AppWidgetManager.getAppWidgetIds(context: Context, appWidgetProviderClass: Class<out AppWidgetProvider>): IntArray =
    getAppWidgetIds(
        context.packageName,
        appWidgetProviderClass
    )

fun AppWidgetManager.getAppWidgetIds(packageName: String, appWidgetProviderClass: Class<out AppWidgetProvider>): IntArray =
    getAppWidgetIds(
        ComponentName(
            packageName,
            appWidgetProviderClass.name
        )
    )
