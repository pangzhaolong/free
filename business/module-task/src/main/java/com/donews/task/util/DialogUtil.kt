package com.donews.task.util

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import com.donews.task.dialog.BoxDialog
import com.donews.task.dialog.ExchangeDialog
import com.donews.task.dialog.RuleDialog

/**
 *  make in st
 *  on 2022/5/9 12:18
 */
@SuppressLint("StaticFieldLeak")
object DialogUtil {

    private var ruleDialog: RuleDialog? = null

    fun showRuleDialog(
        activity: FragmentActivity
    ) {
        if (ruleDialog != null && ruleDialog?.dialog != null && ruleDialog?.dialog!!.isShowing) {
            return
        }
        ruleDialog = RuleDialog.newInstance().apply {
            setOnDismissListener {
                ruleDialog = null
            }
        }

        ruleDialog?.showAllowingStateLoss(activity.supportFragmentManager, RuleDialog::class.simpleName)

    }

    private var exchangeDialog: ExchangeDialog? = null

    fun showExchangeDialog(
        activity: FragmentActivity,
        clickBtnCall: () -> Unit = {}
    ) {
        if (exchangeDialog != null && exchangeDialog?.dialog != null && exchangeDialog?.dialog!!.isShowing) {
            return
        }
        exchangeDialog = ExchangeDialog.newInstance().apply {
            setOnDismissListener {
                exchangeDialog = null
            }
            clickDialogBtn = {
                clickBtnCall.invoke()
            }
        }

        exchangeDialog?.showAllowingStateLoss(activity.supportFragmentManager, ExchangeDialog::class.simpleName)

    }

    private var boxDialog: BoxDialog? = null

    fun showBoxDialog(
        activity: FragmentActivity,
        clickBtnCall: () -> Unit = {}
    ) {
        if (boxDialog != null && boxDialog?.dialog != null && boxDialog?.dialog!!.isShowing) {
            return
        }
        boxDialog = BoxDialog.newInstance().apply {
            setOnDismissListener {
                boxDialog = null
            }
            clickDialogBtn = {
                clickBtnCall.invoke()
            }
        }

        boxDialog?.showAllowingStateLoss(activity.supportFragmentManager, BoxDialog::class.simpleName)

    }

}