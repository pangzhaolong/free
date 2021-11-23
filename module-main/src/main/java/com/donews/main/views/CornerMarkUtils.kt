package com.donews.main.views

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.donews.main.R
import com.donews.utilslibrary.utils.DensityUtils


/**
 * 底部添加角标
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/22 13:47
 */
class CornerMarkUtils(val activity: Activity) {


    private var imageView: ImageView? = null


    fun addMark(item: MainBottomTanItem) {
        if (imageView != null) {
            imageView?.let {
                val parent = it.parent as ViewGroup
                parent.removeView(it)
            }
        }

        val local = intArrayOf(0, 0)
        val iconView: View = item.findViewById(R.id.icon)
        iconView.getLocationInWindow(local)
        val x = local[0] + iconView.width / 2
        val y = local[1]

        imageView = ImageView(activity)
        imageView?.let {

            val height = 28.33f
            val width = 55

            it.setImageResource(R.drawable.main_icon_corner_mark)
            val contentView: ViewGroup = activity.window.decorView.findViewById(android.R.id.content)
            val params =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            params.marginStart = (x - width / 2f).toInt()
            params.topMargin = y - DensityUtils.dip2px(height)
            contentView.addView(it, params)
        }
    }

    fun removeMark() {
        if (imageView != null) {
            imageView?.let {
                val parent = it.parent as ViewGroup
                parent.removeView(it)
            }
        }
    }

}