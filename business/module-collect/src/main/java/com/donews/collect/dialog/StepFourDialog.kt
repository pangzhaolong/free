package com.donews.collect.dialog

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.collect.R
import com.donews.collect.databinding.CollectDialogStepFourBinding
import com.donews.collect.databinding.CollectDialogStepThreeBinding
import com.donews.collect.databinding.CollectDialogStepTwoBinding
import com.donews.collect.util.AnimationUtil
import java.lang.Exception

/**
 *  make in st
 *  on 2022/5/18 10:23
 */
class StepFourDialog : AbstractFragmentDialog<CollectDialogStepFourBinding>(false, false) {

    companion object {
        fun newInstance(): StepFourDialog {
            return StepFourDialog()
        }
    }

    override fun getLayoutId() = R.layout.collect_dialog_step_four

    override fun getThemeStyle(): Int {
        return R.style.CollectDialogStyle
    }

    override fun initView() {
        dataBinding.eventListener = EventListener()
        mHandler.postDelayed(timer,1000L)
        rotate = AnimationUtil.rotate(dataBinding?.rotateView)
        AnimationUtil.rotate(dataBinding?.rotateView)
        //难度低于用户数，从18000-58000随机取数
        val res = (Math.random() * (58000 - 18000 + 1) + 18000).toString()
        dataBinding?.tvRight?.text = res
    }

    override fun isUseDataBinding() = true

    private var rotate: ObjectAnimator? = null

    inner class EventListener {
        fun receiveBtn(view: View) {
            mHandler.removeCallbacks(timer)
            disMissDialog()
        }
    }

    val mHandler = Handler(Looper.getMainLooper())

    //倒计时
    private var curTime = 5
    private val timer = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                if (curTime > 0) {
                    curTime--
                    dataBinding?.rightTv?.text = "(${curTime}s)"
                    if (curTime == 0) {
                        mHandler.removeCallbacks(this)
                        disMissDialog()
                    } else {
                        mHandler.postDelayed(this, 1000L)
                    }
                }
            } catch (e: Exception){}
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacksAndMessages(null)
        if (rotate != null && rotate!!.isRunning){
            rotate?.cancel()
            rotate = null
        }
    }

}