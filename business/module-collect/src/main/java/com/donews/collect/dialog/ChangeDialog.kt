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
import com.donews.collect.bean.GoodBean
import com.donews.collect.bean.GoodInfo
import com.donews.collect.bean.StatusInfo
import com.donews.collect.databinding.CollectDialogGoodsBinding
import com.donews.collect.databinding.CollectDialogGoodsChangeBinding
import com.donews.middle.mainShare.bean.Ex
import com.google.gson.Gson
import java.lang.Exception

/**
 *  make in st
 *  on 2022/5/16 17:11
 */
class ChangeDialog: AbstractFragmentDialog<CollectDialogGoodsChangeBinding>(false, false) {

    companion object {
        fun newInstance(goodJson:String = ""): ChangeDialog {
            return ChangeDialog().apply {
                arguments = Bundle().apply {
                    putString("goodJson", goodJson)
                }
            }
        }
    }

    private var mGoodJson = ""
    private var mGoodInfo: StatusInfo? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            mGoodJson = it.getString("goodJson","")
        }
    }

    override fun getLayoutId() = R.layout.collect_dialog_goods_change

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        dataBinding.eventListener = EventListener()
        try {
            mGoodInfo = Gson().fromJson(mGoodJson,StatusInfo::class.java)
            GlideUtils.loadImageView(context,mGoodInfo?.goodsInfo?.mainPic,dataBinding?.goodImage)
        } catch (e:Exception){}
    }

    override fun isUseDataBinding() = true

    var clickDialogBtn: (cardId: String) -> Unit = {}

    inner class EventListener {
        fun cancelBtn(view: View) {
            disMissDialog()
        }
        fun receiveBtn(view: View) {
            disMissDialog()
            if (mGoodInfo != null){
                clickDialogBtn.invoke(mGoodInfo?.cardId!!)
            }
        }
        fun deleteBtn(view: View) {
            disMissDialog()
        }
    }

}