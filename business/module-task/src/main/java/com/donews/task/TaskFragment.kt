package com.donews.task

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.donews.base.utils.ToastUtil
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.task.databinding.TaskFragmentBinding
import com.donews.task.extend.setOnClickListener
import com.donews.task.util.AnimationUtil
import com.donews.task.util.DayBoxUtil
import com.donews.task.util.DialogUtil
import com.donews.task.util.TimeUtils
import com.donews.task.view.ColdDownTimerView
import com.donews.task.vm.TaskViewModel
import com.donews.utilslibrary.utils.SPUtils
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
    }

    private fun initColdTimerView() {
        mDataBinding?.coldDownTimer?.apply {
            setWaitHint("冷却中...")
            setCountTime(10)
            setOnClickListener { mDataBinding?.coldDownTimer?.startCountdown() }
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

    private fun initClick() {
        setOnClickListener(
            mDataBinding?.ruleClick, mDataBinding?.activityTxBtn,
            mDataBinding?.iconCanGet, mDataBinding?.iconBox
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
            }
        }
    }

    /*宝箱相关*/
    private var boxMaxTime = 120
    private var boxCurTime = boxMaxTime

    //宝箱最大开启数, 中台配
    private var boxMaxOpenNum = 5

    //当前宝箱是否可以打开
    private var isCanOpenBox = false

    val mHandler = Handler(Looper.getMainLooper())

    //region 宝箱相关
    private val boxTimer = object : Runnable {
        override fun run() {
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

    private var gifDrawable: GifDrawable? = null

    private fun initGif() {
        try {
            gifDrawable = GifDrawable(mContext!!.assets, "task_gif.gif")
            mDataBinding?.taskGif?.setImageDrawable(gifDrawable)
        } catch (e: Exception) {
            e.printStackTrace()
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
            if (boxEndTime < System.currentTimeMillis()) {
                isCanOpenBox = false
                cancelBoxAnimation()
                mViewModel?.isShowBoxTimeView?.set(true)
                mViewModel?.isShowIconCanGet?.set(false)
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