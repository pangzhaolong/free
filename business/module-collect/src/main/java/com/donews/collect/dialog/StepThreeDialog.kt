package com.donews.collect.dialog

import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.collect.R
import com.donews.collect.databinding.CollectDialogStepThreeBinding
import com.donews.collect.databinding.CollectDialogStepTwoBinding
import com.donews.collect.util.AnimationUtil
import com.donews.collect.util.DayStepUtil
import com.donews.utilslibrary.utils.SPUtils

/**
 *  make in st
 *  on 2022/5/18 10:23
 */
class StepThreeDialog : AbstractFragmentDialog<CollectDialogStepThreeBinding>(false, false) {

    companion object {
        fun newInstance(): StepThreeDialog {
            return StepThreeDialog()
        }
    }

    override fun getThemeStyle(): Int {
        return R.style.CollectDialogStyle
    }

    override fun getLayoutId() = R.layout.collect_dialog_step_three

    private var rotate: ObjectAnimator? = null

    override fun initView() {
        rotate = AnimationUtil.rotate(dataBinding?.rotateView)
        mHandler.postDelayed({
            val curNum = SPUtils.getInformain("todayShowThreeStepNum", 0)
            DayStepUtil.instance.setStepThreeSp(curNum + 1)
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