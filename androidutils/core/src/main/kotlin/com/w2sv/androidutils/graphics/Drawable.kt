package com.w2sv.androidutils.graphics

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Tints this [Drawable] with a color resource.
 *
 * This avoids resolving the color manually before calling Android's color
 * filter API.
 */
fun Drawable.setColor(context: Context, @ColorRes colorId: Int) {
    @Suppress("DEPRECATION")
    setColorFilter(context.getColor(colorId), PorterDuff.Mode.SRC_IN)
}

/**
 * Loads and tints a drawable resource.
 *
 * This combines AppCompat drawable loading, drawable wrapping, and tinting into
 * one call.
 */
fun Context.getColoredDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable =
    DrawableCompat.wrap(AppCompatResources.getDrawable(this, drawableId)!!).apply {
        setColor(this@getColoredDrawable, colorId)
    }
