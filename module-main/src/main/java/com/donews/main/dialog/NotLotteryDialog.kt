package com.donews.main.dialog

import android.os.Bundle
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogNotLotteryBinding
import com.donews.main.entitys.resps.NotLotteryConfig

/**
 * 退出拦截，用户没有抽奖的情况下显示的弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 9:48
 */
class NotLotteryDialog : AbstractFragmentDialog<MainExitDialogNotLotteryBinding>() {

    companion object {
        fun newInstance(notLotteryConfig: NotLotteryConfig): NotLotteryDialog {
            val args = Bundle()
            args.putSerializable("config", notLotteryConfig)
            val fragment = NotLotteryDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_not_lottery
    }

    override fun initView() {

    }

}