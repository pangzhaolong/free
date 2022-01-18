package com.donews.main.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import com.dn.events.events.DoubleRpEvent
import com.dn.sdk.listener.IAdRewardVideoListener
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.ToastUtil
import com.donews.common.ad.cache.AdVideoCacheUtils.showRewardVideo
import com.donews.main.R
import com.donews.main.databinding.MainMoreAwardDialogLayoutBinding
import com.donews.middle.utils.LottieUtil
import com.vmadalin.easypermissions.EasyPermissions
import org.greenrobot.eventbus.EventBus


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
        var restScore: Float
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
            doubleRp()
            dismiss()
        }
        dataBinding.mainDoubleCloseIv.setOnClickListener {
            EventBus.getDefault().post(DoubleRpEvent(7, 0.1f, "", "", 0f))
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

    private var mIsVerify: Boolean = false
    private fun doubleRp() {
        ToastUtil.show(context, "视频加载中...")
        val listener: IAdRewardVideoListener = object : IAdRewardVideoListener {
            override fun onAdStartLoad() {}
            override fun onAdStatus(code: Int, any: Any?) {}
            override fun onAdLoad() {}
            override fun onAdShow() {
            }

            override fun onAdVideoClick() {}
            override fun onRewardVerify(result: Boolean) {
                mIsVerify = result
            }

            override fun onAdClose() {
                if (!mIsVerify) {
                    ToastUtil.show(context, "未看完视频，不能领取更多红包")
                } else {
                    EventBus.getDefault().post(DoubleRpEvent(8, score, restId, preId, restScore))
                    dismiss()
                }
            }

            override fun onVideoCached() {}
            override fun onVideoComplete() {}
            override fun onAdError(code: Int, errorMsg: String?) {
                ToastUtil.showShort(context, "视频加载失败，点击领取更多重试")
            }
        }
        showRewardVideo(listener)
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