@file:Suppress("unused")

package com.w2sv.androidutils.ui

import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener

abstract class SimpleAnimationListener : AnimationListener {
    override fun onAnimationStart(animation: Animation?) {}
    override fun onAnimationEnd(animation: Animation?) {}
    override fun onAnimationRepeat(animation: Animation?) {}
}