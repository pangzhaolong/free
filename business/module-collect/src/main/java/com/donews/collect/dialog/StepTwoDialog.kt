package com.donews.collect.dialog

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.collect.R
import com.donews.collect.databinding.CollectDialogStepTwoBinding
import com.donews.collect.util.AnimationUtil
import com.donews.collect.util.DayStepUtil
import com.donews.utilslibrary.utils.SPUtils

/**
 *  make in st
 *  on 2022/5/18 10:23
 */
class StepTwoDialog : AbstractFragmentDialog<CollectDialogStepTwoBinding>(false, false) {

    companion object {
        fun newInstance(): StepTwoDialog {
            return StepTwoDialog()
        }
    }

    override fun getThemeStyle(): Int {
        return R.style.CollectDialogStyle
    }

    override fun getLayoutId() = R.layout.collect_dialog_step_two

    private var rotate: ObjectAnimator? = null

    override fun initView() {
        rotate = AnimationUtil.rotate(dataBinding?.rotateView)
        mHandler.postDelayed({
            val curNum = SPUtils.getInformain("todayShowTwoStepNum", 0)
            DayStepUtil.instance.setStepTwoSp(curNum + 1)
            disMissDialog()
        },3000L)
    }

    override fun isUseDataBinding() = true

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacksAndMessages(null)
        if (rotate != null && rotate!!.isRunning){
            rotate?.cancel()
            rotate = null
        }
    }

}