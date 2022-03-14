package com.donews.main.dialog.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.text.Html
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.FreeChargeAndWelfareDialogLayoutBinding
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 免费福利页面(注意:全场商品免费领页面)
 *
 * @author lcl
 * @version v1.0
 * @date 2021/12/16
 */
class FreeChargeAndWelfareDialog(
) : AbstractFragmentDialog<FreeChargeAndWelfareDialogLayoutBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val handler = Handler(Looper.getMainLooper())
    private var timeTask: Runnable? = null
//    private lateinit var addCoinsAnim: ObjectAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getLayoutId(): Int {
        return R.layout.free_charge_and_welfare_dialog_layout
    }

    @SuppressLint("SetTextI18n", "ObjectAnimatorBinding")
    override fun initView() {
//        timeTask = Runnable {
//            downTimeCount--
//            dataBinding.mainLuckyLaterTv.text = "${downTimeCount}S后自动跳转"
//            if (downTimeCount > 0) {
//                handler.postDelayed(timeTask!!, 1000)
//            } else {
//                dataBinding.tvBut.performClick()
//                dismiss()
//            }
//        }
//        handler.post(timeTask!!)
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

        val result = "全场商品<font color='#FFE8AC'>免费</font>领取"
        dataBinding.mainFreeDescDesc.text = Html.fromHtml(result)

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