@file:Suppress("unused")

package com.w2sv.androidutils.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.w2sv.androidutils.R
import kotlin.math.min
import kotlin.reflect.KClass

abstract class RepelledLayout<out V: View>(context: Context, private val attrs: AttributeSet, private val repellingViewClass: KClass<V>) :
    LinearLayout(
        context,
        attrs
    ),
    CoordinatorLayout.AttachedBehavior {

    private val translationCoefficient: Float

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RepelledLayout,
            0,
            0
        )
            .apply {
                try {
                    translationCoefficient =
                        getFloat(R.styleable.RepelledLayout_translationCoefficient, 1f)
                } finally {
                    recycle()
                }
            }
    }

    inner class Behavior(context: Context?, attrs: AttributeSet?) :
        CoordinatorLayout.Behavior<View?>(context, attrs) {

        override fun layoutDependsOn(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ): Boolean =
            dependency::class == repellingViewClass

        override fun onDependentViewChanged(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ): Boolean {
            child
                .apply {
                    clearAnimation()

                    val translation = min(
                        0f,
                        (dependency.translationY - dependency.height) * translationCoefficient
                    )
                    translationY = translation
                    setPadding(0, -translation.toInt(), 0, 0)
                }
            return true
        }

        override fun onDependentViewRemoved(
            parent: CoordinatorLayout,
            child: View,
            dependency: View
        ) {
            child.setPadding(0, 0, 0, 0)
            super.onDependentViewRemoved(parent, child, dependency)
        }
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> =
        Behavior(context, attrs)
}