package com.donews.main.dialog.ext

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.ToastUtil
import com.donews.main.BuildConfig
import com.donews.main.R
import com.donews.main.databinding.AnAdditionalDialogLayoutBinding
import com.donews.main.databinding.GoodLuckDoubleDialogLayoutBinding
import com.donews.main.utils.AndroidProcessesUtils
import com.donews.middle.bean.RestIdBean
import com.donews.middle.bean.front.DoubleRedPacketBean
import com.donews.network.EasyHttp
import com.donews.network.cache.model.CacheMode
import com.donews.network.callback.SimpleCallBack
import com.donews.network.exception.ApiException
import com.donews.utilslibrary.utils.SoundHelp
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 好运翻倍的弹窗
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/3
 */
class GoodLuckDoubleDialog(
    /** 看视频次数 */
    var count: String,
    /** 倒计时时长 */
    var downTimeCount: Int = 4 //倒计时三秒
) : AbstractFragmentDialog<GoodLuckDoubleDialogLayoutBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val handler = Handler(Looper.getMainLooper())
    private var timeTask: Runnable? = null
//    private lateinit var addCoinsAnim: ObjectAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.good_luck_double_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
        timeTask = Runnable {
            downTimeCount--
            dataBinding.tvButTime.text = "${downTimeCount}S"
            if (downTimeCount > 0) {
                handler.postDelayed(timeTask!!, 1000)
            } else {
                dataBinding.tvBut.performClick()
                dismiss()
            }
        }
        handler.post(timeTask!!)
        SoundHelp.newInstance().init(context)
        SoundHelp.newInstance().onStart()
        dataBinding.tvCount.text = Html.fromHtml("仅看 <font color='#FFDC9F'>${count}</font> 个视频")
        setOnDismissListener {
            dataBinding.vLuckFg.clearAnimation()
            handler.removeCallbacksAndMessages(null)
            timeTask?.apply {
                handler.removeCallbacks(this)
            }
        }
        dataBinding.tvBut.setOnClickListener {
            onSureListener?.onSure() ?: disMissDialog()
        }
        dataBinding.mainDoubleCloseIv.setOnClickListener {
            dismiss()
        }
        //调整背景光晕的位置
        val wh = (ScreenUtils.getScreenWidth() * 1.15).toInt()
        val lp: ConstraintLayout.LayoutParams =
            (dataBinding.vLuckFg.layoutParams ?: ConstraintLayout.LayoutParams(
                wh, wh
            )) as ConstraintLayout.LayoutParams
        lp.width = wh
        lp.height = wh
        lp.topMargin = -ConvertUtils.dp2px(3F)
        dataBinding.vLuckFg.layoutParams = lp
        //手
        dataBinding.maskingHand.imageAssetsFolder = "images"
        dataBinding.maskingHand.setAnimation("lottery_finger.json")
        dataBinding.maskingHand.loop(true)
        dataBinding.maskingHand.playAnimation()
    }

    override fun onResume() {
        super.onResume()
        addAmin()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun isUseDataBinding(): Boolean {
        return true
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
        val anim = AnimationUtils.loadAnimation(activity, R.anim.anim_main_good_luck_bg)
        dataBinding.vLuckFg.startAnimation(anim)
//        val addCoinsAnim: ObjectAnimator =
//            ObjectAnimator.ofFloat(dataBinding.vLuckFg, "rotation", 0f, 360f)
//        addCoinsAnim.interpolator = LinearInterpolator()
//        addCoinsAnim.repeatCount = -1
//        addCoinsAnim.duration = 18000
//        addCoinsAnim.start()
    }
}