package com.donews.task.dialog

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.task.R
import com.donews.task.databinding.TaskDialogBoxBinding
import com.donews.task.util.AnimationUtil

/**
 *  make in st
 *  on 2022/5/9 11:35
 */
class BoxDialog : AbstractFragmentDialog<TaskDialogBoxBinding>(false, false) {

    companion object {
        fun newInstance(isActive: Boolean = false): BoxDialog {
            return BoxDialog().apply {
                arguments = Bundle().apply {
                    putBoolean("isActive", isActive)
                }
            }
        }
    }

    private var mIsActive = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            mIsActive = it.getBoolean("isActive")
        }
    }

    override fun getThemeStyle(): Int {
        return R.style.TaskDialogStyle
    }

    override fun getLayoutId() = R.layout.task_dialog_box

    private var rotate: ObjectAnimator? = null

    override fun initView() {
        dataBinding.eventListener = EventListener()
        rotate = AnimationUtil.rotate(dataBinding?.rotateView)
        if (mIsActive) dataBinding.tvEnd.text = "1点活跃度" else dataBinding.tvEnd.text = "1点幸运值"
    }

    override fun isUseDataBinding() = true

    var clickDialogBtn: () -> Unit = {}

    inner class EventListener {
        fun receiveBtn(view: View) {
            clickDialogBtn.invoke()
            disMissDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (rotate != null && rotate!!.isRunning) {
            rotate?.cancel()
            rotate = null
        }
    }

}