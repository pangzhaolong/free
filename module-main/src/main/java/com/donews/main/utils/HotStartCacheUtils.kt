package com.donews.main.utils

import androidx.fragment.app.FragmentActivity
import com.donews.base.base.AppManager
import com.donews.main.dialog.HotStartDialog

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/12/9 10:10
 */
object HotStartCacheUtils {

    private var mHotStartDialog: HotStartDialog? = null

    fun addHotStartAdDialog() {
        var activity = AppManager.getInstance().topActivity
        if (activity !is FragmentActivity) {
            return
//            activity = AppManager.getInstance().secondActivity
//            if (activity !is FragmentActivity) {
//                return
//            }
        }

        if (mHotStartDialog != null && mHotStartDialog?.dialog != null && mHotStartDialog?.dialog?.isShowing == true) {
            return
        }
        mHotStartDialog = HotStartDialog.newInstance()
        mHotStartDialog!!.showAllowingStateLoss(activity.supportFragmentManager, "HotStartDialog")
    }

    fun loadAd() {
        if (mHotStartDialog != null && mHotStartDialog!!.isAdded) {
            mHotStartDialog!!.preloadFirstAd()
        } else {
            clear()
        }
    }

    fun showAd() {
        if (mHotStartDialog == null || mHotStartDialog?.dialog == null || mHotStartDialog?.dialog?.isShowing == true) {
            return
        }
        mHotStartDialog!!.showAd()
    }

    fun dismiss() {
        if (mHotStartDialog == null) {
            return
        }
        mHotStartDialog!!.dismiss()
    }

    fun clear() {
        mHotStartDialog = null
    }
}