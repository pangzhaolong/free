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
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.donews.base.utils.ToastUtil
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterActivityPath
import com.donews.common.router.RouterFragmentPath
import com.donews.module_shareui.ShareUIBottomPopup
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
        //懒加载
    }

    private fun setBinding() {
        mDataBinding?.taskModel = mViewModel
    }

    private fun initView() {
        mContext = this.context
        setBinding()
        initClick()
        networkCheck()
    }

    private fun networkCheck() {
        initColdTimerView()
        initGif()
        initBox()
        initBubble()
    }

    //region 每日看广告气泡相关
    //今日看广告最大数, 中台配
    private var todaySeeAdMaxNum = 3
    private var mTodaySeeAdNum = 0
    //冷却倒计时最大值10s中台配
    private var mMaxCountTime = 10

    @SuppressLint("SetTextI18n")
    private fun initColdTimerView() {
        mTodaySeeAdNum = SPUtils.getInformain("todaySeeAdNum", 0)
        mDataBinding?.seeAdTv?.text = "可领取($mTodaySeeAdNum/3)"
        if (mTodaySeeAdNum == todaySeeAdMaxNum){
            mDataBinding?.coldDownTimer?.alpha = 0.45f
            mDataBinding?.seeAdTv?.text = "明日再来"
        } else {
            mDataBinding?.coldDownTimer?.alpha = 1f
            mDataBinding?.seeAdTv?.text = "可领取($mTodaySeeAdNum/3)"
        }
        mDataBinding?.coldDownTimer?.apply {
            setCountTime(mMaxCountTime)
            setOnCountDownTimeListener(object : ColdDownTimerView.CountDownTimeListener {
                override fun getCurCountDownTime(time: Int) {
                    mTodaySeeAdNum = SPUtils.getInformain("todaySeeAdNum", 0)
                    mDataBinding?.coldDownTimer?.alpha = 1f
                    mDataBinding?.seeAdTv?.text = "可领取(" + (mTodaySeeAdNum + 1) + "/3)"
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
                    SPUtils.setInformain("todaySeeAdNum", mTodaySeeAdNum + 1)
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
            mDataBinding?.iconSignBubble,mDataBinding?.iconSignTv,//签到气泡
            mDataBinding?.iconLuckPanBubble,mDataBinding?.iconLuckPanTv,//转盘气泡
            mDataBinding?.iconCollectBubble,mDataBinding?.iconCollectTv,//集卡气泡
            mDataBinding?.iconShareBubble,mDataBinding?.shareTv,//分享气泡
            mDataBinding?.iconLuckDrawBubble,mDataBinding?.iconLuckDrawTv,//抽奖气泡
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
                        Toast.makeText(mContext,"一波广告走起",Toast.LENGTH_SHORT).show()
                        val mTodayOpenBoxNum = SPUtils.getInformain("todayOpenBoxNum", 0)
                        SPUtils.setInformain("todayOpenBoxNum", mTodayOpenBoxNum + 1)
                        SPUtils.setInformain("todayOpenBoxOpenTime", System.currentTimeMillis())
                        mViewModel?.isShowBoxTimeView?.set(true)
                        mViewModel?.isShowIconCanGet?.set(false)
                        if (DayBoxUtil.instance.isShowDayTwentyOpenBox(boxMaxOpenNum)) {
                            SPUtils.setInformain("boxEndTime", System.currentTimeMillis() + boxCurTime * 1000)
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
                mDataBinding?.coldDownTimer->{
                    if (DayAdUtil.instance.isShowDayThreeSeeAd(todaySeeAdMaxNum)) {
                        if (mDataBinding?.coldDownTimer?.getIsCountDownOver()!!){
                            //ad
                            Toast.makeText(mContext,"一波广告走起",Toast.LENGTH_SHORT).show()
                            SPUtils.setInformain("todaySeeAdTime", System.currentTimeMillis())
                            mTodaySeeAdNum = SPUtils.getInformain("todaySeeAdNum", 0)
                            when{
                                mTodaySeeAdNum < todaySeeAdMaxNum - 1->{
                                    mDataBinding?.coldDownTimer?.startCountdown()
                                }
                                mTodaySeeAdNum == todaySeeAdMaxNum - 1 ->{
                                    mDataBinding?.coldDownTimer?.alpha = 0.45f
                                    mDataBinding?.seeAdTv?.text = "明日再来"
                                    SPUtils.setInformain("todaySeeAdNum", mTodaySeeAdNum + 1)
                                }
                                else->{
                                    mDataBinding?.coldDownTimer?.alpha = 0.45f
                                    mDataBinding?.seeAdTv?.text = "明日再来"
                                }
                            }
                        }
                    }
                }
                mDataBinding?.iconSignBubble,mDataBinding?.iconSignTv->{
                    if (!isExplosionBubble){
                        //处理签到逻辑
                        isExplosionBubble = true
                    } else {
                        makeBubbleExplosion(mDataBinding?.iconSignBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconSignTv as View)
                    }
                }
                mDataBinding?.iconLuckPanBubble,mDataBinding?.iconLuckPanTv->{
                    //处理转盘逻辑
                    if (!isExplosionBubble){
                        ARouter.getInstance()
                            .build(RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
                            .navigation()
                    } else {
                        makeBubbleExplosion(mDataBinding?.iconLuckPanBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconLuckPanTv as View)
                    }
                }
                mDataBinding?.iconCollectBubble,mDataBinding?.iconCollectTv->{
                    //处理集卡逻辑
                    makeBubbleExplosion(mDataBinding?.iconCollectBubble as View)
                    makeBubbleExplosion(mDataBinding?.iconCollectTv as View)
                }
                mDataBinding?.iconShareBubble,mDataBinding?.shareTv->{
                    //处理分享逻辑
                    makeBubbleExplosion(mDataBinding?.iconShareBubble as View)
                    makeBubbleExplosion(mDataBinding?.shareTv as View)
                }
                mDataBinding?.iconShareBubble,mDataBinding?.shareTv->{
                    //处理每日看广告逻辑
                    if (context != null){
                        XPopup.Builder(activity)
                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                            .popupAnimation(PopupAnimation.TranslateFromBottom)
                            .navigationBarColor(Color.BLACK)
                            .asCustom(ShareUIBottomPopup(requireContext()))
                            .show()
                    }
                }
                mDataBinding?.iconLuckDrawBubble,mDataBinding?.iconLuckDrawTv->{
                    //处理抽奖逻辑
                    makeBubbleExplosion(mDataBinding?.iconLuckDrawBubble as View)
                    makeBubbleExplosion(mDataBinding?.iconLuckDrawTv as View)
                }
                mDataBinding?.iconBtn->{
                    if (!isExplosionBubble){
                        Toast.makeText(mContext,"当前没有可点击气泡",Toast.LENGTH_SHORT).show()
                    } else {
                        makeBubbleExplosion(mDataBinding?.iconSignBubble as View)
                        makeBubbleExplosion(mDataBinding?.iconSignTv as View)
                    }
                }
            }
        }
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
            if (boxCurTime > 0){
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
        val boxEndTime = SPUtils.getLongInformain("boxEndTime", 0L)
        if (boxEndTime == 0L){
            isCanOpenBox = true
            startBoxAnimation()
            mViewModel?.isShowBoxTimeView?.set(false)
            mViewModel?.isShowIconCanGet?.set(true)
        } else {
            if (boxEndTime > System.currentTimeMillis()) {
                //倒计时未结束
                isCanOpenBox = false
                cancelBoxAnimation()
                mViewModel?.isShowBoxTimeView?.set(true)
                mViewModel?.isShowIconCanGet?.set(false)
                boxCurTime = ((boxEndTime - System.currentTimeMillis()) / 1000).toInt()
                mHandler.postDelayed(boxTimer, 1000L)
            } else {
                DayBoxUtil.instance.isShowDayTwentyOpenBox(boxMaxOpenNum).let {
                    if (it) {
                        isCanOpenBox = true
                        startBoxAnimation()
                        mViewModel?.isShowBoxTimeView?.set(false)
                        mViewModel?.isShowIconCanGet?.set(true)
                    } else {
                        cancelBoxAnimation()
                        mViewModel?.isShowBoxTimeView?.set(true)
                        mViewModel?.isShowIconCanGet?.set(false)
                        mDataBinding?.boxTimeTv?.text = "明日再来"
                    }
                }
            }
        }
    }
    //endregion

    //region gif相关
    private var gifDrawable: GifDrawable? = null

    private fun initGif() {
        try {
            gifDrawable = GifDrawable(mContext!!.assets, "task_gif.gif")
            mDataBinding?.taskGif?.setImageDrawable(gifDrawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //endregion

    //region 气泡相关
    private var isExplosionBubble = false
    private fun initBubble(){
        if (!isExplosionBubble){
            mDataBinding?.iconSignBubble?.alpha = 0.45f
            mDataBinding?.iconLuckPanBubble?.alpha = 0.45f
            mDataBinding?.iconCollectBubble?.alpha = 0.45f
            mDataBinding?.iconShareBubble?.alpha = 0.45f
            mDataBinding?.iconLuckDrawBubble?.alpha = 0.45f
        } else {
            mDataBinding?.iconSignBubble?.alpha = 1f
            mDataBinding?.iconLuckPanBubble?.alpha = 1f
            mDataBinding?.iconCollectBubble?.alpha = 1f
            mDataBinding?.iconShareBubble?.alpha = 1f
            mDataBinding?.iconLuckDrawBubble?.alpha = 1f
            startFingerAnimation()
        }
    }

    private fun makeBubbleExplosion(bubbleView: View){
        ExplosionField(mContext, ExplodeParticleFactory()).apply {
            explode(bubbleView)
        }
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

    private fun startFingerAnimation(){
        AnimationUtil.startTaskFingerAnimation(mDataBinding?.fingerAnimation)
    }

    private fun cancelFingerAnimation(){
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
    }

}