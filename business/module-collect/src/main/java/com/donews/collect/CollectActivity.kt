package com.donews.collect

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.collect.bean.DanMuBean
import com.donews.collect.bean.DanMuInfo
import com.donews.collect.bean.StatusInfo
import com.donews.collect.databinding.CollectFragmentBinding
import com.donews.collect.dialog.DialogUtil
import com.donews.collect.view.DanMuView
import com.donews.collect.vm.CollectViewModel
import com.donews.common.base.MvvmBaseLiveDataActivity
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.middle.mainShare.extend.setOnClickListener
import com.donews.utilslibrary.utils.DensityUtils
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar

/**
 *  make in st
 *  on 2022/5/16 09:53
 *  集卡模块
 */
@Route(path = RouterFragmentPath.Collect.PAGER_COLLECT)
class CollectActivity: MvvmBaseLiveDataActivity<CollectFragmentBinding, CollectViewModel>() {

    private var mContext: Context? = null

    override fun getLayoutId() = R.layout.collect_fragment

    private fun setBinding() {
        mDataBinding?.taskModel = mViewModel
    }

    override fun initView() {
        setBinding()
        preInit()
        initClick()
        normalStart()
    }

    private fun preInit(){
        ImmersionBar.with(this)
            .statusBarColor(R.color.transparent)
            .navigationBarColor(R.color.black)
            .fitsSystemWindows(false)
            .autoDarkModeEnable(true)
            .init()
        mContext = this
        ARouter.getInstance().inject(this)
    }

    private fun normalStart(){
        initLiveData()
        initRecyclerView()
        loadDanMu()
        loadStatus()
    }

    private fun initLiveData(){
        initDanMu()
        initStatus()
        initGoods()
    }

    private fun initRecyclerView(){
        mDataBinding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(mContext,3)
        }
    }

    private fun initDanMu(){
        mViewModel?.danMu?.observe(this,{
            it?.let {
                handleDanMu(it)
            }
        })
        startDanMuPlay()
    }

    private fun loadDanMu(){
        mViewModel?.requestDanMu()
    }

    private fun initStatus(){
        mViewModel?.status?.observe(this,{
            it?.let {
                mStatusInfo = it
                handleStatus()
            }
        })
    }

    private fun loadStatus(){
        mViewModel?.requestDanMu()
    }

    private fun initGoods(){
        mViewModel?.goodsInfo?.observe(this,{
            it?.let {
                DialogUtil.showGoodDialog(this,Gson().toJson(it),{finish()}){

                }
            }
        })
    }

    private fun loadGoods(){
        mViewModel?.requestGoods()
    }

    //region 弹幕
    private val danMuList: MutableList<DanMuBean> = arrayListOf()
    private var danMuView: DanMuView? = null

    private fun handleDanMu(it:DanMuInfo){
        danMuList.clear()
        danMuList.addAll(it.list)
        danMuView = DanMuView(this)
        danMuView?.setData(danMuList)
        mDataBinding?.danMuLayout?.removeAllViews()
        danMuView?.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtils.dip2px(60f)
            )
        mDataBinding?.danMuLayout?.addView(danMuView)
    }

    private fun startDanMuPlay(){
        mHandler.postDelayed(danMuTimer,(Math.random() * 2000 + 4000).toLong())
    }

    private val danMuTimer = object: Runnable{
        override fun run() {
            if (danMuView != null) {
                danMuView!!.addDanMuView()
                mHandler.postDelayed(this, (Math.random() * 2000 + 4000).toLong())
            }
        }

    }
    //endregion

    //region 集卡状态
    private var mStatusInfo: StatusInfo? = null
    companion object{
        //集卡状态 0 未集卡 1 集卡中 2 集卡完成 3 集卡失败
        private const val STATUS_ZERO = 0
        private const val STATUS_ONE = 1
        private const val STATUS_TWO = 2
        private const val STATUS_THREE = 3
    }
    private fun handleStatus(){
        when(mStatusInfo?.status){
            STATUS_ZERO->{
                loadGoods()
            }
            STATUS_ONE->{}
            STATUS_TWO->{}
            STATUS_THREE->{}
        }
    }
    //endregion

    private fun initClick(){
        setOnClickListener(mDataBinding?.iconBack){
            when(this){
                mDataBinding?.iconBack->{
                    finish()
                }
            }
        }
    }

    val mHandler = Handler(Looper.getMainLooper())

}