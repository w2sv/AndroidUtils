@file:Suppress("unused")

package com.w2sv.androidutils.graphics

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import kotlin.math.roundToInt

/**
 * Converts a normalized float channel to an RGB channel integer.
 *
 * This makes alpha/color math call sites read in Android's 0..255 channel
 * representation without repeating scaling and rounding.
 */
@ColorInt
fun @receiver:FloatRange(0.0, 1.0) Float.toRGBChannelInt(): Int =
    (this * 255).roundToInt()

/**
 * Returns [color] with its alpha replaced by [alpha].
 *
 * This accepts Android's usual normalized alpha value and hides the required
 * conversion to the integer channel expected by [ColorUtils.setAlphaComponent].
 */
@ColorInt
fun getAlphaSetColor(@ColorInt color: Int, @FloatRange(0.0, 1.0) alpha: Float): Int =
    ColorUtils.setAlphaComponent(
        color,
        alpha.toRGBChannelInt()
    )
