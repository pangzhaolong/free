package com.donews.main.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.ToastUtil
import com.donews.main.BuildConfig
import com.donews.main.R
import com.donews.main.databinding.MainMoreAwardDialogLayoutBinding
import com.donews.middle.bean.RestIdBean
import com.donews.middle.bean.front.DoubleRedPacketBean
import com.donews.middle.utils.LottieUtil
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 额外获得奖励的弹窗
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/3
 */
class MoreAwardDialog(
        /** 金额 */
        var restId: String,
        var preId: String,
        var score: Float,
        var number: Float
) : AbstractFragmentDialog<MainMoreAwardDialogLayoutBinding>(),
        EasyPermissions.PermissionCallbacks {
    lateinit var eventListener: EventListener
    private val handler = Handler(Looper.getMainLooper())
    lateinit var cdt: CountDownTimer

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.main_more_award_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
        setOnDismissListener {
            handler.removeCallbacksAndMessages(null)
            if (eventListener != null) {
                eventListener.dismiss()
            }
        }
        dataBinding.mainDoubleRpGetLl.setOnClickListener {
            doubleGetRp()
            dismiss()
        }
        dataBinding.mainDoubleCloseIv.setOnClickListener {
            dismiss()
        }

        LottieUtil.initLottieView(dataBinding.mainMoreAwardHandLav)

        cdt = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                dataBinding.mainDoubleCloseIv.visibility = View.VISIBLE
            }
        }
        cdt.start()
    }

    fun doubleGetRp() {
        EasyHttp.post(BuildConfig.API_WALLET_URL + "v1/double-red-packet")
                .upObject(RestIdBean(restId, preId))
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(object : SimpleCallBack<DoubleRedPacketBean?>() {
                    override fun onError(e: ApiException) {
                    }

                    override fun onSuccess(t: DoubleRedPacketBean?) {
                        ToastUtil.show(context, "双倍红包领取成功")
                    }
                })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        cdt.cancel()
        LottieUtil.cancelLottieView(dataBinding.mainMoreAwardHandLav)
    }

    override fun isUseDataBinding(): Boolean {
        return true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    interface EventListener {
        fun dismiss()
    }
}