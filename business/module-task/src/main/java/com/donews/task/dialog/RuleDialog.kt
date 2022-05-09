package com.donews.task.dialog

import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.task.R
import com.donews.task.databinding.TaskDialogRuleBinding

/**
 *  make in st
 *  on 2022/5/9 11:35
 */
class RuleDialog : AbstractFragmentDialog<TaskDialogRuleBinding>(false, false) {

    companion object {
        fun newInstance(): RuleDialog {
            return RuleDialog()
        }
    }

    override fun getLayoutId() = R.layout.task_dialog_rule

    override fun initView() {
        dataBinding.eventListener = EventListener()
    }

    override fun isUseDataBinding() = true

    inner class EventListener {
        fun cancelBtn(view: View) {
            disMissDialog()
        }
    }

}