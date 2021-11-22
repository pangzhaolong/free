package com.donews.main.views

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.donews.common.contract.BaseCustomViewModel
import com.donews.main.BuildConfig
import com.donews.main.R
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.DensityUtils
import com.google.gson.annotations.SerializedName


/**
 * 底部添加角标
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/11/22 13:47
 */
class CornerMarkUtils(val activity: Activity) {


    private var imageView: ImageView? = null


    fun addMark(activity: Activity, item: MainBottomTanItem) {
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


    companion object {
        fun check() {
            EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/recent-lottery")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(object : SimpleCallBack<LotteryRecent>() {
                    override fun onError(e: ApiException?) {

                    }

                    override fun onSuccess(t: LotteryRecent?) {

                    }
                })
        }
    }
}

data class LotteryRecent(
    @SerializedName("joined")
    var joined: Boolean = false,
    @SerializedName("period")
    var period: Int = 0,
    @SerializedName("now")
    var now: String = ""
) : BaseCustomViewModel()