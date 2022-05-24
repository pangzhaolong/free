package com.donews.collect

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ToastUtils
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener
import com.donews.base.utils.ToastUtil
import com.donews.collect.adapter.CollectAdapter
import com.donews.collect.bean.*
import com.donews.collect.databinding.CollectFragmentBinding
import com.donews.collect.util.*
import com.donews.collect.view.DanMuView
import com.donews.collect.view.GridDividerDecoration
import com.donews.collect.vm.CollectViewModel
import com.donews.common.base.MvvmBaseLiveDataActivity
import com.donews.common.router.RouterFragmentPath
import com.donews.middle.adutils.RewardVideoAd
import com.donews.middle.mainShare.bus.CollectStartNewCardEvent
import com.donews.middle.mainShare.extend.setOnClickListener
import com.donews.utilslibrary.utils.DensityUtils
import com.donews.utilslibrary.utils.SPUtils
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
        startFingerAnimation()
        loadDanMu()
        loadStatus()
    }

    private fun initLiveData() {
        initDanMu()
        initStatus()
        initGoods()
        initNewGoodCard()
        initStopCard()
        initDrawCard()
        initCardCharge()
    }

    private var collectAdapter: CollectAdapter? = null
    private var collectFgList: MutableList<CardFragment> = arrayListOf()
    private fun initRecyclerView() {
        mDataBinding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(mContext, 3)
            collectAdapter = CollectAdapter(R.layout.collect_item_fragment,collectFgList)
            adapter = collectAdapter
            addItemDecoration(
                GridDividerDecoration(GridLayoutManager.VERTICAL).apply {
                    dividerHeight(DensityUtils.dip2px(4f))
                    dividerColor(ContextCompat.getColor(context,R.color.white))
                }
            )
        }
    }

    private var bubbleFloatOneAnimation: ObjectAnimator? = null
    private var bubbleFloatTwoAnimation: ObjectAnimator? = null
    private fun startBubbleAnimation(){
        bubbleFloatOneAnimation = AnimationUtil.bubbleFloat(mDataBinding?.bubbleOne,3000,10f,-1)
        bubbleFloatOneAnimation?.start()
        bubbleFloatTwoAnimation = AnimationUtil.bubbleFloat(mDataBinding?.bubbleTwo,2000,10f,-1)
        bubbleFloatTwoAnimation?.start()
    }
    private fun cancelBubbleAnimation(){
        if (bubbleFloatOneAnimation != null && bubbleFloatOneAnimation!!.isRunning){
            bubbleFloatOneAnimation?.cancel()
            bubbleFloatOneAnimation = null
        }
        if (bubbleFloatTwoAnimation != null && bubbleFloatTwoAnimation!!.isRunning){
            bubbleFloatTwoAnimation?.cancel()
            bubbleFloatTwoAnimation = null
        }
    }

    private fun startFingerAnimation(){
        AnimationUtil.startFingerAnimation(mDataBinding?.jsonAnimation)
    }

    private fun cancelFingerAnimation(){
        AnimationUtil.cancelLottieAnimation(mDataBinding?.jsonAnimation)
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
                //抽选卡后及时赋值(中途不能刷新),保证cardId正确
                mStatusInfo = it
                handleNewCard()
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
                if (mFailProcessStatus){
                    mFailProcessStatus = false
                    startStepOne()
                } else {
                    loadGoods()
                }
            }
        })
    }

    private fun loadStopCard(cardId:String){
        mViewModel?.requestStopCard(cardId)
    }

    private fun initDrawCard(){
        mViewModel?.drawCard?.observe(this, {
            it?.let {
                if (it.cardTimes > 0){
                    SPUtils.setInformain("oneTimeLimitTime",System.currentTimeMillis() + MAX_FRAGMENT_TIME * 1000)
                    mHandler.removeCallbacks(fragmentTimer)
                    mHandler.postDelayed(fragmentTimer,1000L)
                }
                DialogUtil.showDrawDialog(this,it.no,it.img){
                    loadStatus()
                }
            }
        })
    }

    //抽碎片才消耗次数
    private fun loadDrawCard(cardId:String){
        mViewModel?.requestDrawCard(cardId)
    }

    private fun initCardCharge(){
        mViewModel?.cardCharge?.observe(this, {
            it?.let {
                if (it.uniTimes > 0){
                    SPUtils.setInformain("twoTimeLimitTime",System.currentTimeMillis() + MAX_ADD_TIME * 1000)
                    mHandler.removeCallbacks(addTimer)
                    mHandler.postDelayed(addTimer,1000L)
                }
                loadStatus()
            }
        })
    }

    private fun loadCardCharge(cardId:String){
        mViewModel?.requestCardCharge(cardId)
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
        //抽碎片可抽情况下的倒计时最大值
        private const val MAX_FRAGMENT_TIME = 300
        //冲能可冲情况下的倒计时最大值
        private const val MAX_ADD_TIME = 60
    }

    //集卡状态处理
    private fun handleStatus() {
        showFragmentAndAddBtnStatus()
        when (mStatusInfo?.status) {
            STATUS_ZERO -> {
                startStepOne()
            }
            STATUS_ONE -> {
                showLayoutMsg()
            }
            STATUS_TWO -> {
            }
            STATUS_THREE -> {
                mFailProcessStatus = true
                DialogUtil.showFailDialog(this@CollectActivity,Gson().toJson(mStatusInfo)){
                    if (mStatusInfo?.cardId != null){
                        loadStopCard(mStatusInfo?.cardId!!)
                    }
                }
            }
        }
    }

    //重选卡后的处理(处理新老流程)
    private fun handleNewCard(){
        Log.i("adsfadf-->","-ads-->${mClickChangeClickStatus}")
        if (mClickChangeClickStatus){
            mClickChangeClickStatus = false
            //点击更换集卡直接送一次碎片
            showDrawDialog()
        } else {
            //未集卡状态下走新老流程
            if (UserStatusUtil.isNewUser()){
                startStepTwo()
            } else {
                startStepThree()
            }
        }
    }

    private fun startStepOne(){
        if (DayStepUtil.instance.isTodayShowOneStep()){
            DialogUtil.showStepOneDialog(this){
                loadGoods()
            }
        } else {
            loadGoods()
        }
    }

    private fun startStepTwo(){
        if (DayStepUtil.instance.isTodayShowTwoStep()){
            DialogUtil.showStepTwoDialog(this){
                startStepFour()
            }
        } else {
            startStepFour()
        }
    }

    private fun startStepThree(){
        if (DayStepUtil.instance.isTodayShowThreeStep()){
            DialogUtil.showStepThreeDialog(this){
                startStepFour()
            }
        } else {
            startStepFour()
        }
    }

    private fun startStepFour(){
        if (DayStepUtil.instance.isTodayShowFourStep()){
            DialogUtil.showStepFourDialog(this){
                val curNum = SPUtils.getInformain("todayShowFourStepNum", 0)
                DayStepUtil.instance.setStepFourSp(curNum + 1)
                showDrawDialog()
            }
        } else {
            showDrawDialog()
        }
    }

    private fun showDrawDialog(){
        var newIndex = 0
        try {
            for (index in mStatusInfo!!.fragments.indices){
                if (mStatusInfo!!.fragments[index].holdNum == 1){
                    newIndex = index
                    break
                }
            }
            DialogUtil.showDrawDialog(this,mStatusInfo!!.fragments[newIndex].no,mStatusInfo!!.fragments[newIndex].img){
                loadStatus()
            }
        } catch (e:Exception){}

    }
    //endregion

    //布局内容填充
    @SuppressLint("NotifyDataSetChanged")
    private fun showLayoutMsg(){
        try {
            mDataBinding?.rightTv?.text = mStatusInfo?.goodsInfo?.title
            curTime = mStatusInfo?.timeOut!!
            mHandler.removeCallbacks(timer)
            mHandler.postDelayed(timer,1000L)
            mDataBinding?.lotteryTwo?.text = mStatusInfo?.cardTimes.toString()
            if (mStatusInfo?.uniProgress in 0..10000){
                val div = BigDecimalUtils.div(mStatusInfo?.uniProgress.toString(), 10000.toString(), 4, false)
                val str = DecimalFormat("000.00%").format(div.toDouble())
                mDataBinding?.bottomTvOne?.text = str[0].toString()
                mDataBinding?.bottomTvTwo?.text = str[1].toString()
                mDataBinding?.bottomTvThree?.text = str[2].toString()
                mDataBinding?.bottomTvFour?.text = str[4].toString()
                mDataBinding?.bottomTvFive?.text = str[5].toString()
                mDataBinding?.bottomTvSix?.text = str[6].toString()
                mDataBinding?.progress?.max = 10000
                val startProgress = SPUtils.getInformain("cardProgress",0).toFloat()
                startProgressAddAnimation(startProgress,(mStatusInfo?.uniProgress ?: 0).toFloat())
            }
            collectFgList.clear()
            collectFgList.addAll(mStatusInfo?.fragments as MutableList)
            collectAdapter?.notifyDataSetChanged()
        } catch (e:Exception){}
    }

    private var mProgressAnim: Animator? = null
    private fun startProgressAddAnimation(start:Float=0f,end:Float=0f){
        mProgressAnim = ValueAnimator.ofFloat(start,end).apply {
            duration = 3000L
            interpolator = LinearInterpolator()
            addUpdateListener{
                val value: Float = it.animatedValue as Float
                mDataBinding?.progress?.progress = value.toInt()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator?) {
                    super.onAnimationRepeat(animation)

                }
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    SPUtils.setInformain("cardProgress",end.toInt())
                    mProgressAnim = null
                }
            })
            start()
        }
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
                       loadStatus()
                    } else {
                        mHandler.postDelayed(this, 1000L)
                    }
                }
            } catch (e:Exception){}
        }
    }

    //抽碎片按钮和冲能按钮状态展示
    private fun showFragmentAndAddBtnStatus(){
        if (mStatusInfo?.cardTimes ?: 0 > 0){
            if (isOneTimeLimit()){
                mDataBinding?.timeLimitOneTv?.visibility = View.VISIBLE
                mDataBinding?.centerBtn?.alpha = 0.5f
                fragmentTime = ((SPUtils.getLongInformain("oneTimeLimitTime",0L) - System.currentTimeMillis()) / 1000).toInt()
                mHandler.removeCallbacks(fragmentTimer)
                mHandler.postDelayed(fragmentTimer,1000L)
            } else {
                mDataBinding?.timeLimitOneTv?.visibility = View.INVISIBLE
                mDataBinding?.centerBtn?.alpha = 1f
            }
        } else {
            mDataBinding?.timeLimitOneTv?.visibility = View.INVISIBLE
        }
        if (mStatusInfo?.uniTimes ?: 0 > 0){
            if (isTwoTimeLimit()){
                mDataBinding?.timeLimitTwoTv?.visibility = View.VISIBLE
                mDataBinding?.bottomClick?.alpha = 0.75f
                mDataBinding?.addTv?.alpha = 0.75f
                addTime = ((SPUtils.getLongInformain("twoTimeLimitTime",0L) - System.currentTimeMillis()) / 1000).toInt()
                mHandler.removeCallbacks(addTimer)
                mHandler.postDelayed(addTimer,1000L)
            } else {
                mDataBinding?.timeLimitTwoTv?.visibility = View.INVISIBLE
                mDataBinding?.bottomClick?.alpha = 1f
                mDataBinding?.addTv?.alpha = 1f
            }
        } else {
            mDataBinding?.timeLimitTwoTv?.visibility = View.INVISIBLE
        }
    }

    //抽碎片按钮是否处于5分钟倒计时中
    private fun isOneTimeLimit(): Boolean{
        return SPUtils.getLongInformain("oneTimeLimitTime",0L) > System.currentTimeMillis()
    }

    private var fragmentTime = MAX_FRAGMENT_TIME
    private val fragmentTimer = object : Runnable{
        override fun run() {
            try {
                if (fragmentTime > 0) {
                    fragmentTime--
                    mDataBinding?.timeLimitOneTv?.text = TimeUtil.stringForTimeNoHour(fragmentTime * 1000L)
                    if (fragmentTime == 0) {
                        mDataBinding?.timeLimitOneTv?.visibility = View.GONE
                        mDataBinding?.centerBtn?.alpha = 1f
                    } else {
                        mHandler.postDelayed(this, 1000L)
                        mDataBinding?.timeLimitOneTv?.visibility = View.VISIBLE
                        mDataBinding?.centerBtn?.alpha = 0.5f
                    }
                }
            } catch (e:Exception){}
        }

    }

    //点击抽碎片按钮
    private var isSortFgClick = false//控制抽碎片连续点击
    private fun clickFragmentBtn(){
        if (isSortFgClick) return
        isSortFgClick = true
        mHandler.postDelayed({
            isSortFgClick = false
        },5000L)
        AnimationUtil.startShakeAnimation(mDataBinding?.centerBtn, 1f,1)
        AnimationUtil.startShakeAnimation(mDataBinding?.chouTv, 1f,1)
        AnimationUtil.startShakeAnimation(mDataBinding?.timeLimitOneTv, 1f,1){
            mStatusInfo?.let {
                if (mStatusInfo?.cardTimes!! > 0){
                    if (isOneTimeLimit()){
                        ToastUtils.showShort("倒计时结束后才可以抽碎片")
                    } else {
                        RewardVideoAd.loadRewardVideoAd(
                            this@CollectActivity,
                            object : SimpleRewardVideoListener() {
                                override fun onAdError(code: Int, errorMsg: String?) {
                                    super.onAdError(code, errorMsg)
                                    ToastUtil.show(mContext, "视频加载失败请稍后再试")
                                }
                                override fun onAdShow() {
                                    super.onAdShow()
                                    isSortFgClick = false
                                }
                                override fun onAdClose() {
                                    super.onAdClose()
                                    if (mStatusInfo!=null){
                                        loadDrawCard(mStatusInfo!!.cardId)
                                    }
                                }
                            },
                            false
                        )
                    }
                } else {
                    ToastUtils.showShort("今日抽碎片剩余次数不足,明日再来!")
                }
            }
        }
    }

    //充能按钮是否处于1分钟倒计时中
    private fun isTwoTimeLimit(): Boolean{
        return SPUtils.getLongInformain("twoTimeLimitTime",0L) > System.currentTimeMillis()
    }

    private var addTime = MAX_ADD_TIME
    private val addTimer = object : Runnable{
        override fun run() {
            try {
                if (addTime > 0) {
                    addTime--
                    mDataBinding?.timeLimitTwoTv?.text = TimeUtil.stringForTimeNoHour(addTime * 1000L)
                    if (addTime == 0) {
                        mDataBinding?.timeLimitTwoTv?.visibility = View.GONE
                        mDataBinding?.bottomClick?.alpha = 1f
                        mDataBinding?.addTv?.alpha = 1f
                    } else {
                        mHandler.postDelayed(this, 1000L)
                        mDataBinding?.timeLimitTwoTv?.visibility = View.VISIBLE
                        mDataBinding?.bottomClick?.alpha = 0.75f
                        mDataBinding?.addTv?.alpha = 0.75f
                    }
                }
            } catch (e:Exception){}
        }

    }

    //点击充能按钮
    private var isSortAddClick = false//控制充能连续点击
    private fun clickAddBtn(){
        if (isSortAddClick) return
        isSortAddClick = true
        mHandler.postDelayed({
            isSortAddClick = false
        },5000L)
        AnimationUtil.clickPressAnimation(mDataBinding?.addTv)
        AnimationUtil.clickPressAnimation(mDataBinding?.bottomClick){
            mStatusInfo?.let {
            if (mStatusInfo?.uniTimes!! > 0){
                if (isTwoTimeLimit()){
                    ToastUtils.showShort("倒计时结束后才可以冲能")
                } else {
                    RewardVideoAd.loadRewardVideoAd(
                        this@CollectActivity,
                        object : SimpleRewardVideoListener() {
                            override fun onAdError(code: Int, errorMsg: String?) {
                                super.onAdError(code, errorMsg)
                                ToastUtil.show(mContext, "视频加载失败请稍后再试")
                            }
                            override fun onAdShow() {
                                super.onAdShow()
                                isSortAddClick = false
                            }
                            override fun onAdClose() {
                                super.onAdClose()
                                if (mStatusInfo != null){
                                    loadCardCharge(mStatusInfo!!.cardId)
                                }
                            }
                        },
                        false
                    )
                }
            } else {
                ToastUtils.showShort("今日充能剩余次数不足,明日再来!")
            }
        }
        }
    }

    //标记通过点击换商品按钮动作
    private var mClickChangeClickStatus = false
    //标记通过集卡过期弹窗触发
    private var mFailProcessStatus = false

    private fun initClick() {
        setOnClickListener(mDataBinding?.iconBack,mDataBinding?.changeGoodClick,mDataBinding?.centerBtn,
            mDataBinding?.bottomClick,mDataBinding?.jsonAnimation) {
            when (this) {
                mDataBinding?.iconBack -> {
                    finish()
                }
                mDataBinding?.changeGoodClick -> {
                    if (mStatusInfo != null){
                        Log.i("adsfadf-->","-ads-adsa->${mClickChangeClickStatus}")
                        mClickChangeClickStatus = true
                        DialogUtil.showChangeGoodDialog(this@CollectActivity,Gson().toJson(mStatusInfo)){
                            loadStopCard(it)
                        }
                    }
                }
                mDataBinding?.centerBtn->{
                    clickFragmentBtn()
                }
                mDataBinding?.bottomClick,mDataBinding?.jsonAnimation->{
                    clickAddBtn()
                }
            }
        }
    }

    val mHandler = Handler(Looper.getMainLooper())

    override fun onDestroy() {
        super.onDestroy()
        cancelBubbleAnimation()
        cancelFingerAnimation()
        mHandler.removeCallbacks(timer)
        mHandler.removeCallbacks(danMuTimer)
    }

}