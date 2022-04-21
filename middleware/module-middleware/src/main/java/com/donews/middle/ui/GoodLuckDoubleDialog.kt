package com.donews.middle.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.donews.common.base.MvvmBaseLiveDataActivity
import com.donews.middle.R
import com.donews.middle.databinding.GoodLuckDoubleDialogLayoutBinding
import com.donews.middle.model.MiddleModel
import com.donews.middle.utils.CommonUtils
import com.donews.middle.viewmodel.MiddleViewModel
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 好运翻倍的弹窗
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/3
 */
class GoodLuckDoubleDialog() :
    MvvmBaseLiveDataActivity<GoodLuckDoubleDialogLayoutBinding, MiddleViewModel<*>?>(),
    EasyPermissions.PermissionCallbacks {
    private val handler = Handler(Looper.getMainLooper())
    private var timeTask: Runnable? = null
    private var downTimeCount: Int = 4;//倒计时三秒
    override fun getLayoutId(): Int {
        return R.layout.good_luck_double_dialog_layout
    }

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding.vLuckFg.clearAnimation()
        handler.removeCallbacksAndMessages(null)
        timeTask?.apply {
            handler.removeCallbacks(this)
        }
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
        timeTask = Runnable {
            downTimeCount--
            mDataBinding.tvButTime.text = "${downTimeCount}S"
            if (downTimeCount > 0) {
                handler.postDelayed(timeTask!!, 1000)
            } else {
                finishActivity()
            }
        }
        handler.post(timeTask!!)
        mDataBinding.tvBut.setOnClickListener {
            finishActivity()
        }
        //手
        mDataBinding.maskingHand.imageAssetsFolder = "images"
        mDataBinding.maskingHand.setAnimation("lottery_finger.json")
        mDataBinding.maskingHand.loop(true)
        mDataBinding.maskingHand.playAnimation()
    }

    private fun finishActivity() {
        //开始暴击模式
        ToastUtils.showShort("暴击模式已开启")
        CommonUtils.startCrit();
        finish()
    }

    override fun onResume() {
        super.onResume()
        addAmin()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
    }

    private var isAddAnim = false
    private fun addAmin() {
        if (isAddAnim) {
            return
        }
        isAddAnim = true
        val anim =
            AnimationUtils.loadAnimation(this@GoodLuckDoubleDialog, R.anim.anim_main_good_luck_bg)
        mDataBinding.vLuckFg.startAnimation(anim)
    }
}