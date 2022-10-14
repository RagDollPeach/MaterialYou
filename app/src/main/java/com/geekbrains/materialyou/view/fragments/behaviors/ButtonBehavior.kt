package com.geekbrains.materialyou.view.fragments.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ButtonBehavior(context: Context, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return (dependency is AppBarLayout)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        if (dependency is AppBarLayout) {
            child.y = dependency.y + dependency.height - child.height / 2
            child.x = (dependency.width - child.width - 20).toFloat()

            if (dependency.y < -600) child.hide() else child.show()

        }
        return super.onDependentViewChanged(parent, child, dependency)
    }
}