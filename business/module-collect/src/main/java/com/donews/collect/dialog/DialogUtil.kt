package com.donews.collect.dialog

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity

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
        dialogCancel:() -> Unit = {},
        dialogBtn:() -> Unit = {},
    ) {
        if (goodDialog != null && goodDialog?.dialog != null && goodDialog?.dialog!!.isShowing) {
            return
        }
        goodDialog = GoodDialog.newInstance(goodJson).apply {
            setOnDismissListener {
                goodDialog = null
            }
            clickDialogBtn = {}
            clickDialogCancel = {}
        }

        goodDialog?.showAllowingStateLoss(activity.supportFragmentManager, GoodDialog::class.simpleName)

    }

}