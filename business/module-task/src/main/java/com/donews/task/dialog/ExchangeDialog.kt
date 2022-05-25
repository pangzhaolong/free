package com.donews.task.dialog

import android.animation.ObjectAnimator
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.task.R
import com.donews.task.databinding.TaskDialogExchangeBinding
import com.donews.task.databinding.TaskDialogRuleBinding
import com.donews.task.util.AnimationUtil

/**
 *  make in st
 *  on 2022/5/9 11:35
 */
class ExchangeDialog : AbstractFragmentDialog<TaskDialogExchangeBinding>(false, false) {

    companion object {
        fun newInstance(): ExchangeDialog {
            return ExchangeDialog()
        }
    }

    override fun getLayoutId() = R.layout.task_dialog_exchange

    override fun getThemeStyle(): Int {
        return R.style.TaskDialogStyle
    }

    override fun initView() {
        dataBinding.eventListener = EventListener()
        rotate = AnimationUtil.rotate(dataBinding?.rotateView)
    }

    override fun isUseDataBinding() = true

    private var rotate: ObjectAnimator? = null

    var clickDialogBtn: () -> Unit = {}

    inner class EventListener {
        fun cancelBtn(view: View) {
            disMissDialog()
        }
        fun exchangeBtn(view: View) {
            clickDialogBtn.invoke()
            disMissDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (rotate != null && rotate!!.isRunning) {
            rotate?.cancel()
            rotate = null
        }
    }

}