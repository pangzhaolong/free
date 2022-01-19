package com.donews.main.dialog

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.ToastUtil
import com.donews.main.BuildConfig
import com.donews.main.R
import com.donews.main.databinding.AnAdditionalDialogLayoutBinding
import com.donews.middle.bean.RestIdBean
import com.donews.middle.bean.front.DoubleRedPacketBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.SoundHelp
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 额外获得奖励的弹窗
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/3
 */
class AnAdditionalDialog(
        var from: Int,  // 0: 普通；1: 通知栏来的
        /** 金额 */
        var restId: String,
        var preId: String,
        var score: Float,
        var number: Float,
        var count: Int = 4 //倒计时三秒
) : AbstractFragmentDialog<AnAdditionalDialogLayoutBinding>(),
        EasyPermissions.PermissionCallbacks {
    lateinit var eventListener: EventListener
    private val handler = Handler(Looper.getMainLooper())
    private var timeTask: Runnable? = null
//    private lateinit var addCoinsAnim: ObjectAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.an_additional_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
        timeTask = Runnable {
            count--
            dataBinding.mainDoubleGetTv.text = "(${count}s)"
            if (count > 0) {
                handler.postDelayed(timeTask!!, 1000)
            } else {
                doubleRp()
                dismiss()
            }
        }
        handler.post(timeTask!!)
        SoundHelp.newInstance().init(context)
        SoundHelp.newInstance().onStart()
        dataBinding.tvNum.text = score.toString()
        dataBinding.mainDoubleAddCoinsTv.text = String.format("%.02f", number)
        setOnDismissListener {
            handler.removeCallbacksAndMessages(null)
            timeTask?.apply {
                handler.removeCallbacks(this)
            }
            if (eventListener != null) {
                eventListener.dismiss()
            }
        }
        dataBinding.mainDoubleRpGetLl.setOnClickListener {
            doubleRp()
            SoundHelp.newInstance().onRelease()
            dismiss()
        }
        dataBinding.mainDoubleCloseIv.setOnClickListener {
            SoundHelp.newInstance().onRelease()
            dismiss()
        }

        val addCoinsAnim: ObjectAnimator = ObjectAnimator.ofFloat(dataBinding.mainDoubleAddCoinsTv, "translationY", 0f, -200f)
        addCoinsAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                dataBinding.mainDoubleAddCoinsTv.visibility = View.GONE
                var total = score + number
                dataBinding.tvNum.text = String.format("%.02f", total)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        addCoinsAnim.interpolator = LinearInterpolator()
        addCoinsAnim.duration = 2000
        addCoinsAnim.start()
    }

    fun doubleRp() {
        if (from == 0) {
            doubleGetRp()
        } else if (from == 1) {
            notifyDoubleGetRp()
        }
    }

    private fun doubleGetRp() {
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

    private fun notifyDoubleGetRp() {
        EasyHttp.post(BuildConfig.API_WALLET_URL + "v1/rest-red-packet")
                .upJson("{\"rest_id\": \"$restId\"}")
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
        SoundHelp.newInstance().onRelease()
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