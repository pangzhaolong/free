package com.donews.main.dialog

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.main.R
import com.donews.main.databinding.MainExitDialogRemindBinding
import com.donews.main.entitys.resps.RemindConfig
import com.donews.utilslibrary.utils.CalendarUtils
import com.vmadalin.easypermissions.EasyPermissions


/**
 * 温馨提示弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 20:31
 */
class RemindDialog : AbstractFragmentDialog<MainExitDialogRemindBinding>(), EasyPermissions.PermissionCallbacks {

    companion object {

        private const val REQUEST_PER_CODE = 10001
        private const val PARAMS_CONFIG = "config"

        val PAR = arrayListOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

        fun newInstance(remindConfig: RemindConfig): RemindDialog {
            val args = Bundle()
            args.putSerializable(PARAMS_CONFIG, remindConfig)
            val fragment = RemindDialog()
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var remindConfig: RemindConfig
    private val handler = Handler(Looper.getMainLooper())


    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            remindConfig = it.getSerializable(PARAMS_CONFIG) as RemindConfig
        } ?: kotlin.run {
            remindConfig = RemindConfig()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.main_exit_dialog_remind
    }

    override fun initView() {
        dataBinding.remindConfig = remindConfig
        dataBinding.eventListener = EventListener()

        dataBinding.btnNext.visibility = if (checkPermission()) View.GONE else View.VISIBLE
//        showCloseBtn()
    }


    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed(Runnable {
            dataBinding.ivClose.visibility = View.VISIBLE
        }, remindConfig.closeBtnLazyShow * 1000L)
    }

    private fun checkPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        onCancelListener?.onCancel()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        addEvent()
    }

    private fun requiresPermission() {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, PAR)) {
            Toast.makeText(context, "开启提醒失败", Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "提醒需要日历权限",
                REQUEST_PER_CODE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            )
        }
    }


    private fun addEvent() {
        context?.let {
            val time = System.currentTimeMillis() + 5 * 60 * 1000;
            CalendarUtils.addCalendarEvent(
                it,
                "测试日程",
                "点击跳转>><a href=\"jdd://com.cdyf.lottery.jdd\">打开app</a><br/>".toString(),
                time,
                30,
                "FREQ=DAILY;INTERVAL=1",
                4
            )
        }
        onCancelListener?.onCancel()
    }


    inner class EventListener {
        fun clickRemind(view: View) {
            requiresPermission()
        }

        fun clickClose(view: View) {
            onSureListener?.onSure()
        }

        fun clickIvClose(view: View) {
            onCloseListener?.onClose()
        }
    }


}