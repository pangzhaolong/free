package com.donews.main.dialog

import android.os.Bundle
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogOpenRedPacketBinding
import com.donews.main.entitys.resps.OpenRedPacketConfig

/**
 * 拦截开红包中奖弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 20:24
 */
class OpenRedPacketDialog : AbstractFragmentDialog<MainExitDialogOpenRedPacketBinding>() {

    companion object {
        fun newInstance(openRedPacketConfig: OpenRedPacketConfig): OpenRedPacketDialog {
            val args = Bundle()
            args.putSerializable("config", openRedPacketConfig)
            val fragment = OpenRedPacketDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_open_red_packet
    }

    override fun initView() {

    }

    override fun isUseDataBinding(): Boolean {
        return true
    }
}