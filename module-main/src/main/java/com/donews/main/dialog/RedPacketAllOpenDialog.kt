package com.donews.main.dialog

import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogRedPacketAllOpenBinding
import kotlin.random.Random
import java.text.DecimalFormat


/**
 * 参与了抽奖。并且红包全开的弹窗
 *
 * @author lcl
 * @version v1.0
 */
class RedPacketAllOpenDialog : AbstractFragmentDialog<MainExitDialogRedPacketAllOpenBinding>() {

    companion object {
        fun newInstance(): RedPacketAllOpenDialog {
            val args = Bundle()
            val fragment = RedPacketAllOpenDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private var isShowProgerssAnim = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_red_packet_all_open
    }

    override fun initView() {
        dataBinding.eventListener = EventListener()
        showCloseBtn()
    }

    override fun onResume() {
        super.onResume()
        setProgess()
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed({
            dataBinding.ivClose.visibility = View.VISIBLE
            dataBinding.tvFailure.visibility = View.VISIBLE
        }, 2300L)
    }

    private fun setProgess() {
        if (isShowProgerssAnim) {
            return
        }
        isShowProgerssAnim = true
        val df = DecimalFormat("#.0")
        val max = ((Random.nextDouble(0.31) + 0.5) * 100)
        val anim = ValueAnimator.ofFloat(0F, max.toFloat())
        var curValue = 0F
        val processWid = ConvertUtils.dp2px(243F) -
                ConvertUtils.dp2px(26F) * 2
        anim.addUpdateListener {
            curValue = it.animatedValue as Float
            dataBinding.mainExitDialogProgressBar.progress = curValue.toInt()
            dataBinding.mainExitQFloat.setPadding(
                (processWid * (curValue / 100)).toInt(),
                dataBinding.mainExitQFloat.paddingTop,
                dataBinding.mainExitQFloat.paddingRight,
                dataBinding.mainExitQFloat.paddingBottom
            )
            dataBinding.tvZjglNum.text = df.format(curValue) + "%"
        }
        //放大动画
        val scAnim = ScaleAnimation(
            1.0f, 1.3f, 1.0f, 1.3f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        scAnim.duration = 500
        scAnim.repeatCount = 1
        scAnim.repeatMode = REVERSE
        anim.doOnEnd {
            dataBinding.mainExitQIv.clearAnimation()
            dataBinding.mainExitQIv.animation = scAnim
            scAnim.start()
        }
        anim.setDuration(800)
            .start()
    }

    inner class EventListener {
        fun clickTvClose(view: View) {
            onCancelListener?.onCancel()
        }

        fun clickIvClose(view: View) {
            if (view.visibility == View.VISIBLE) {
                onCloseListener?.onClose()
            }
        }

        fun clickOpenPacket(view: View) {
            onSureListener?.onSure()
        }

        fun onLaterListener(view: View) {
            if (view.visibility == View.VISIBLE) {
                onLaterListener?.onClose()
            }
        }
    }
}