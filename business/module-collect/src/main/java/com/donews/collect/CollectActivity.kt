package com.donews.collect

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.collect.adapter.CollectAdapter
import com.donews.collect.bean.*
import com.donews.collect.databinding.CollectFragmentBinding
import com.donews.collect.util.DialogUtil
import com.donews.collect.util.AnimationUtil
import com.donews.collect.util.TimeUtil
import com.donews.collect.view.DanMuView
import com.donews.collect.vm.CollectViewModel
import com.donews.common.base.MvvmBaseLiveDataActivity
import com.donews.common.router.RouterFragmentPath
import com.donews.middle.mainShare.bus.CollectStartNewCardEvent
import com.donews.middle.mainShare.extend.setOnClickListener
import com.donews.utilslibrary.utils.DensityUtils
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.text.DecimalFormat

/**
 *  make in st
 *  on 2022/5/16 09:53
 *  集卡模块
 */
@Route(path = RouterFragmentPath.Collect.PAGER_COLLECT)
class CollectActivity : MvvmBaseLiveDataActivity<CollectFragmentBinding, CollectViewModel>() {

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

    private fun preInit() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.transparent)
            .navigationBarColor(R.color.black)
            .fitsSystemWindows(false)
            .autoDarkModeEnable(true)
            .init()
        mContext = this
        ARouter.getInstance().inject(this)
    }

    private fun normalStart() {
        initLiveData()
        initRecyclerView()
        startBubbleAnimation()
        loadDanMu()
        loadStatus()
    }

    private fun initLiveData() {
        initDanMu()
        initStatus()
        initGoods()
        initNewGoodCard()
        initStopCard()
    }

    private var collectAdapter: CollectAdapter? = null
    private var collectFgList: MutableList<CardFragment> = arrayListOf()
    private fun initRecyclerView() {
        mDataBinding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(mContext, 3)
            collectAdapter = CollectAdapter(R.layout.collect_item_fragment,collectFgList)
            adapter = collectAdapter
        }
    }

    private var bubbleFloutOneAnimation: ObjectAnimator? = null
    private var bubbleFloutTwoAnimation: ObjectAnimator? = null
    private fun startBubbleAnimation(){
        bubbleFloutOneAnimation = AnimationUtil.bubbleFloat(mDataBinding?.bubbleOne,3000,10f,-1)
        bubbleFloutOneAnimation?.start()
        bubbleFloutTwoAnimation = AnimationUtil.bubbleFloat(mDataBinding?.bubbleTwo,2000,10f,-1)
        bubbleFloutTwoAnimation?.start()
    }
    private fun cancelBubbleAnimation(){
        if (bubbleFloutOneAnimation != null && bubbleFloutOneAnimation!!.isRunning){
            bubbleFloutOneAnimation?.cancel()
            bubbleFloutOneAnimation = null
        }
        if (bubbleFloutTwoAnimation != null && bubbleFloutTwoAnimation!!.isRunning){
            bubbleFloutTwoAnimation?.cancel()
            bubbleFloutTwoAnimation = null
        }
    }

    //region 接口相关
    private fun initDanMu() {
        mViewModel?.danMu?.observe(this, {
            it?.let {
                handleDanMu(it)
            }
        })
        startDanMuPlay()
    }

    private fun loadDanMu() {
        mViewModel?.requestDanMu()
    }

    private fun initStatus() {
        mViewModel?.status?.observe(this, {
            it?.let {
                mStatusInfo = it
                handleStatus()
            }
        })
    }

    private fun loadStatus() {
        mViewModel?.requestStatus()
    }

    private fun initGoods() {
        mViewModel?.goodsInfo?.observe(this, {
            it?.let {
                mHandler.postDelayed({
                    mDataBinding?.iconBack?.visibility = View.INVISIBLE
                },500L)
                DialogUtil.showGoodDialog(this, Gson().toJson(it), {
                    mDataBinding?.iconBack?.visibility = View.VISIBLE
                }, {
                    finish()
                }) { goodId ->
                    loadNewGoodCard(goodId)
                }
            }
        })
    }

    private fun loadGoods() {
        mViewModel?.requestGoods()
    }

    private fun initNewGoodCard() {
        mViewModel?.newGoodCard?.observe(this, {
            it?.let {
                loadStatus()
                EventBus.getDefault().post(CollectStartNewCardEvent())
            }
        })
    }

    private fun loadNewGoodCard(goodId: String) {
        mViewModel?.requestNewGoodCard(goodId)
    }

    private fun initStopCard(){
        mViewModel?.stopCard?.observe(this, {
            it?.let {
                loadGoods()
            }
        })
    }

    private fun loadStopCard(cardId:String){
        mViewModel?.requestStopCard(cardId)
    }
    //endregion

    //region 弹幕
    private val danMuList: MutableList<DanMuBean> = arrayListOf()
    private var danMuView: DanMuView? = null

    private fun handleDanMu(it: DanMuInfo) {
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

    private fun startDanMuPlay() {
        mHandler.postDelayed(danMuTimer, (Math.random() * 2000 + 4000).toLong())
    }

    private val danMuTimer = object : Runnable {
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

    companion object {
        //集卡状态 0 未集卡 1 集卡中 2 集卡完成 3 集卡失败
        private const val STATUS_ZERO = 0
        private const val STATUS_ONE = 1
        private const val STATUS_TWO = 2
        private const val STATUS_THREE = 3
    }

    //集卡状态处理
    private fun handleStatus() {
        when (mStatusInfo?.status) {
            STATUS_ZERO -> {
                loadGoods()
            }
            STATUS_ONE -> {
                showLayoutMsg()
            }
            STATUS_TWO -> {
            }
            STATUS_THREE -> {
            }
        }
    }
    //endregion

    //布局内容填充
    @SuppressLint("NotifyDataSetChanged")
    private fun showLayoutMsg(){
        try {
            mDataBinding?.rightTv?.text = mStatusInfo?.goodsInfo?.title
            curTime = mStatusInfo?.timeOut!!
            mHandler.postDelayed(timer,1000L)
            mDataBinding?.lotteryTwo?.text = mStatusInfo?.cardTimes.toString()
            val curProgress = (mStatusInfo?.uniProgress!! / 100).toDouble()
            val str = DecimalFormat("000.00%").format(curProgress)
            mDataBinding?.bottomTvOne?.text = str[0].toString()
            mDataBinding?.bottomTvTwo?.text = str[1].toString()
            mDataBinding?.bottomTvThree?.text = str[2].toString()
            mDataBinding?.bottomTvFour?.text = str[4].toString()
            mDataBinding?.bottomTvFive?.text = str[5].toString()
            mDataBinding?.bottomTvSix?.text = str[6].toString()
            when (curProgress){
                100.0->mDataBinding?.progress?.progress = 100
                0.0-> mDataBinding?.progress?.progress = 0
                else-> mDataBinding?.progress?.progress = curProgress.toInt()
            }
            collectFgList.clear()
            collectFgList.addAll(mStatusInfo?.fragments as MutableList)
            collectAdapter?.notifyDataSetChanged()

        } catch (e:Exception){}
    }

    //倒计时
    private var curTime = 100
    private val timer = object : Runnable {
        override fun run() {
            try {
                if (curTime > 0) {
                    curTime--
                    TimeUtil.timeConversion(curTime.toLong(),mDataBinding?.tvOne!!,
                        mDataBinding?.tvTwo!!,mDataBinding?.tvThree!!,mDataBinding?.tvFour!!)
                    if (curTime == 0) {
                        //timeout
                    } else {
                        mHandler.postDelayed(this, 1000L)
                    }
                }
            } catch (e:Exception){}
        }

    }

    private fun initClick() {
        setOnClickListener(mDataBinding?.iconBack,mDataBinding?.changeGoodClick) {
            when (this) {
                mDataBinding?.iconBack -> {
                    finish()
                }
                mDataBinding?.changeGoodClick -> {
                    if (mStatusInfo != null){
                        DialogUtil.showChangeGoodDialog(this@CollectActivity,Gson().toJson(mStatusInfo)){
                            loadStopCard(it)
                        }
                    }
                }
            }
        }
    }

    val mHandler = Handler(Looper.getMainLooper())

    override fun onDestroy() {
        super.onDestroy()
        cancelBubbleAnimation()
    }

}