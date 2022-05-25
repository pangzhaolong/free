package com.donews.home.dialogs

import android.text.Html
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.home.R
import com.donews.home.databinding.MainTaskDialogRuleBinding

/**
 *  make in st
 *  on 2022/5/9 11:35
 */
class ExchangeRuleDialog : AbstractFragmentDialog<MainTaskDialogRuleBinding>(false, false) {

    companion object {
        fun newInstance(): ExchangeRuleDialog {
            return ExchangeRuleDialog()
        }
    }

    override fun getLayoutId() = R.layout.main_task_dialog_rule

    override fun getThemeStyle(): Int {
        return R.style.MainTaskDialogStyle
    }

    override fun initView() {
        dataBinding.eventListener = EventListener()
        dataBinding.tvDesc.text = Html.fromHtml(getString(R.string.main_str_exchange_rule_msg))
    }

    override fun isUseDataBinding() = true

    inner class EventListener {
        fun cancelBtn(view: View) {
            disMissDialog()
        }
    }

}