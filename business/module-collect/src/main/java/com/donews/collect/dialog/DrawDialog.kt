package com.donews.collect.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.ToastUtil
import com.donews.base.utils.glide.GlideUtils
import com.donews.collect.R
import com.donews.collect.adapter.GoodAdapter
import com.donews.collect.bean.DrawCardInfo
import com.donews.collect.bean.GoodBean
import com.donews.collect.bean.GoodInfo
import com.donews.collect.databinding.CollectDialogGoodsBinding
import com.donews.collect.databinding.CollectDialogGoodsDrawBinding
import com.donews.middle.mainShare.bean.Ex
import com.google.gson.Gson
import java.lang.Exception

/**
 *  make in st
 *  on 2022/5/16 17:11
 */
class DrawDialog: AbstractFragmentDialog<CollectDialogGoodsDrawBinding>(false, false) {

    companion object {
        fun newInstance(goodJson:String = ""): DrawDialog {
            return DrawDialog().apply {
                arguments = Bundle().apply {
                    putString("goodJson", goodJson)
                }
            }
        }
    }

    private var mGoodJson = ""
    private var mGoodInfo: DrawCardInfo? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            mGoodJson = it.getString("goodJson","")
        }
    }

    override fun getLayoutId() = R.layout.collect_dialog_goods_draw

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun initView() {
        dataBinding.eventListener = EventListener()
        try {
            mGoodInfo = Gson().fromJson(mGoodJson,DrawCardInfo::class.java)
            dataBinding?.rightTv?.text = "${mGoodInfo?.no}号碎片"
            GlideUtils.loadImageView(context,mGoodInfo?.img,dataBinding.img)
        } catch (e:Exception){}
    }

    override fun isUseDataBinding() = true

    var clickDialogBtn: () -> Unit = {}

    inner class EventListener {
        fun receiveBtn(view: View) {
            clickDialogBtn.invoke()
            disMissDialog()
        }
    }

}