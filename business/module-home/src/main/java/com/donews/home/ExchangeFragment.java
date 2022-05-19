package com.donews.home;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.adapter.ExchangeFragmentAdapter;
import com.donews.home.databinding.ExchanageFragmentBinding;
import com.donews.home.viewModel.ExchangeViewModel;
import com.donews.middle.bean.home.HomeCategory2Bean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeReceiveGiftReq;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.viewmodel.BaseMiddleViewModel;
import com.donews.middle.views.ExchanageTabItem;
import com.donews.middle.views.TaskView;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.yfsdk.loader.AdManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
                if (downcountCountTime <= 0) {
                    return;//计算结果不支持计时
                }
                mDataBinding.tvTime.setVisibility(View.VISIBLE);
                downcountCountTime = sp;
                if (downcountTimeTask == null) {
                    downcountTimeTask = () -> {
                        mViewModel.giftCountdownCount.postValue(downcountCountTime--);
                        if (mViewModel.giftCountdownCount.getValue() > 0) {
                            mHand.postDelayed(downcountTimeTask, 1000);
                        }
                    };
                }
                mHand.postDelayed(downcountTimeTask, 1000);
                mDataBinding.homeSearchLb.setEnabled(false);
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
                int s = timeValue % 60;
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
            isInitLoad = false;
            loadCategory();
        });

        isInitLoad = true;
        //更新配置信息(对话相关的配置)
        mViewModel.getCoinCritConfig();
        //初始化数据
        loadCategory();
    }

    @Override
    public void onDestroy() {
        mHand.removeCallbacks(downcountTimeTask);
        super.onDestroy();
    }

    //获取搜索左侧的显示文字
    public String getSearchLeftText() {
        return "?";
    }

    //搜索框左侧的点击
    public void getSearchLeftClick() {
        ToastUtil.showShort(getActivity(), "搜索左侧点击了");
    }

    //搜索框的点击操作
    public void getSearchInputEditClick() {
        Intent intent = new Intent(getContext(), HomeSearch2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //搜索框右侧按钮点击
    public void getSearchRightClick() {
        mDataBinding.homeSearchLb.setEnabled(false);

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
                    ToastUtil.showShort(activity, "要参与活动才能领取奖励哦~");
                }
            }
        });
    }

    //执行宝箱奖励领取
    private void invokGiftGet() {
        HomeReceiveGiftReq req = new HomeReceiveGiftReq();
        req.user_id = AppInfo.getUserId();
        mViewModel.getReceiveGift(req)
                .observe(this, (resp) -> {
                    if (resp != null) {
                        ToastUtil.showShort(getContext(), "奖励发放成功了");
                    }
                });
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
}
