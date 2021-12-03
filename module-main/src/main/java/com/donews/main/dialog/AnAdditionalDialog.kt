package com.donews.main.dialog

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.BuildConfig
import com.donews.main.R
import com.donews.main.bean.RecentLotteryInfoBean
import com.donews.main.databinding.AnAdditionalDialogLayoutBinding
import com.donews.main.databinding.DrawDialogLayoutBinding
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.DateManager
import com.orhanobut.logger.Logger
import com.vmadalin.easypermissions.EasyPermissions
import java.util.*


/**
 * 额外获得奖励的弹窗
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/3
 */
class AnAdditionalDialog(
    /** 金额 */
    val number: String
) : AbstractFragmentDialog<AnAdditionalDialogLayoutBinding>(),
    EasyPermissions.PermissionCallbacks {
    lateinit var eventListener: EventListener
    private val handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun getLayoutId(): Int {
        return R.layout.an_additional_dialog_layout
    }

    override fun initView() {
        dataBinding.tvNum.text = number
        setOnDismissListener {
            if (eventListener != null) {
                eventListener.dismiss()
            }
        }
        dataBinding.butSx.setOnClickListener {
            dismiss()
        }
        handler.postDelayed({
            val arr = arrayOf(dataBinding.ivYh0, dataBinding.ivYh1)
            var pd = 100L
            for (imageView in arr) {
                handler.postDelayed({
                    if (activity != null) {
                        val anim = AnimationUtils.loadAnimation(
                            activity,
                            R.anim.anim_yh_in
                        )
                        anim.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation?) {
                            }

                            override fun onAnimationEnd(animation: Animation?) {
                                imageView.visibility = View.INVISIBLE
                            }

                            override fun onAnimationRepeat(animation: Animation?) {
                            }
                        })
                        anim.repeatCount = 1
                        imageView.startAnimation(anim)
                        imageView.visibility = View.VISIBLE
                    }
                }, pd)
                pd += Random().nextInt(200) + 1000
            }
        }, 150)
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