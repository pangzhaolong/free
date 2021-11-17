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
import com.donews.main.utils.ExitInterceptUtils
import com.donews.utilslibrary.utils.CalendarUtils
import com.orhanobut.logger.Logger
import com.vmadalin.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*


/**
 * 温馨提示弹出框
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 20:31
 */
class RemindDialog : AbstractFragmentDialog<MainExitDialogRemindBinding>(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val CALENDAR_TITLE = "奖多多开奖提醒"
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
        showCloseBtn()
    }


    override fun isUseDataBinding(): Boolean {
        return true
    }

    private fun showCloseBtn() {
        handler.postDelayed(Runnable {
            dataBinding.ivClose.visibility = View.VISIBLE
            dataBinding.tvOk.visibility = View.VISIBLE
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
        EasyPermissions.requestPermissions(
            this,
            "提醒需要日历权限",
            REQUEST_PER_CODE,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )

    }


    private fun addEvent() {
        context?.let {
//            val time = System.currentTimeMillis() + 5 * 60 * 1000;
            val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val startTimeString = ExitInterceptUtils.exitInterceptConfig.calendarRemindStartTime
            val startDate = format.parse(startTimeString)

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.time = startDate!!
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val m = calendar.get(Calendar.MINUTE)
            val s = calendar.get(Calendar.SECOND)
            calendar.set(year, month, day, hour, m, s)
            val time = calendar.timeInMillis
            CalendarUtils.addCalendarEvent(
                it,
                CALENDAR_TITLE,
                "打开app进行开奖",
                time,
                ExitInterceptUtils.exitInterceptConfig.calendarRemindDuration.toLong(),
                "FREQ=DAILY;INTERVAL=1",
                10
            )
        }
        onCancelListener?.onCancel()
    }


    inner class EventListener {
        fun clickRemind(view: View) {
            requiresPermission()
        }

        fun clickClose(view: View) {
            CalendarUtils.deleteCalendarEvent(view.context, CALENDAR_TITLE)
            addEvent()
            onSureListener?.onSure()
        }

        fun clickIvClose(view: View) {
            if(view.visibility == View.VISIBLE) {
                onCloseListener?.onClose()
            }
        }

        fun laterClose(view: View) {
            if(view.visibility == View.VISIBLE) {
                onLaterListener?.onClose()
            }
        }
    }


}