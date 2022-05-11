package com.donews.task

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.utils.ToastUtil
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterActivityPath
import com.donews.common.router.RouterFragmentPath
import com.donews.middle.mainShare.bean.BubbleBean
import com.donews.middle.mainShare.bean.TaskBubbleInfo
import com.donews.middle.mainShare.vm.MainShareViewModel
import com.donews.module_shareui.ShareUIBottomPopup
import com.donews.task.bean.TaskConfigInfo
import com.donews.task.databinding.TaskFragmentBinding
import com.donews.task.extend.setOnClickListener
import com.donews.task.util.*
import com.donews.task.view.ColdDownTimerView
import com.donews.task.view.explosion.ExplodeParticleFactory
import com.donews.task.view.explosion.ExplosionField
import com.donews.task.vm.TaskViewModel
import com.donews.utilslibrary.utils.SPUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import pl.droidsonroids.gif.GifDrawable

/**
 *  make in st
 *  on 2022/5/7 10:37
 *  活动模块
 */
@Route(path = RouterFragmentPath.Task.PAGER_TASK)
class TaskFragment : MvvmLazyLiveDataFragment<TaskFragmentBinding, TaskViewModel>() {

    private var mContext: Context? = null

    override fun getLayoutId() = R.layout.task_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        loadUserAssets()
    }

    private fun setBinding() {
        mDataBinding?.taskModel = mViewModel
    }

    private fun initView() {
        mContext = this.context
        setBinding()
        initClick()
        normalStart()
    }

    private fun normalStart() {
        initTaskControl()
        initShareViewModel()
        initLiveData()
        initMainGif()
        initBubble()
        initColdTimerView()
    }

    //region 接口调用相关
    private fun initLiveData() {
        initUserAssets()
        initTaskBubbles()
        initBubbleReceive()
    }

    private fun initUserAssets() {
        mShareVideModel.userAssets.observe(viewLifecycleOwner, {
            it?.let {
                mViewModel?.goldCoinNum?.set(it.coin.toString())
                mViewModel?.activityNum?.set(it.active.toString())
            }
            loadTaskBubbles()
        })
    }

    //获取用户幸运值和活跃度
    private fun loadUserAssets() {
        mShareVideModel.requestUserAssets()
    }

    private lateinit var taskBubbleBean: TaskBubbleInfo
    private var bubbleIsLeftOrRight = true//默认气泡在左边

    private fun initTaskBubbles() {
        mShareVideModel.taskBubbles.observe(viewLifecycleOwner, {
            it?.let {
                taskBubbleBean = it
                handleTaskBubbles()
            }
        })
    }

    //获取任务气泡列表
    private fun loadTaskBubbles() {
        mShareVideModel.requestTaskBubbles()
    }

    //当前正在处理哪个气泡
    private var mCurWhichBubbleType = ""

    private fun initBubbleReceive() {
        mViewModel.bubbleReceive.observe(viewLifecycleOwner, {
            it?.let {
                when(mCurWhichBubbleType){
                    SIGN->{
                        makeBubbleExplosion(mDataBinding?.iconSignBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconSignTv as View)
                        //签到没有金币效果
                        loadUserAssets()
                    }
                    COLLECT->{
                        makeBubbleExplosion(mDataBinding?.iconCollectBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconCollectTv as View)
                        startCoinGif()
                        loadUserAssets()
                    }
                    LOTTERY->{
                        makeBubbleExplosion(mDataBinding?.iconLuckDrawBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconLuckDrawTv as View)
                        startCoinGif()
                        loadUserAssets()
                    }
                    TURNTABLE->{
                        makeBubbleExplosion(mDataBinding?.iconLuckPanBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconLuckPanTv as View)
                        startCoinGif()
                        loadUserAssets()
                    }
                    SHARE->{
                        makeBubbleExplosion(mDataBinding?.iconShareBubble as View)
                        makeBubbleExplosion(mDataBinding?.shareTv as View)
                        startCoinGif()
                        loadUserAssets()
                    }
                    VIDEO->{
                        startCoinGif()
                        loadUserAssets()
                        loadTaskBubbles()
                    }
                    GIFT_BOX->{

                    }
                }
            }
        })
    }

    private fun loadBubbleReceive(mId: Int, mType: String) {
        mViewModel?.requestBubbleReceive(mId, mType)
    }
    //endregion

    //region 接口请求结果处理
    companion object {
        //任务状态 0 未完成 1 完成可领取 2 已领取
        private const val BUBBLE_NO_FINISH = 0
        private const val BUBBLE_NO_RECEIVE = 1
        private const val BUBBLE_HAVE_FINISH = 2

        //转盘、签到、抽奖、分享、集卡、视频、宝箱
        private const val TURNTABLE = "turntable"
        private const val SIGN = "sign"
        private const val LOTTERY = "lottery"
        private const val SHARE = "share"
        private const val COLLECT = "collect"
        private const val VIDEO = "video"
        private const val GIFT_BOX = "giftbox"
    }

    private fun handleTaskBubbles() {
        var canReceiveBubbleNum = 0//可领取气泡数
        for (index in taskBubbleBean.list.indices) {
            bubbleIsShow(index)
            if (taskBubbleBean.list[index].status == BUBBLE_NO_RECEIVE) {
                canReceiveBubbleNum++
            }
        }
        if (canReceiveBubbleNum > 0) startFingerAnimation() else cancelFingerAnimation()
    }

    private var taskBubbleSignBean: BubbleBean? = null
    private var taskBubbleLuckPanBean: BubbleBean? = null
    private var taskBubbleCollectBean: BubbleBean? = null
    private var taskBubbleShareBean: BubbleBean? = null
    private var taskBubbleLuckDrawBean: BubbleBean? = null
    private var taskBubbleVideoBean: BubbleBean? = null
    private var taskBubbleBoxBean: BubbleBean? = null

    @SuppressLint("SetTextI18n")
    private fun bubbleIsShow(index: Int) {
        when (taskBubbleBean.list[index].type) {
            TURNTABLE -> {
                taskBubbleLuckPanBean = taskBubbleBean.list[index]
                bubbleIsLeftOrRight = false
                when (taskBubbleLuckPanBean?.status) {
                    BUBBLE_NO_FINISH -> {
                        mDataBinding?.iconLuckPanBubble?.alpha = 0.45f
                        mDataBinding?.iconLuckPanTv?.alpha = 0.45f
                    }
                    BUBBLE_NO_RECEIVE -> {
                        mDataBinding?.iconLuckPanBubble?.alpha = 1f
                        mDataBinding?.iconLuckPanTv?.alpha = 1f
                    }
                    BUBBLE_HAVE_FINISH -> {
                        mDataBinding?.iconLuckPanBubble?.alpha = 0f
                        mDataBinding?.iconLuckPanTv?.alpha = 0f
                    }
                }
            }
            SIGN -> {
                taskBubbleSignBean = taskBubbleBean.list[index]
                bubbleIsLeftOrRight = true
                when (taskBubbleSignBean?.status) {
                    BUBBLE_NO_FINISH -> {
                        mDataBinding?.iconSignBubble?.alpha = 0.45f
                        mDataBinding?.iconSignTv?.alpha = 0.45f
                    }
                    BUBBLE_NO_RECEIVE -> {
                        mDataBinding?.iconSignBubble?.alpha = 1f
                        mDataBinding?.iconSignTv?.alpha = 1f
                    }
                    BUBBLE_HAVE_FINISH -> {
                        mDataBinding?.iconSignBubble?.alpha = 0f
                        mDataBinding?.iconSignTv?.alpha = 0f
                    }
                }
            }
            LOTTERY -> {
                taskBubbleLuckDrawBean = taskBubbleBean.list[index]
                bubbleIsLeftOrRight = true
                when (taskBubbleLuckDrawBean?.status) {
                    BUBBLE_NO_FINISH -> {
                        mDataBinding?.iconLuckDrawBubble?.alpha = 0.45f
                        mDataBinding?.iconLuckDrawTv?.alpha = 0.45f
                    }
                    BUBBLE_NO_RECEIVE -> {
                        mDataBinding?.iconLuckDrawBubble?.alpha = 1f
                        mDataBinding?.iconLuckDrawTv?.alpha = 1f
                    }
                    BUBBLE_HAVE_FINISH -> {
                        mDataBinding?.iconLuckDrawBubble?.alpha = 0f
                        mDataBinding?.iconLuckDrawTv?.alpha = 0f
                    }
                }
            }
            SHARE -> {
                taskBubbleShareBean = taskBubbleBean.list[index]
                bubbleIsLeftOrRight = false
                when (taskBubbleShareBean?.status) {
                    BUBBLE_NO_FINISH -> {
                        mDataBinding?.iconShareBubble?.alpha = 0.45f
                        mDataBinding?.shareTv?.alpha = 0.45f
                    }
                    BUBBLE_NO_RECEIVE -> {
                        mDataBinding?.iconShareBubble?.alpha = 1f
                        mDataBinding?.shareTv?.alpha = 1f
                    }
                    BUBBLE_HAVE_FINISH -> {
                        mDataBinding?.iconShareBubble?.alpha = 0f
                        mDataBinding?.shareTv?.alpha = 0f
                    }
                }
            }
            COLLECT -> {
                taskBubbleCollectBean = taskBubbleBean.list[index]
                bubbleIsLeftOrRight = true
                when (taskBubbleCollectBean?.status) {
                    BUBBLE_NO_FINISH -> {
                        mDataBinding?.iconCollectBubble?.alpha = 0.45f
                        mDataBinding?.iconCollectTv?.alpha = 0.45f
                    }
                    BUBBLE_NO_RECEIVE -> {
                        mDataBinding?.iconCollectBubble?.alpha = 1f
                        mDataBinding?.iconCollectTv?.alpha = 1f
                    }
                    BUBBLE_HAVE_FINISH -> {
                        mDataBinding?.iconCollectBubble?.alpha = 0f
                        mDataBinding?.iconCollectTv?.alpha = 0f
                    }
                }
            }
            VIDEO -> {
                taskBubbleVideoBean = taskBubbleBean.list[index]
                bubbleIsLeftOrRight = true
                when (taskBubbleVideoBean?.status) {
                    BUBBLE_NO_FINISH -> {
                        //冷却中
                        mDataBinding?.coldDownTimer?.alpha = 1f
                        mDataBinding?.countDownTimeTv?.alpha = 1f
                        mDataBinding?.seeAdTv?.alpha = 0.45f
                        mDataBinding?.seeAdTv?.text = "可领取(${taskBubbleVideoBean?.done ?: 0}/3)"
                        mDataBinding?.coldDownTimer?.apply {
                            setCountTime(taskBubbleVideoBean?.cd ?: 0)
                            startCountdown()
                        }
                    }
                    BUBBLE_NO_RECEIVE -> {
                        mDataBinding?.coldDownTimer?.alpha = 1f
                        mDataBinding?.countDownTimeTv?.alpha = 1f
                        mDataBinding?.seeAdTv?.text = "可领取(${taskBubbleVideoBean?.done ?: 0}/3)"
                        mDataBinding?.seeAdTv?.alpha = 1f
                        mDataBinding?.coldDownTimer?.apply {
                            setCountTime(mMaxCountTime)
                        }
                    }
                    BUBBLE_HAVE_FINISH -> {
                        mDataBinding?.coldDownTimer?.alpha = 0.45f
                        mDataBinding?.countDownTimeTv?.alpha = 0.45f
                        mDataBinding?.seeAdTv?.alpha = 0.45f
                        mDataBinding?.seeAdTv?.text = "明日再来"
                    }
                }
            }
            GIFT_BOX -> {
                taskBubbleBoxBean = taskBubbleBean.list[index]
                boxMaxOpenNum = taskBubbleBoxBean?.total ?: 5
                initBox()
            }
        }
    }
    //endregion

    //region 每日看广告气泡相关
    //今日看广告最大数, 中台配
    private var todaySeeAdMaxNum = 3
    //冷却倒计时最大值10s中台配
    private var mMaxCountTime = 120

    @SuppressLint("SetTextI18n")
    private fun initColdTimerView() {
        mDataBinding?.coldDownTimer?.apply {
            setCountTime(mMaxCountTime)
            setOnCountDownTimeListener(object : ColdDownTimerView.CountDownTimeListener {
                override fun getCurCountDownTime(time: Int) {
                    if (time > 0) {
                        mDataBinding?.countDownTimeTv?.text =
                            TimeUtils.stringForTimeNoHour(time * 1000.toLong())
                        mDataBinding?.countDownTimeTv?.visibility = View.VISIBLE
                    } else {
                        mDataBinding?.countDownTimeTv?.visibility = View.GONE
                    }
                }

                override fun countDownFinish() {
                    mDataBinding?.countDownTimeTv?.visibility = View.GONE
                }

            })
        }
    }
    //endregion

    //region 批量点击相关
    private fun initClick() {
        setOnClickListener(
            mDataBinding?.ruleClick,
            mDataBinding?.activityTxBtn,
            mDataBinding?.iconCanGet, mDataBinding?.iconBox,
            mDataBinding?.coldDownTimer,
            mDataBinding?.iconSignBubble, mDataBinding?.iconSignTv,//签到气泡
            mDataBinding?.iconLuckPanBubble, mDataBinding?.iconLuckPanTv,//转盘气泡
            mDataBinding?.iconCollectBubble, mDataBinding?.iconCollectTv,//集卡气泡
            mDataBinding?.iconShareBubble, mDataBinding?.shareTv,//分享气泡
            mDataBinding?.iconLuckDrawBubble, mDataBinding?.iconLuckDrawTv,//抽奖气泡
            mDataBinding?.iconBtn
        ) {
            when (this) {
                mDataBinding?.ruleClick -> {
                    if (activity != null) {
                        DialogUtil.showRuleDialog(requireActivity())
                    }
                }
                mDataBinding?.activityTxBtn -> {
                    if (activity != null) {
                        DialogUtil.showExchangeDialog(requireActivity()) {

                        }
                    }
                }
                mDataBinding?.iconCanGet, mDataBinding?.iconBox -> {
                    if (isCanOpenBox) {
                        //ad
                        Toast.makeText(mContext, "一波广告走起", Toast.LENGTH_SHORT).show()
                        val mTodayOpenBoxNum = SPUtils.getInformain("todayOpenBoxNum", 0)
                        SPUtils.setInformain("todayOpenBoxNum", mTodayOpenBoxNum + 1)
                        SPUtils.setInformain("todayOpenBoxOpenTime", System.currentTimeMillis())
                        mViewModel?.isShowBoxTimeView?.set(true)
                        mViewModel?.isShowIconCanGet?.set(false)
                        if (DayBoxUtil.instance.isShowDayTwentyOpenBox(boxMaxOpenNum)) {
                            SPUtils.setInformain(
                                "boxEndTime",
                                System.currentTimeMillis() + boxCurTime * 1000
                            )
                            boxCurTime = boxMaxTime
                            mHandler.postDelayed(boxTimer, 1000L)
                        } else {
                            cancelBoxAnimation()
                            mDataBinding?.boxTimeTv?.text = "明日再来"
                            ToastUtil.show(mContext, "今日宝箱已领完")
                        }
                    } else {
                        ToastUtil.show(mContext, "倒计时结束才可领取")
                    }
                }
                mDataBinding?.coldDownTimer -> {
                    when(taskBubbleVideoBean?.status){
                        BUBBLE_HAVE_FINISH->{
                            mDataBinding?.coldDownTimer?.alpha = 0.45f
                            mDataBinding?.seeAdTv?.text = "明日再来"
                        }
                        BUBBLE_NO_RECEIVE->{
                            //ad
                            Toast.makeText(mContext, "一波广告走起", Toast.LENGTH_SHORT).show()
                            mCurWhichBubbleType = VIDEO
                            loadBubbleReceive(
                                taskBubbleVideoBean?.id ?: 5,
                                taskBubbleVideoBean?.type ?: VIDEO
                            )
                        }
                        BUBBLE_NO_FINISH->{
                            Toast.makeText(mContext, "冷却中", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                mDataBinding?.iconSignBubble, mDataBinding?.iconSignTv -> {
                    //处理签到逻辑
                    when (taskBubbleSignBean?.status) {
                        BUBBLE_NO_FINISH -> {
                            if (activity != null && activity?.supportFragmentManager != null) {
                                RouterFragmentPath.User.getSingDialog()
                                    .show(activity?.supportFragmentManager!!, "SignInMineDialog")
                            }
                        }
                        BUBBLE_NO_RECEIVE -> {
                            mCurWhichBubbleType = SIGN
                            loadBubbleReceive(
                                taskBubbleSignBean?.id ?: 1,
                                taskBubbleSignBean?.type ?: SIGN
                            )
                        }
                    }
                }
                mDataBinding?.iconLuckPanBubble, mDataBinding?.iconLuckPanTv -> {
                    //处理转盘逻辑
                    when (taskBubbleLuckPanBean?.status) {
                        BUBBLE_NO_FINISH -> {
                            ARouter.getInstance()
                                .build(RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
                                .navigation()
                        }
                        BUBBLE_NO_RECEIVE -> {
                            mCurWhichBubbleType = COLLECT
                            loadBubbleReceive(
                                taskBubbleLuckPanBean?.id ?: 0,
                                taskBubbleLuckPanBean?.type ?: TURNTABLE
                            )
                        }
                    }
                }
                mDataBinding?.iconCollectBubble, mDataBinding?.iconCollectTv -> {
                    //处理集卡逻辑
                    when (taskBubbleCollectBean?.status) {
                        BUBBLE_NO_FINISH -> {
                            //跳集卡
                        }
                        BUBBLE_NO_RECEIVE -> {
                            mCurWhichBubbleType = COLLECT
                            loadBubbleReceive(
                                taskBubbleCollectBean?.id ?: 4,
                                taskBubbleCollectBean?.type ?: COLLECT
                            )
                        }
                    }
                }
                mDataBinding?.iconShareBubble, mDataBinding?.shareTv -> {
                    //处理分享逻辑
                    when (taskBubbleShareBean?.status) {
                        BUBBLE_NO_FINISH -> {
                            if (context != null) {
                                XPopup.Builder(activity)
                                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                    .popupAnimation(PopupAnimation.TranslateFromBottom)
                                    .navigationBarColor(Color.BLACK)
                                    .asCustom(ShareUIBottomPopup(requireContext()))
                                    .show()
                            }
                        }
                        BUBBLE_NO_RECEIVE -> {
                            mCurWhichBubbleType = COLLECT
                            loadBubbleReceive(
                                taskBubbleShareBean?.id ?: 3,
                                taskBubbleShareBean?.type ?: SHARE
                            )
                        }
                    }
                }
                mDataBinding?.iconLuckDrawBubble, mDataBinding?.iconLuckDrawTv -> {
                    //处理抽奖逻辑
                    when (taskBubbleLuckDrawBean?.status) {
                        BUBBLE_NO_FINISH -> {
                            //跳抽奖
                            ARouter.getInstance()
                                .build(RouterFragmentPath.HomeLottery.PAGER_LOTTERY)
                                .navigation()
                        }
                        BUBBLE_NO_RECEIVE -> {
                            mCurWhichBubbleType = SIGN
                            loadBubbleReceive(
                                taskBubbleLuckDrawBean?.id ?: 2,
                                taskBubbleLuckDrawBean?.type ?: LOTTERY
                            )
                        }
                    }
                }
                mDataBinding?.iconBtn -> {
                    clickAllBubble()
                }
            }
        }
    }

    private fun clickAllBubble(){
        var isHaveCanReceiveBubble = false
        for (index in taskBubbleBean.list.indices) {
            if (taskBubbleBean.list[index].status == BUBBLE_NO_RECEIVE) {
                isHaveCanReceiveBubble = true
                break
            }
        }
        if (isHaveCanReceiveBubble){
            for (index in taskBubbleBean.list.indices) {
                if (taskBubbleBean.list[index].status == BUBBLE_NO_RECEIVE) {
                    when(taskBubbleBean.list[index].type){
                        SIGN->{
                            makeBubbleExplosion(mDataBinding?.iconSignBubble as View)
                            makeBubbleExplosion(mDataBinding?.iconSignTv as View)
                            //签到没有金币效果
                            loadUserAssets()
                        }
                        COLLECT->{
                            makeBubbleExplosion(mDataBinding?.iconCollectBubble as View)
                            makeBubbleExplosion(mDataBinding?.iconCollectTv as View)
                            startCoinGif()
                            loadUserAssets()
                        }
                        LOTTERY->{
                            makeBubbleExplosion(mDataBinding?.iconLuckDrawBubble as View)
                            makeBubbleExplosion(mDataBinding?.iconLuckDrawTv as View)
                            startCoinGif()
                            loadUserAssets()
                        }
                        TURNTABLE->{
                            makeBubbleExplosion(mDataBinding?.iconLuckPanBubble as View)
                            makeBubbleExplosion(mDataBinding?.iconLuckPanTv as View)
                            startCoinGif()
                            loadUserAssets()
                        }
                        SHARE->{
                            makeBubbleExplosion(mDataBinding?.iconShareBubble as View)
                            makeBubbleExplosion(mDataBinding?.shareTv as View)
                            startCoinGif()
                            loadUserAssets()
                        }
                        VIDEO->{
                            startCoinGif()
                            loadUserAssets()
                            loadTaskBubbles()
                        }
                        GIFT_BOX->{

                        }
                    }
                }
            }
        } else Toast.makeText(mContext, "当前没有可点击气泡", Toast.LENGTH_SHORT).show()
    }
    //endregion

    //region 宝箱相关
    /*宝箱相关*/
    private var boxMaxTime = 120
    private var boxCurTime = boxMaxTime

    //宝箱最大开启数, 中台配
    private var boxMaxOpenNum = 5

    //当前宝箱是否可以打开
    private var isCanOpenBox = false

    private val boxTimer = object : Runnable {
        override fun run() {
            if (boxCurTime > 0) {
                boxCurTime--
                mDataBinding?.boxTimeTv?.text = TimeUtils.stringForTimeNoHour(boxCurTime * 1000L)
                //当前宝箱时间倒计时结束
                if (boxCurTime == 0) {
                    isCanOpenBox = true
                    mViewModel?.isShowBoxTimeView?.set(false)
                    startBoxAnimation()
                } else {
                    isCanOpenBox = false
                    mHandler.postDelayed(this, 1000L)
                    cancelBoxAnimation()
                }
            }
        }
    }

    private fun initBox() {
        if (taskBubbleBoxBean?.cd ?: 0 > 0){
            //倒计时未结束
            isCanOpenBox = false
            cancelBoxAnimation()
            mViewModel?.isShowBoxTimeView?.set(true)
            mViewModel?.isShowIconCanGet?.set(false)
            boxCurTime = taskBubbleBoxBean?.cd!!
            mHandler.postDelayed(boxTimer, 1000L)
        } else {
            when(taskBubbleBoxBean?.status){
                BUBBLE_NO_RECEIVE -> {
                    isCanOpenBox = true
                    startBoxAnimation()
                    mViewModel?.isShowBoxTimeView?.set(false)
                    mViewModel?.isShowIconCanGet?.set(true)
                }
                BUBBLE_HAVE_FINISH->{
                    cancelBoxAnimation()
                    mViewModel?.isShowBoxTimeView?.set(true)
                    mViewModel?.isShowIconCanGet?.set(false)
                    mDataBinding?.boxTimeTv?.text = "明日再来"
                }
            }
        }
    }
    //endregion

    //region gif相关
    private var gifDrawable: GifDrawable? = null
    private var gifLeftDrawable: GifDrawable? = null
    private var gifRightDrawable: GifDrawable? = null

    private fun initMainGif() {
        try {
            gifDrawable = GifDrawable(mContext!!.assets, "task_gif.gif")
            mDataBinding?.taskGif?.setImageDrawable(gifDrawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startCoinGif(){
        if (bubbleIsLeftOrRight){
            leftGifStart()
        } else rightGifStart()
    }

    private fun leftGifStart(){
        try {
            gifLeftDrawable = GifDrawable(mContext!!.assets, "task_coin_gif.gif")
            mDataBinding?.leftCoinGif?.setImageDrawable(gifDrawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rightGifStart(){
        try {
            gifRightDrawable = GifDrawable(mContext!!.assets, "task_coin_gif.gif")
            mDataBinding?.rightCoinGif?.setImageDrawable(gifDrawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //endregion

    //region 气泡相关
    private fun initBubble() {
        mDataBinding?.iconSignBubble?.alpha = 0f
        mDataBinding?.iconSignTv?.alpha = 0f
        mDataBinding?.iconLuckPanBubble?.alpha = 0f
        mDataBinding?.iconLuckPanTv?.alpha = 0f
        mDataBinding?.iconCollectBubble?.alpha = 0f
        mDataBinding?.iconCollectTv?.alpha = 0f
        mDataBinding?.iconShareBubble?.alpha = 0f
        mDataBinding?.shareTv?.alpha = 0f
        mDataBinding?.iconLuckDrawBubble?.alpha = 0f
        mDataBinding?.iconLuckDrawTv?.alpha = 0f
        mDataBinding?.coldDownTimer?.alpha = 0f
        mDataBinding?.countDownTimeTv?.alpha = 0f
        mDataBinding?.seeAdTv?.alpha = 0f
    }

    private fun makeBubbleExplosion(bubbleView: View) {
        ExplosionField(mContext, ExplodeParticleFactory()).apply {
            explode(bubbleView)
        }
    }
    //endregion

    //region 获取中台配置数据(部分收据后台气泡任务列表接口给)
    private var taskControlConfig: TaskConfigInfo? = null

    private fun initTaskControl() {
        taskControlConfig = TaskControlUtil.getTaskControlConfig()
        todaySeeAdMaxNum = taskControlConfig?.ad?.todaySeeAdMaxNum ?: 3
        mMaxCountTime = taskControlConfig?.ad?.mMaxCountTime ?: 10
        boxMaxTime = taskControlConfig?.box?.boxMaxTime ?: 120
        boxMaxOpenNum = taskControlConfig?.box?.boxMaxOpenNum ?: 5
    }
    //endregion

    //region 共享viewModel
    private lateinit var mShareVideModel: MainShareViewModel

    private fun initShareViewModel() {
        mShareVideModel = ViewModelProvider(requireActivity()).get(MainShareViewModel::class.java)
    }
    //endregion

    //region 动画相关
    private var shakeAnimation: ObjectAnimator? = null
    private var shakeAnimation1: ObjectAnimator? = null

    private fun startBoxAnimation() {
        shakeAnimation = AnimationUtil.startShakeAnimation(mDataBinding?.iconBox, 1f)
        shakeAnimation1 = AnimationUtil.startShakeAnimation(mDataBinding?.iconCanGet, 1f)
    }

    private fun cancelBoxAnimation() {
        if (shakeAnimation != null && shakeAnimation!!.isRunning) {
            shakeAnimation?.cancel()
            shakeAnimation = null
        }
        if (shakeAnimation1 != null && shakeAnimation1!!.isRunning) {
            shakeAnimation1?.cancel()
            shakeAnimation1 = null
        }
    }

    private fun startFingerAnimation() {
        AnimationUtil.startTaskFingerAnimation(mDataBinding?.fingerAnimation)
    }

    private fun cancelFingerAnimation() {
        AnimationUtil.cancelFingerAnimation(mDataBinding?.fingerAnimation)
    }
    //endregion

    val mHandler = Handler(Looper.getMainLooper())

    override fun onDestroy() {
        super.onDestroy()
        if (shakeAnimation != null && shakeAnimation!!.isRunning) {
            shakeAnimation?.cancel()
            shakeAnimation = null
        }
        if (shakeAnimation1 != null && shakeAnimation1!!.isRunning) {
            shakeAnimation1?.cancel()
            shakeAnimation1 = null
        }
        if (gifDrawable != null && !gifDrawable!!.isRecycled) {
            gifDrawable?.recycle()
            gifDrawable = null
        }
        if (gifLeftDrawable != null && !gifLeftDrawable!!.isRecycled) {
            gifLeftDrawable?.recycle()
            gifLeftDrawable = null
        }
        if (gifRightDrawable != null && !gifRightDrawable!!.isRecycled) {
            gifRightDrawable?.recycle()
            gifRightDrawable = null
        }
    }

}