package com.donews.main.dialog

import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogNotLotteryBinding

/**
 * 退出拦截，用户没有抽奖的情况下显示的弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 9:48
 */
class NotLotteryDialog : AbstractFragmentDialog<MainExitDialogNotLotteryBinding>() {

    override fun isUseDataBinding(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_not_lottery
    }

    override fun initView() {

    }

}