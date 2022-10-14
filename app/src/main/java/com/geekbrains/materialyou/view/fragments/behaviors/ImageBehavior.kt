package com.geekbrains.materialyou.view.fragments.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.geekbrains.materialyou.R
import com.google.android.material.appbar.AppBarLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.abs

class ImageBehavior(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<CircleImageView>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: CircleImageView,
        dependency: View
    ): Boolean {
        return (dependency is AppBarLayout)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: CircleImageView,
        dependency: View
    ): Boolean {
        if (dependency.id == R.id.mars_app_bar_layout) {
            child.y = dependency.y + dependency.height - child.height / 2
            child.x = dependency.x + 20
            if (dependency.y > -400) child.alpha = 0.0f
            else child.alpha = 0.0f + abs(dependency.y / dependency.height)
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}