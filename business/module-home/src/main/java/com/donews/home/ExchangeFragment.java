package com.donews.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SPUtils;
import com.dn.sdk.AdCustomError;
import com.dn.sdk.listener.interstitialfull.SimpleInterstitialFullListener;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.adapter.ExchangeFragmentAdapter;
import com.donews.home.databinding.ExchanageFragmentBinding;
import com.donews.home.dialogs.ExchangeRuleDialog;
import com.donews.home.viewModel.ExchangeViewModel;
import com.donews.middle.IMainParams;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.middle.adutils.adcontrol.AdControlManager;
import com.donews.middle.bean.home.HomeCategory2Bean;
import com.donews.middle.bean.home.HomeEarnCoinReq;
import com.donews.middle.bean.home.HomeReceiveGiftReq;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.centralDeploy.OutherSwitchConfig;
import com.donews.middle.dialog.qbn.DoingResultDialog;
import com.donews.middle.events.HomeGoodGetJbSuccessEvent;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.viewmodel.BaseMiddleViewModel;
import com.donews.middle.views.ExchanageTabItem;
import com.donews.middle.views.TaskView;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.yfsdk.loader.AdManager;
import com.donews.yfsdk.moniter.PageMonitor;
import com.donews.yfsdk.monitor.InterstitialFullAdCheck;
import com.donews.yfsdk.monitor.PageMoniterCheck;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.cd.dn.sdk.models.utils.DNServiceTimeManager;

/**
 * @author lcl
 * Date on 2022/5/12
 * Description:
 * 首页 -> 兑换Fragment
 */
@Route(path = RouterFragmentPath.Home.PAGER_EXCHANGE_FRAGMENT)
public class ExchangeFragment extends MvvmLazyLiveDataFragment<ExchanageFragmentBinding, ExchangeViewModel> {

    private HomeCategory2Bean mHomeBean;
    // Tab的Fragment
    private ExchangeFragmentAdapter mFragmentAdapter;
    private boolean isInitLoad = true;

    private int downcountCountTime = 0;
    private Runnable downcountTimeTask = null;
    private Handler mHand = new Handler();
    private StringBuffer tvTimeTxt = new StringBuffer();

