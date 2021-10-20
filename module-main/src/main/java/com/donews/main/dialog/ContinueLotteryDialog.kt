package com.donews.main.dialog

import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogContinueLotteryBinding

/**
 * 拦截弹窗之继续抽奖
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 20:28
 */
class ContinueLotteryDialog : AbstractFragmentDialog<MainExitDialogContinueLotteryBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_continue_lottery
    }

    override fun initView() {

    }

    override fun isUseDataBinding(): Boolean {
        return true
    }
}