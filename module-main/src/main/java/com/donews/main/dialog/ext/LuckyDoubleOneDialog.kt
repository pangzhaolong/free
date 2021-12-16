package com.donews.main.dialog.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.LuckyDoubleOneDialogLayoutBinding
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 幸运翻倍只差一步
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/16
 */
class LuckyDoubleOneDialog(
    /** 倒计时时长 */
    var downTimeCount: Int = 4 //倒计时三秒
) : AbstractFragmentDialog<LuckyDoubleOneDialogLayoutBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val handler = Handler(Looper.getMainLooper())
    private var timeTask: Runnable? = null
//    private lateinit var addCoinsAnim: ObjectAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.lucky_double_one_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
        timeTask = Runnable {
            downTimeCount--
            dataBinding.mainLuckyLaterTv.text = "${downTimeCount}S后自动跳转"
            if (downTimeCount > 0) {
                handler.postDelayed(timeTask!!, 1000)
            } else {
                dataBinding.tvBut.performClick()
                dismiss()
            }
        }
        handler.post(timeTask!!)
        setOnDismissListener {
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
        //手
//        dataBinding.maskingHand.imageAssetsFolder = "images"
//        dataBinding.maskingHand.setAnimation("lottery_finger.json")
//        dataBinding.maskingHand.loop(true)
//        dataBinding.maskingHand.playAnimation()
    }

    override fun onResume() {
        super.onResume()
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
}