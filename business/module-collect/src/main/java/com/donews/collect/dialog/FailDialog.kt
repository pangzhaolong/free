package com.donews.collect.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.donews.collect.bean.StatusInfo
import com.donews.collect.databinding.CollectDialogGoodsBinding
import com.donews.collect.databinding.CollectDialogGoodsDrawBinding
import com.donews.collect.databinding.CollectDialogGoodsFailBinding
import com.donews.collect.util.TimeUtil
import com.donews.middle.mainShare.bean.Ex
import com.google.gson.Gson
import java.lang.Exception

/**
 *  make in st
 *  on 2022/5/16 17:11
 */
class FailDialog: AbstractFragmentDialog<CollectDialogGoodsFailBinding>(false, false) {

    companion object {
        fun newInstance(goodJson:String = ""): FailDialog {
            return FailDialog().apply {
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

    override fun getLayoutId() = R.layout.collect_dialog_goods_fail

    override fun getThemeStyle(): Int {
        return R.style.CollectDialogStyle
    }

    val mHandler = Handler(Looper.getMainLooper())

    //倒计时
    private var curTime = 5
    private val timer = object : Runnable {
        @SuppressLint("SetTextI18n")
        override fun run() {
            try {
                if (curTime > 0) {
                    curTime--
                    dataBinding?.rightTv?.text = "(${curTime}s)"
                    if (curTime == 0) {
                        mHandler.removeCallbacks(this)
                        disMissDialog()
                    } else {
                        mHandler.postDelayed(this, 1000L)
                    }
                }
            } catch (e:Exception){}
        }

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun initView() {
        dataBinding.eventListener = EventListener()
        mHandler.postDelayed(timer,1000L)
        try {
            mGoodInfo = Gson().fromJson(mGoodJson, StatusInfo::class.java)
            GlideUtils.loadImageView(context,mGoodInfo?.goodsInfo?.mainPic,dataBinding.img)
        } catch (e:Exception){}
    }

    override fun isUseDataBinding() = true

    inner class EventListener {
        fun receiveBtn(view: View) {
            mHandler.removeCallbacks(timer)
            disMissDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacks(timer)
    }

}