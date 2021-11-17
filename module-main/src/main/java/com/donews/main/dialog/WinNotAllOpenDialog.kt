package com.donews.main.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogRedPacketNotAllOpenBinding
import com.donews.utilslibrary.utils.KeySharePreferences
import com.donews.utilslibrary.utils.KeySharePreferences.STEPS_TO_GOLD_RED_PACKAGE_COUNTS
import com.donews.utilslibrary.utils.SPUtils

/**
 * 参与了抽奖，但是红包未全部领取
 *
 * @author lcl
 * @version v1.0
 */
class WinNotAllOpenDialog : AbstractFragmentDialog<MainExitDialogRedPacketNotAllOpenBinding>() {

    companion object {
        fun newInstance(): WinNotAllOpenDialog {
            val args = Bundle()
            val fragment = WinNotAllOpenDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_red_packet_not_all_open
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        dataBinding.eventListener = EventListener()
        dataBinding.tvZjglNum.text = "${SPUtils.getInformain(STEPS_TO_GOLD_RED_PACKAGE_COUNTS, 0)}"
        showCloseBtn()
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed({
            dataBinding.ivClose.visibility = View.VISIBLE
            dataBinding.tvFailure.visibility = View.VISIBLE
        }, 1000L)
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

        fun onLaterListener(view: View) {
            if (view.visibility == View.VISIBLE) {
                onLaterListener?.onClose()
            }
        }
    }
}