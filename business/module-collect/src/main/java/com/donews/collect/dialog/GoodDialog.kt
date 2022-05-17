package com.donews.collect.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.donews.base.fragmentdialog.AbstractFragmentDialog
import com.donews.base.utils.ToastUtil
import com.donews.collect.R
import com.donews.collect.adapter.GoodAdapter
import com.donews.collect.bean.GoodBean
import com.donews.collect.bean.GoodInfo
import com.donews.collect.databinding.CollectDialogGoodsBinding
import com.donews.middle.mainShare.bean.Ex
import com.google.gson.Gson
import java.lang.Exception

/**
 *  make in st
 *  on 2022/5/16 17:11
 */
class GoodDialog: AbstractFragmentDialog<CollectDialogGoodsBinding>(false, false) {

    companion object {
        fun newInstance(goodJson:String = ""): GoodDialog {
            return GoodDialog().apply {
                arguments = Bundle().apply {
                    putString("goodJson", goodJson)
                }
            }
        }
    }

    private var mGoodJson = ""
    private var mGoodInfo: GoodInfo? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            mGoodJson = it.getString("goodJson","")
        }
    }

    override fun getLayoutId() = R.layout.collect_dialog_goods

    private var collectList: MutableList<GoodBean> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        dataBinding.eventListener = EventListener()
        try {
            mGoodInfo = Gson().fromJson(mGoodJson,GoodInfo::class.java)
            collectList.clear()
            collectList.addAll(mGoodInfo?.list!!)
            dataBinding?.recyclerView?.apply {
                layoutManager = GridLayoutManager(context,2)
                adapter = GoodAdapter(R.layout.collect_item_good,collectList).apply {
                    clickCall = {
                        isSelect = true
                        mPosition = it
                        for(index in collectList.indices){
                            collectList[index].isSelect = it == index
                        }
                        notifyDataSetChanged()
                    }
                }
            }
        } catch (e:Exception){}
    }

    override fun isUseDataBinding() = true

    var clickDialogBtn: (goodId: String) -> Unit = {}
    var clickDialogCancel: () -> Unit = {}
    private var isSelect = false
    private var mPosition = 0

    inner class EventListener {
        fun cancelBtn(view: View) {
            clickDialogCancel.invoke()
            disMissDialog()
        }
        fun receiveBtn(view: View) {
            if (isSelect){
                clickDialogBtn.invoke(collectList[mPosition].goodsId)
                disMissDialog()
            } else {
                if (context != null) ToastUtil.show(requireContext(),"请选择你的福利卡!")
            }
        }
    }

}