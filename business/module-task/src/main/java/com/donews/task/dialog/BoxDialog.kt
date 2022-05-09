package com.donews.task.dialog

import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.task.R
import com.donews.task.databinding.TaskDialogBoxBinding
import com.donews.task.databinding.TaskDialogExchangeBinding
import com.donews.task.databinding.TaskDialogRuleBinding

/**
 *  make in st
 *  on 2022/5/9 11:35
 */
class BoxDialog : AbstractFragmentDialog<TaskDialogBoxBinding>(false, false) {

    companion object {
        fun newInstance(): BoxDialog {
            return BoxDialog()
        }
    }

    override fun getLayoutId() = R.layout.task_dialog_box

    override fun initView() {
        dataBinding.eventListener = EventListener()
        dataBinding.tvEnd.text = "1点活跃度"
        dataBinding.tvEnd.text = "1点幸运值"
    }

    override fun isUseDataBinding() = true

    var clickDialogBtn: () -> Unit = {}

    inner class EventListener {
        fun cancelBtn(view: View) {
            disMissDialog()
        }
        fun receiveBtn(view: View) {
            clickDialogBtn.invoke()
            disMissDialog()
        }
    }

}