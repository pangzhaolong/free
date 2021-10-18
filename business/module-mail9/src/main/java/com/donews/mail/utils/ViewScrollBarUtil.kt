package com.donews.mail.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import com.donews.base.base.BaseApplication
import com.donews.mail.R

/**
 * @author lcl
 * Date on 2021/10/13
 * Description:
 *  处理View的ScrollBar的工具
 */
@SuppressLint("UseCompatLoadingForDrawables")
object ViewScrollBarUtil {

    //RecyclerView 滚动条样式。向上滚动的滚动条样式
    private val recyScrollDownBar: Drawable by lazy {
        BaseApplication.getInstance().resources.getDrawable(R.drawable.mail_recycler_scroll_bar_down)
    }

    //RecyclerView 滚动条样式。向下滚动的滚动条样式
    private val recyScrollUpBar: Drawable by lazy {
        BaseApplication.getInstance().resources.getDrawable(R.drawable.mail_recycler_scroll_bar)
    }

    /**
     * 设置视图的滚动条样式
     * @param scrollBarIsDown T:滚动条向下滑动。F:滚动条向上滑动
     */
    fun setViewScrollBarStyle(scrollView: View, scrollBarIsDown: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!scrollBarIsDown) { //上滚动，滚动条向下滑动
                if (scrollView.verticalScrollbarThumbDrawable != recyScrollDownBar) {
                    scrollView.verticalScrollbarThumbDrawable = recyScrollDownBar
                }
            } else { //下滚动，滚动条向上滑动
                if (scrollView.verticalScrollbarThumbDrawable != recyScrollUpBar) {
                    scrollView.verticalScrollbarThumbDrawable = recyScrollUpBar
                }
            }
        }
    }

}