    @Override
    public int getLayoutId() {
        return R.layout.exchanage_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        new PageMonitor().attach(this, new PageMonitor.PageListener() {
            @NonNull
            @Override
            public AdCustomError checkShowAd() {
                if (AdControlManager.INSTANCE.getAdControlBean().getUseInstlFullWhenSwitch()) {
                    return InterstitialFullAdCheck.INSTANCE.isEnable();
                } else {
                    return InterstitialFullAdCheck.INSTANCE.isEnable();
                }
            }

            @Override
            public int getIdleTime() {
                return AdControlManager.INSTANCE.getAdControlBean().getNoOperationDuration();
            }

            @Override
            public void showAd() {
                Activity activity = requireActivity();
                if (activity == null || activity.isFinishing()) {
                    return;
                }
                if (activity instanceof IMainParams &&
                        !OutherSwitchConfig.Ins().checkMainTabInterstitial(
                                ((IMainParams) activity).getThisFragmentCurrentPos(ExchangeFragment.this))) {
                    //后台设置当前Tab不允许加载插屏
                    return;
                }
                if (!AdControlManager.INSTANCE.getAdControlBean().getUseInstlFullWhenSwitch()) {
                    InterstitialFullAd.INSTANCE.showAd(activity, new SimpleInterstitialFullListener() {
                        @Override
                        public void onAdError(int code, String errorMsg) {
                            super.onAdError(code, errorMsg);
                            Logger.d("晒单页插屏加载广告错误---- code = $code ,msg =  $errorMsg ");
                        }

                        @Override
                        public void onAdClose() {
                            super.onAdClose();
                            PageMoniterCheck.INSTANCE.showAdSuccess("mine_fragment");
                        }
                    });
                } else {
                    InterstitialFullAd.INSTANCE.showAd(activity, new SimpleInterstitialFullListener() {
                        @Override
                        public void onAdError(int errorCode, String errprMsg) {
                            super.onAdError(errorCode, errprMsg);
                            Logger.d("晒单页插全屏加载广告错误---- code = $errorCode ,msg =  $errprMsg ");
                        }

                        @Override
                        public void onAdClose() {
                            super.onAdClose();
                            PageMoniterCheck.INSTANCE.showAdSuccess("mine_fragment");
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        initDataBinding();
        // 运营位
        if (FrontConfigManager.Ins().getConfigBean().getTask()) {
            mDataBinding.taskGroupLlNew.setVisibility(View.VISIBLE);
            mDataBinding.taskGroupLlNew.refreshYyw(TaskView.Place_Front);
        } else {
            mDataBinding.taskGroupLlNew.setVisibility(View.GONE);
        }
        mFragmentAdapter = new ExchangeFragmentAdapter(this);
        mDataBinding.homeCategoryVp2.setAdapter(mFragmentAdapter);
        // Tab相关列表
        mDataBinding.homeCategoryTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.homeCategoryTl, mDataBinding.homeCategoryVp2, (tab1, position) -> {
            /*if (position == 0) {
                if (tab1.getCustomView() == null) {
                    tab1.setCustomView(new TabItem(mContext));
                }
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                tabItem.setTitle("精品");
            } else {*/

            if (tab1.getCustomView() == null) {
                tab1.setCustomView(new ExchanageTabItem(getActivity()));
            }
            ExchanageTabItem tabItem = (ExchanageTabItem) tab1.getCustomView();
            assert tabItem != null;
            if (mHomeBean == null || mHomeBean.list == null) {
                return;
            }
            tabItem.setTitle(mHomeBean.list.get(position).name);
            /*}*/
        });
        tab.attach();
        // tab选择的监听
        mDataBinding.homeCategoryTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ExchanageTabItem tabItem = (ExchanageTabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ExchanageTabItem tabItem = (ExchanageTabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.unSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ExchanageTabItem tabItem = (ExchanageTabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }
        });

        //订阅是否开始计时的动作
        mViewModel.giftCountdownIsStartCount.observe(this, (item) -> {
            if (item) {
                //开始计时
                int sp = mViewModel.queryGiftCountdownStep();
                if (sp <= 0) {
                    return;//计算结果不支持计时
                }
                mHand.removeCallbacks(downcountTimeTask);
                mDataBinding.tvTime.setVisibility(View.VISIBLE);
                downcountCountTime = sp; //重新开始两分钟计时动作
                if (downcountTimeTask == null) {
                    downcountTimeTask = () -> {
                        downcountCountTime -= 1;
                        mViewModel.giftCountdownCount.postValue(downcountCountTime);
                        if (downcountCountTime > 0) {
                            mHand.postDelayed(downcountTimeTask, 1000);
                        }
                    };
                }
                mHand.postDelayed(downcountTimeTask, 1000);
            }
        });
        //订阅计时动作数值
        mViewModel.giftCountdownCount.observe(this, (timeValue) -> {
            if (timeValue <= 0) {
                mDataBinding.tvTime.setVisibility(View.GONE);
                mDataBinding.homeSearchLb.setEnabled(true);
            } else {
                //更新计时
                tvTimeTxt.delete(0, tvTimeTxt.length());
                int m = timeValue / 60;
                int s = (timeValue - m * 60) % 60;
                if (m > 10) {
                    tvTimeTxt.append("" + m);
                } else {
                    tvTimeTxt.append("0" + m);
                }
                tvTimeTxt.append(":");
                if (s < 10) {
                    tvTimeTxt.append("0" + s);
                } else {
                    tvTimeTxt.append("" + s);
                }
                mDataBinding.tvTime.setText(tvTimeTxt.toString());
            }
        });

        mDataBinding.homeSrl.setEnableLoadMore(false);
        mDataBinding.homeSrl.setOnRefreshListener((refreshLayout) -> {
            //初始化数据
            if (isInitLoad) {
                isInitLoad = false;
            }
            loadCategory();
        });

        //更新配置信息(对话相关的配置)
        mViewModel.getCoinCritConfig();
        //初始化数据
        mDataBinding.homeSrl.autoRefresh();
    }

    private long fastNotifyTime = 0;

    @Subscribe //赚金币
    public void onEventGoodGetJb(HomeGoodGetJbSuccessEvent event) {
        if (System.currentTimeMillis() - fastNotifyTime < 2000) {
            return;
        }
        if (getBaseActivity() instanceof MvvmBaseLiveDataActivity) {
            ((MvvmBaseLiveDataActivity<?, ?>) getBaseActivity()).showLoading("奖励发放中");
        }
        fastNotifyTime = System.currentTimeMillis();
        HomeEarnCoinReq req = new HomeEarnCoinReq();
        req.user_id = AppInfo.getUserId();
        mViewModel.getEarnCoin(req)
                .observe(this, (item) -> {
                    if (getBaseActivity() instanceof MvvmBaseLiveDataActivity) {
                        ((MvvmBaseLiveDataActivity<?, ?>) getBaseActivity()).hideLoading();
                    }
                    if (item != null) {
                        if (BaseMiddleViewModel.getBaseViewModel().mine2JBCount.getValue() != null) {
                            BaseMiddleViewModel.getBaseViewModel().mine2JBCount.postValue(
                                    BaseMiddleViewModel.getBaseViewModel().mine2JBCount.getValue() + item.coin);
                        } else {
                            BaseMiddleViewModel.getBaseViewModel().mine2JBCount.postValue(item.coin);
                        }
                        //显示金币弹窗
                        showDoingResultDialog(item.coin);
                    } else {
                        ToastUtil.showShort(BaseApplication.getInstance(), "奖励发放失败,请稍后重试!");
                    }
                });
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHand.removeCallbacks(downcountTimeTask);
        super.onDestroy();
    }

    //获取搜索左侧的显示文字
    public String getSearchLeftText() {
        return "?";
    }

    //搜索框左侧的点击
    public void getSearchLeftClick() {
        //显示规则弹窗
        ExchangeRuleDialog.Companion.newInstance()
                .show(getChildFragmentManager(), "halsjdflk");
    }

    //搜索框的点击操作
    public void getSearchInputEditClick() {
        Intent intent = new Intent(getContext(), HomeSearch2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private long fastClickTime = 0;

    //搜索框右侧按钮点击
    public void getSearchRightClick() {
        if (System.currentTimeMillis() - fastClickTime < 1000) {
            ToastUtil.showShort(getActivity(), "你点太快了,让手指休息下吧~");
            return;
        }
        fastClickTime = System.currentTimeMillis();
        if (downcountCountTime > 0) {
            ToastUtil.showShort(getActivity(), "距离下次领取还剩 " + mDataBinding.tvTime.getText());
            return;//还在倒计时中
        }

        if (getGiftCurrDayCount() >= 5) {
            ToastUtil.showShort(getActivity(), "今日领取已达上限,请明日再来!");
            return;//还在倒计时中
        }

        MvvmBaseLiveDataActivity activity = (MvvmBaseLiveDataActivity) getActivity();
        activity.showLoading("加载中");
        AdManager.INSTANCE.loadRewardVideoAd(getActivity(), new SimpleRewardVideoListener() {

            //是否发放奖励
            boolean isRewvrdVerify = false;

            @Override
            public void onVideoCached() {
                super.onVideoCached();
                activity.hideLoading();
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                super.onAdError(code, errorMsg);
                activity.hideLoading();
            }

            @Override
            public void onRewardVerify(boolean result) {
                if (result) {
                    isRewvrdVerify = true;
                    invokGiftGet();
                }
            }

            @Override
            public void onAdClose() {
                if (isRewvrdVerify) {
                }
            }
        });
    }

    //执行宝箱奖励领取(只有宝箱打开。视频看完了才会调用)
    private void invokGiftGet() {
        HomeReceiveGiftReq req = new HomeReceiveGiftReq();
        req.user_id = AppInfo.getUserId();
        mViewModel.getReceiveGift(req)
                .observe(this, (resp) -> {
                    if (resp != null) {
                        saveGiftCurrDayCount(-1);
                        showDoingResultDialog(resp.coin);
                    } else {
                        ToastUtil.showShort(getActivity(), "奖励领取失败,请稍后重试!");
                    }
                });
    }

    /**
     * 保存当日保存数量，将当日已观看的数量+1
     *
     * @param syCount 剩余次数(暂时未同步)
     */
    private void saveGiftCurrDayCount(int syCount) {
        try {
            String file = "giftCurrDayCountFile";
            String key = new SimpleDateFormat("yyyyMMdd").format(
                    new Date(DNServiceTimeManager.Companion.getIns().getServiceTime()));
            int count = SPUtils.getInstance(file).getInt(key, -1);
            if (count < 0) {
                SPUtils.getInstance(file).clear();
                SPUtils.getInstance(file).put(key, 1);
            } else {
                SPUtils.getInstance(file).put(key, count + 1);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 获取当日宝箱已打开的次数
     */
    private int getGiftCurrDayCount() {
        try {
            String file = "giftCurrDayCountFile";
            String key = new SimpleDateFormat("yyyyMMdd").format(
                    new Date(DNServiceTimeManager.Companion.getIns().getServiceTime()));
            int count = SPUtils.getInstance(file).getInt(key, -1);
            if (count < 0) {
                return 0;
            } else {
                return count;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    //初始化DataBinding
    private void initDataBinding() {
        mDataBinding.setThiz(this);
        mDataBinding.setVModel(mViewModel);
    }

    //加载Tab页面
    private void loadCategory() {
        mHomeBean = GoodsCache.readGoodsBean(HomeCategory2Bean.class, "exchanage_category");
        if (mHomeBean != null && mHomeBean.list != null && mFragmentAdapter != null) {
            mFragmentAdapter.refreshData(mHomeBean.list);
        }
        mViewModel.getHomeCategoryBean().observe(this, homeBean -> {
            mDataBinding.homeSrl.finishRefresh();
            if (homeBean == null) {
                if (mHomeBean == null) {
                    ToastUtil.showShort(this.getContext(), "数据加载出错,请手动刷新");
                }
                return;
            }
            mHomeBean = homeBean;
            // 处理正常的逻辑。
            if (mFragmentAdapter != null) {
                mFragmentAdapter.refreshData(mHomeBean.list);
            }
            GoodsCache.saveGoodsBean(mHomeBean, "exchanage_category");
        });
    }

    // 显示活动奖励弹窗
    private void showDoingResultDialog(int count) {
        DoingResultDialog dialog = new DoingResultDialog(getActivity(), count, R.drawable.sign_reward_mine_dialog_djb);
        dialog.setStateListener(() -> {
        });
        dialog.show(getActivity());
    }
}
