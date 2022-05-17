package com.donews.collect.util

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import com.donews.collect.dialog.ChangeDialog
import com.donews.collect.dialog.DrawDialog
import com.donews.collect.dialog.FailDialog
import com.donews.collect.dialog.GoodDialog

/**
 *  make in st
 *  on 2022/5/16 17:16
 */
@SuppressLint("StaticFieldLeak")
object DialogUtil {

    private var goodDialog: GoodDialog? = null

    fun showGoodDialog(
        activity: FragmentActivity,
        goodJson:String,
        dis:() -> Unit = {},
        dialogCancel:() -> Unit = {},
        dialogBtn:(goodId: String) -> Unit = {},
    ) {
        if (goodDialog != null && goodDialog?.dialog != null && goodDialog?.dialog!!.isShowing) {
            return
        }
        goodDialog = GoodDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                dis.invoke()
                goodDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke(it)
            }
            clickDialogCancel = {
                dialogCancel.invoke()
            }
        }

        goodDialog?.showAllowingStateLoss(activity.supportFragmentManager, GoodDialog::class.simpleName)

    }

    private var changeDialog: ChangeDialog? = null

    fun showChangeGoodDialog(
        activity: FragmentActivity,
        goodJson:String,
        dialogBtn:(cardId: String) -> Unit = {},
    ) {
        if (changeDialog != null && changeDialog?.dialog != null && changeDialog?.dialog!!.isShowing) {
            return
        }
        changeDialog = ChangeDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                changeDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke(it)
            }
        }

        changeDialog?.showAllowingStateLoss(activity.supportFragmentManager, ChangeDialog::class.simpleName)

    }

    private var drawDialog: DrawDialog? = null

    fun showDrawDialog(
        activity: FragmentActivity,
        goodJson:String,
        dialogBtn:() -> Unit = {},
    ) {
        if (drawDialog != null && drawDialog?.dialog != null && drawDialog?.dialog!!.isShowing) {
            return
        }
        drawDialog = DrawDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                drawDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke()
            }
        }

        drawDialog?.showAllowingStateLoss(activity.supportFragmentManager, DrawDialog::class.simpleName)

    }

    private var failDialog: FailDialog? = null

    fun showFailDialog(
        activity: FragmentActivity,
        goodJson:String,
        dialogBtn:() -> Unit = {},
    ) {
        if (failDialog != null && failDialog?.dialog != null && failDialog?.dialog!!.isShowing) {
            return
        }
        failDialog = FailDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                failDialog = null
            }
            clickDialogBtn = {
                dialogBtn.invoke()
            }
        }

        failDialog?.showAllowingStateLoss(activity.supportFragmentManager, FailDialog::class.simpleName)

    }

}