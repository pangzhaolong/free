package com.donews.collect.util

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import com.donews.collect.dialog.ChangeDialog
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

}