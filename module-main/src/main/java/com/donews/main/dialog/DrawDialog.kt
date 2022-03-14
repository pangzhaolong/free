package com.donews.main.dialog

import android.app.Activity
import android.content.Context
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.BuildConfig
import com.donews.main.R
import com.donews.main.bean.RecentLotteryInfoBean
import com.donews.main.databinding.DrawDialogLayoutBinding
import com.donews.main.utils.ExitInterceptUtils
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.DateManager
import com.orhanobut.logger.Logger
import com.vmadalin.easypermissions.EasyPermissions
import java.util.*


/**
 * 开奖弹出框
 *
 * @author hegai
 * @version v1.0
 * @date 2021/10/20 20:31
 */
class DrawDialog : AbstractFragmentDialog<DrawDialogLayoutBinding>(),
        EasyPermissions.PermissionCallbacks {
    private var FATHER_URL = BuildConfig.API_LOTTERY_URL
    var RECENT_LOTTERY = FATHER_URL + "v1/recent-lottery"
    private lateinit var data: RecentLotteryInfoBean
    lateinit var eventListener: EventListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun getLayoutId(): Int {
        return R.layout.draw_dialog_layout
    }

    override fun initView() {
        dataBinding.ivClose.setOnClickListener {
            dismissDialog()
            activity?.apply {
                if (activity is Activity) {
                    ExitInterceptUtils.closeExitDialog(requireActivity())
                }
            }
        }
        dataBinding.checkButton.setOnClickListener {
            if (eventListener != null) {
                eventListener.switchPage()
            }
            dismiss()
        }
        try {
            if (data != null) {
                dataBinding.lotteryInfo = data
            }
        } catch (e: java.lang.Exception) {
        }
    }


    override fun isUseDataBinding(): Boolean {
        return true
    }


    private fun dismissDialog() {
        if (eventListener != null) {
            eventListener.dismiss()
        } else {
            dismiss()
        }
    }

    fun requestGoodsInfo(context: Context) {
        if (DateManager.getInstance().ifFirst(DateManager.DRAW_DIALOG_KEY)) {
            val disposable = EasyHttp.get(RECENT_LOTTERY)
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("days", "0")
                    .execute(object : SimpleCallBack<RecentLotteryInfoBean>() {
                        override fun onError(e: ApiException?) {
                            dismissDialog()
                            Logger.d("" + e)
                        }

                        override fun onSuccess(t: RecentLotteryInfoBean?) {
                            if (eventListener != null && t != null && t.joined) {
                                try {
                                    var time = (t.now + "").toLong() * 1000
                                    var calendar = Calendar.getInstance()
                                    calendar.timeInMillis = time.toLong()
                                    var hour = calendar.get(Calendar.HOUR_OF_DAY)
                                    var minute = calendar.get(Calendar.MINUTE)
                                    //判断是否是今天首次
                                    if (hour > 9 || (hour >= 9 && minute >= 58)) {
                                        data = t
                                        eventListener.show()
                                    } else {
                                        dismissDialog()
                                    }
                                } catch (e: Exception) {
                                    Logger.d("" + e.message)
                                    dismissDialog()
                                }
                            } else {
                                dismissDialog()
                            }
                        }
                    })
            addDisposable(disposable)
        } else {
//            Toast.makeText(context, "今天不能在弹起", Toast.LENGTH_SHORT).show()
            dismissDialog()
        }

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }


    interface EventListener {
        fun dismiss()
        fun show()
        fun switchPage()
    }

}