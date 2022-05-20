package com.donews.collect.dialog

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.glide.GlideUtils
import com.donews.collect.R
import com.donews.collect.databinding.CollectDialogGoodsDrawBinding
import com.donews.collect.util.AnimationUtil

/**
 *  make in st
 *  on 2022/5/16 17:11
 */
class DrawDialog : AbstractFragmentDialog<CollectDialogGoodsDrawBinding>(false, false) {

    companion object {
        fun newInstance(no: Int = 0, img: String = ""): DrawDialog {
            return DrawDialog().apply {
                arguments = Bundle().apply {
                    putInt("no", no)
                    putString("img", img)
                }
            }
        }
    }

    private var mNo = 0
    private var mImg = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            mNo = it.getInt("no", 0)
            mImg = it.getString("img", "")
        }
    }

    override fun getLayoutId() = R.layout.collect_dialog_goods_draw

    override fun getThemeStyle(): Int {
        return R.style.CollectDialogStyle
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun initView() {
        dataBinding.eventListener = EventListener()
        rotate = AnimationUtil.rotate(dataBinding?.rotateView)
        try {
            dataBinding?.rightTv?.text = "${mNo}号碎片"
            GlideUtils.loadImageView(context, mImg, dataBinding.img)
        } catch (e: Exception) {
        }
    }

    override fun isUseDataBinding() = true

    private var rotate: ObjectAnimator? = null

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