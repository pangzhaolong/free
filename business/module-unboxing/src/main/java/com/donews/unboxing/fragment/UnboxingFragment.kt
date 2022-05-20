package com.donews.unboxing.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.BarUtils
import com.dn.sdk.AdCustomError
import com.dn.sdk.listener.interstitial.SimpleInterstitialFullListener
import com.dn.sdk.listener.interstitial.SimpleInterstitialListener
import com.donews.base.utils.ToastUtil
import com.donews.common.base.MvvmLazyLiveDataFragment
import com.donews.common.router.RouterFragmentPath
import com.donews.middle.adutils.InterstitialAd
import com.donews.middle.adutils.InterstitialFullAd
import com.donews.middle.adutils.adcontrol.AdControlManager
import com.donews.middle.views.TaskView
import com.donews.unboxing.R
import com.donews.unboxing.adapter.UnboxingRVAdapter
import com.donews.unboxing.bean.UnboxingBean
import com.donews.unboxing.databinding.UnboxingFragUnboxingBinding
import com.donews.unboxing.smartrefreshlayout.SmartRefreshState
import com.donews.unboxing.viewmodel.UnboxingViewModel
import com.donews.utilslibrary.analysis.AnalysisUtils
import com.donews.utilslibrary.dot.Dot
import com.donews.yfsdk.check.InterstitialAdCheck
import com.donews.yfsdk.moniter.PageMonitor
import com.donews.yfsdk.monitor.InterstitialFullAdCheck
import com.donews.yfsdk.monitor.PageMoniterCheck
import com.orhanobut.logger.Logger

/**
 * 晒单页 fragment
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/20 17:43
 */
@Route(path = RouterFragmentPath.Unboxing.PAGER_UNBOXING_FRAGMENT)
class UnboxingFragment :
        MvvmLazyLiveDataFragment<UnboxingFragUnboxingBinding, UnboxingViewModel>() {

    private val unboxingRVAdapter = UnboxingRVAdapter(R.layout.unboxing_item_unboxing)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PageMonitor().attach(this, object : PageMonitor.PageListener {
            override fun getIdleTime(): Int {
                return AdControlManager.adControlBean.noOperationDuration
            }

            override fun showAd() {
                activity?.let {
                    if (!AdControlManager.adControlBean.useInstlFullWhenSwitch) {
                        InterstitialAd.showAd(it, object : SimpleInterstitialListener() {
                            override fun onAdError(code: Int, errorMsg: String?) {
                                super.onAdError(code, errorMsg)
                                Logger.d("晒单页插屏加载广告错误---- code = $code ,msg =  $errorMsg ");
                            }

                            override fun onAdClosed() {
                                super.onAdClosed()
                                PageMoniterCheck.showAdSuccess("unbox_fragment")
                            }
                        })
                    } else {
                        InterstitialFullAd.showAd(it, object : SimpleInterstitialFullListener() {
                            override fun onAdError(errorCode: Int, errprMsg: String) {
                                super.onAdError(errorCode, errprMsg)
                                Logger.d("晒单页插全屏加载广告错误---- code = $errorCode ,msg =  $errprMsg ");
                            }

                            override fun onAdClose() {
                                super.onAdClose()
                                PageMoniterCheck.showAdSuccess("unbox_fragment")
                            }
                        })
                    }
                }
            }

            override fun checkShowAd(): AdCustomError {
                return if (AdControlManager.adControlBean.useInstlFullWhenSwitch) {
                    InterstitialFullAdCheck.isEnable()
                } else {
                    InterstitialAdCheck.isEnable()
                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.unboxing_frag_unboxing
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDataBinding.viewModel = mViewModel
        mDataBinding.onEventListener = OnEventListener()

        unboxingRVAdapter.setOnItemChildClickListener { adapter, _, position ->
            val data: UnboxingBean = adapter.data[position] as UnboxingBean
            ARouter.getInstance()
                    .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                    .withString("goods_id", data.goodsId)
                    .navigation();
        }

        unboxingRVAdapter.setDiffCallback(object : DiffUtil.ItemCallback<UnboxingBean>() {
            override fun areItemsTheSame(oldItem: UnboxingBean, newItem: UnboxingBean): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UnboxingBean, newItem: UnboxingBean): Boolean {
                return oldItem.id == newItem.id
            }

        })
        mDataBinding.rvUnboxing.adapter = unboxingRVAdapter
        mViewModel.run {
            listData.observe(this@UnboxingFragment.viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    val data = it.toMutableList()
                    unboxingRVAdapter.setDiffNewData(data)
                }
            })
        }
        mDataBinding.tvShowProduct.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ToastUtil.show(context, "中奖后才可晒单噢！")
            }
        })
        mDataBinding.unboxingTaskView.refreshYyw(TaskView.Place_Show)
    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        //加载数据
        mViewModel.refreshing.value = SmartRefreshState(true)
        //显示顶部距离,达到侵入式状态栏
        var lp = mDataBinding.tvTitle.layoutParams
        lp.height = lp.height + BarUtils.getStatusBarHeight()
        mDataBinding.tvTitle.layoutParams = lp
        mDataBinding.tvTitle.setPadding(
                mDataBinding.tvTitle.paddingLeft,
                mDataBinding.tvTitle.paddingTop + BarUtils.getStatusBarHeight(),
                mDataBinding.tvTitle.paddingRight,
                mDataBinding.tvTitle.paddingBottom
        )

        lp = mDataBinding.tvShowProduct.layoutParams
        lp.height = lp.height + BarUtils.getStatusBarHeight()
        mDataBinding.tvShowProduct.layoutParams = lp
        mDataBinding.tvShowProduct.setPadding(
                mDataBinding.tvShowProduct.paddingLeft,
                mDataBinding.tvShowProduct.paddingTop + BarUtils.getStatusBarHeight(),
                mDataBinding.tvShowProduct.paddingRight,
                mDataBinding.tvShowProduct.paddingBottom
        )
    }


    inner class OnEventListener {
        @SuppressLint("NotifyDataSetChanged")
        fun refresh() {
            Logger.d("刷新")
            mViewModel.onRefresh.invoke()
            mDataBinding.unboxingTaskView.refreshYyw(TaskView.Place_Show)
            mDataBinding.rvUnboxing.adapter?.notifyDataSetChanged()
        }
    }

}