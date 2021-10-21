package com.donews.main.dialog

import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogRemindBinding

/**
 * 温馨提示弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 20:31
 */
class RemindDialog : AbstractFragmentDialog<MainExitDialogRemindBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_remind
    }

    override fun initView() {

    }

    override fun isUseDataBinding(): Boolean {
        return true
    }
}