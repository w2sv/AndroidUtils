@file:Suppress("unused")

package com.w2sv.androidutils.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import com.w2sv.androidutils.content.componentName

/**
 * @see AppWidgetManager.getAppWidgetIds
 */
inline fun <reified T : AppWidgetProvider> AppWidgetManager.appWidgetIds(context: Context): IntArray =
    getAppWidgetIds(componentName<T>(context))
