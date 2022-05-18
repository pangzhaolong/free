package com.donews.collect.dialog

import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.collect.R
import com.donews.collect.databinding.CollectDialogStepOneBinding
import com.donews.collect.util.AnimationUtil
import com.donews.collect.util.DayStepUtil
import com.donews.utilslibrary.utils.SPUtils

/**
 *  make in st
 *  on 2022/5/18 10:23
 */
class StepOneDialog : AbstractFragmentDialog<CollectDialogStepOneBinding>(false, false) {

    companion object {
        fun newInstance(): StepOneDialog {
            return StepOneDialog()
        }
    }

    override fun getThemeStyle(): Int {
        return R.style.CollectDialogStyle
    }

    override fun getLayoutId() = R.layout.collect_dialog_step_one

    override fun initView() {
        startAnimation()
    }

    private fun startAnimation(){
        AnimationUtil.startLottieAnimation(dataBinding?.lottieAnimation){
            val curNum = SPUtils.getInformain("todayShowOneStepNum", 0)
            DayStepUtil.instance.setStepOneSp(curNum + 1)
            cancelAnimation()
            disMissDialog()
        }
    }

    private fun cancelAnimation(){
        AnimationUtil.cancelLottieAnimation(dataBinding?.lottieAnimation)
    }

    override fun isUseDataBinding() = true

}