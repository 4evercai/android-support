package com.i4evercai.android.support.utils

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.ImageButton
import androidx.annotation.ColorInt
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar

/**
 *
 * @Description: ToolBarUtils
 * @author Fitz
 * @email FitzPro@qq.com
 * @date 2017/7/8 11:25
 * @version V1.0
 */
object ToolbarUtils {
    /**
     * 对Toolbar上的图标进行着色
     */
    @JvmStatic
    fun colorizeToolbar(toolbar: Toolbar, @ColorInt toolbarIconsColor: Int) {
        val colorFilter = PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY)
        for (i in 0..toolbar.childCount - 1) {
            try {
                val view = toolbar.getChildAt(i)
                if (view is ImageButton) {
                    view.drawable.setColorFilter(colorFilter)
                } else if (view is ActionMenuView) {
                    colorizeActionMenuView(view, colorFilter)
                } else if (view is android.widget.ActionMenuView) {
                    colorizeActionMenuView(view, colorFilter)
                }
            } catch (e: Exception) {

            }
        }
    }

    @JvmStatic
    fun colorizeToolbar(toolbar: android.widget.Toolbar, @ColorInt toolbarIconsColor: Int) {
        val colorFilter = PorterDuffColorFilter(toolbarIconsColor, PorterDuff.Mode.MULTIPLY)
        for (i in 0..toolbar.childCount - 1) {
            try {
                val view = toolbar.getChildAt(i)
                if (view is ImageButton) {
                    view.drawable.setColorFilter(colorFilter)
                }
                if (view is ActionMenuView) {
                    colorizeActionMenuView(view, colorFilter)
                }
            } catch (e: Exception) {

            }
        }
    }

    @JvmStatic
    private fun colorizeActionMenuView(actionMenuView: ActionMenuView, colorFilter: PorterDuffColorFilter) {

        for (i in 0..actionMenuView.childCount) {
            val view = actionMenuView.getChildAt(i)
            if (view is ActionMenuItemView) {
                val compoundDrawables = view.compoundDrawables
                val drawablesCount = compoundDrawables.size
                for (j in 0..drawablesCount - 1) {
                    if (compoundDrawables[j] != null) {
                        actionMenuView.post {
                            compoundDrawables[j].setColorFilter(colorFilter)
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    private fun colorizeActionMenuView(actionMenuView: android.widget.ActionMenuView, colorFilter: PorterDuffColorFilter) {

        for (i in 0..actionMenuView.childCount) {
            val view = actionMenuView.getChildAt(i)
            if (view is ActionMenuItemView) {
                val compoundDrawables = view.compoundDrawables
                val drawablesCount = compoundDrawables.size
                for (j in 0..drawablesCount - 1) {
                    if (compoundDrawables[j] != null) {
                        actionMenuView.post {
                            compoundDrawables[j].setColorFilter(colorFilter)
                        }
                    }
                }
            }
        }
    }
}