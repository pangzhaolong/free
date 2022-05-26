package com.donews.lotterypage.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.cd.dn.sdk.models.utils.DNServiceTimeManager.Companion.getIns
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.interstitialfull.SimpleInterstitialFullListener
import com.doing.spike.adapter.SpikeContextAdapter
import com.doing.spike.bean.CombinationSpikeBean
import com.doing.spike.bean.SpikeBean
import com.doing.spike.bean.SpikeBean.GoodsListDTO
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterActivityPath
import com.donews.common.router.RouterFragmentPath
import com.donews.lotterypage.Adapter.ContentAdapter
import com.donews.lotterypage.R
import com.donews.lotterypage.base.BaseFragment
import com.donews.lotterypage.base.LotteryPageBean
import com.donews.lotterypage.base.LotteryPastBean
import com.donews.lotterypage.databinding.LotteryPageLayoutBinding
import com.donews.lotterypage.viewmodel.LotteryPageViewModel
import com.donews.middle.IMainParams
import com.donews.middle.adutils.InterstitialFullAd
import com.donews.middle.adutils.adcontrol.AdControlManager
import com.donews.middle.bean.front.AwardBean
import com.donews.middle.centralDeploy.OutherSwitchConfig
import com.donews.middle.front.FrontConfigManager
import com.donews.middle.front.LotteryConfigManager
import com.donews.middle.utils.ActivityGuideMaskUtil
import com.donews.middle.views.TaskView
import com.donews.yfsdk.moniter.PageMonitor
import com.donews.yfsdk.monitor.InterstitialFullAdCheck
import com.donews.yfsdk.monitor.PageMoniterCheck
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*

@Route(path = RouterFragmentPath.HomeLottery.PAGER_LOTTERY)
class LotteryPageFragment :
    MvvmLazyLiveDataFragment<LotteryPageLayoutBinding, LotteryPageViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.lottery_page_layout
    }

    var mContentAdapter: ContentAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PageMonitor().attach(this, object : PageMonitor.PageListener {
            override fun checkShowAd(): AdCustomError {
                return if (AdControlManager.adControlBean.useInstlFullWhenSwitch) {
                    InterstitialFullAdCheck.isEnable()
                } else {
                    InterstitialFullAdCheck.isEnable()
                }
            }

            override fun getIdleTime(): Int {
                return AdControlManager.adControlBean.noOperationDuration
            }

            override fun showAd() {
                val activity: Activity = requireActivity()
                if (activity == null || activity.isFinishing) {
                    return
                }
                if (activity is IMainParams &&
                    !OutherSwitchConfig.Ins().checkMainTabInterstitial(
                        (activity as IMainParams).getThisFragmentCurrentPos(this@LotteryPageFragment)
                    )
                ) {
                    //后台设置当前Tab不允许加载插屏
                    return
                }
                if (!AdControlManager.adControlBean.useInstlFullWhenSwitch) {
                    InterstitialFullAd.showAd(activity, object : SimpleInterstitialFullListener() {
                        override fun onAdError(code: Int, errprMsg: String) {
                            super.onAdError(code, errprMsg)
                            Logger.d("晒单页插屏加载广告错误---- code = \$code ,msg =  \$errorMsg ")
                        }

                        override fun onAdClose() {
                            super.onAdClose()
                            PageMoniterCheck.showAdSuccess("mine_fragment")
                        }
                    })
                } else {
                    InterstitialFullAd.showAd(activity, object : SimpleInterstitialFullListener() {
                        override fun onAdError(errorCode: Int, errprMsg: String) {
                            super.onAdError(errorCode, errprMsg)
                            Logger.d("晒单页插全屏加载广告错误---- code = \$errorCode ,msg =  \$errprMsg ")
                        }

                        override fun onAdClose() {
                            super.onAdClose()
                            PageMoniterCheck.showAdSuccess("mine_fragment")
                        }
                    })
                }
            }
        })
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        activity?.apply {
            if (!ActivityGuideMaskUtil.getGuideShowRecord(
                    this, R.id.accessibility_custom_action_0
                )
            ) {
                //如果被显示过。那么设置为已经引导过了
                ActivityGuideMaskUtil.saveGuideShowRecord(
                    this, R.id.accessibility_custom_action_0, true
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //运营位
        if (LotteryConfigManager.Ins().getConfigBean().getTask()) {
            mDataBinding.advertise.setVisibility(View.VISIBLE);
            mDataBinding.advertise.refreshYyw(TaskView.Place_Front);
        } else {
            mDataBinding.advertise.setVisibility(View.GONE);
        }
        mDataBinding.popular.setOnClickListener(View.OnClickListener {
            RouterFragmentPath.Unboxing.goUnboxingActivity()
        })
        mDataBinding.openLottery.setOnClickListener(View.OnClickListener {
            ARouter.getInstance()
                .build(RouterActivityPath.Mine.PAGER_MINE_WINNING_CODE_ACTIVITY)
                .navigation();
        })

        mContentAdapter = ContentAdapter(this.requireContext());
        mContentAdapter?.getLayout(R.layout.lottery_page_content_item)
        mDataBinding.spikeContentRecyclerView.layoutManager = LinearLayoutManager(
            context
        )
        mDataBinding.spikeContentRecyclerView.adapter = mContentAdapter
        mContentAdapter?.setOnItemClickListener(object : ContentAdapter.OnItemClickListener {
            override fun onItemClick(goodsListDTO: LotteryPageBean.ListDTO?) {
                if (goodsListDTO != null) {
                    ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", goodsListDTO.goodsId)
                        .navigation()
                }
            }
        })
        initContent()
        dataObservation()
        startTime()
    }


    private fun startTime() {
        timeFormat();

    }


    private fun timeFormat() {
        val calendar = Calendar.getInstance()
        //服务器时间
        val time = getIns().getServiceTime()
        calendar.timeInMillis = time
        //小时
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        //分钟
        var minute = calendar.get(Calendar.MINUTE)
        //秒
        var second = calendar.get(Calendar.SECOND)
        if (hour <= 10) {
            //剩余的小时
            var h = (10 - hour) * 60 * 60 * 1000
            //剩余的分钟
            var f = (60 - minute) * 60 * 1000
            //剩余的分钟
            var s = (60 - second) * 1000

            var sum = h + f + s;
            mDataBinding.newUserGiftCountDown.updateCountDownTime(sum.toLong())
            mDataBinding.titleTime.visibility = View.VISIBLE
            mDataBinding.newUserGiftCountDown.visibility = View.VISIBLE
        } else {
            mDataBinding.titleTime.text = "每日10点开奖"
            mDataBinding.titleTime.visibility = View.VISIBLE
            mDataBinding.newUserGiftCountDown.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        initContent()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //观察请求回来的数据
    fun dataObservation() {
        mViewModel.liveData.observe(viewLifecycleOwner, object : Observer<LotteryPageBean?> {
            override fun onChanged(t: LotteryPageBean?) {
                if (t != null) {
                    mContentAdapter?.setLotteryPageBean(t)
                    mContentAdapter?.notifyDataSetChanged()
                }
            }
        })
        //往期人员
        mViewModel.livePastData.observe(viewLifecycleOwner, object : Observer<AwardBean?> {
            override fun onChanged(t: AwardBean?) {
                mDataBinding.revealView.setData(t?.list)
                mDataBinding.revealView.startAnimation()
//                mDataBinding.reveal.refreshData(t?.list)
            }
        })
    }

    private fun initContent() {
        mViewModel.requestInternetData()
    }


}







