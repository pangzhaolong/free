package com.donews.main.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogOpenRedPacketBinding
import com.donews.main.entitys.resps.NotLotteryConfig
import com.donews.main.entitys.resps.OpenRedPacketConfig
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.SPUtils

/**
 * 红包未全部开启时候的弹窗
 *
 * @author lcl
 * @version v1.0
 */
class OpenRedPacketDialog : AbstractFragmentDialog<MainExitDialogOpenRedPacketBinding>() {

    companion object {

        const val PARAMS_CONFIG = "config"

        fun newInstance(openRedPacketConfig: OpenRedPacketConfig): OpenRedPacketDialog {
            val args = Bundle()
            args.putSerializable(PARAMS_CONFIG, openRedPacketConfig)
            val fragment = OpenRedPacketDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var openRedPacketConfig: OpenRedPacketConfig

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            openRedPacketConfig =
                it.getSerializable(NotLotteryDialog.PARAMS_CONFIG) as OpenRedPacketConfig
        } ?: kotlin.run {
            openRedPacketConfig = OpenRedPacketConfig()
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_open_red_packet
    }

    override fun initView() {
        val number = SPUtils.getInformain(KeySharePreferences.CLOSE_RED_PACKAGE_COUNTS, 1)
        dataBinding.redPacketNumber = number
        dataBinding.eventListener = EventListener()
        showCloseBtn()
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed({
            dataBinding.ivClose.visibility = View.VISIBLE
            dataBinding.tvFailure.visibility = View.VISIBLE
        }, openRedPacketConfig.closeBtnLazyShow * 1000L)
    }


    inner class EventListener {
        fun clickTvClose(view: View) {
            onCancelListener?.onCancel()
        }

        fun clickIvClose(view: View) {
            if (view.visibility == View.VISIBLE) {
                onCloseListener?.onClose()
            }
        }

        fun clickOpenPacket(view: View) {
            onSureListener?.onSure()
        }

        fun clickLater(view: View) {
            if (view.visibility == View.VISIBLE) {
                onLaterListener?.onClose()
            }
        }
    }
}