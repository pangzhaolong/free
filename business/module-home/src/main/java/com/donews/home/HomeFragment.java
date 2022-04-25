package com.donews.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.Glide;
import com.dn.sdk.AdCustomError;
import com.dn.sdk.listener.interstitial.SimpleInterstitialFullListener;
import com.dn.sdk.listener.interstitial.SimpleInterstitialListener;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.adapter.FragmentAdapter;
import com.donews.home.databinding.HomeFragmentBinding;
import com.donews.home.viewModel.HomeViewModel;
import com.donews.middle.adutils.InterstitialAd;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.middle.adutils.adcontrol.AdControlManager;
import com.donews.middle.bean.home.FactorySaleBean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.TopIconsBean;
import com.donews.middle.bean.home.UserBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.go.GotoUtil;
import com.donews.middle.views.TabItem;
import com.donews.utilslibrary.utils.UrlUtils;
import com.donews.yfsdk.check.InterstitialAdCheck;
import com.donews.yfsdk.moniter.PageMonitor;
import com.donews.yfsdk.monitor.InterstitialFullAdCheck;
import com.donews.yfsdk.monitor.PageMoniterCheck;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.orhanobut.logger.Logger;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/11/10 18:13<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends MvvmLazyLiveDataFragment<HomeFragmentBinding, HomeViewModel> {
    boolean isFirst = false;
    private FragmentAdapter mFragmentAdapter;
    private HomeCategoryBean mHomeBean;

    private Context mContext;

    private Timer mLoadUserAndLoadSecKilTimer = null;
    private TimerTask mLoadUserListTask = null;
    private TimerTask mLoadSecKilTask = null;

    private boolean mIsResumed = false;

    //banner图标
    private List<String> bannerStr = new ArrayList() {
        {
            add("https://img.alicdn.com/imgextra/i2/2053469401/O1CN01iLaErN2JJhz69QDna_!!2053469401.jpg");
            add("https://img.alicdn.com/imgextra/i1/2053469401/O1CN01oKK8D32JJi3C0Twb4_!!2053469401.jpg");
            add("https://img.alicdn.com/imgextra/i1/2053469401/O1CN01tO2vcm2JJi0KdNVOD_!!2053469401.jpg");
            add("https://img.alicdn.com/imgextra/i4/2053469401/O1CN01tA6DEy2JJi559xG1M_!!2053469401.jpg");
            add("https://img.alicdn.com/imgextra/i4/2053469401/O1CN01sZei422JJi3BIqYCi_!!2053469401.jpg");
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PageMonitor().attach(this, new PageMonitor.PageListener() {
            @NonNull
            @Override
            public AdCustomError checkShowAd() {
                if (AdControlManager.INSTANCE.getAdControlBean().getUseInstlFullWhenSwitch()) {
                    return InterstitialFullAdCheck.INSTANCE.isEnable();
                } else {
                    return InterstitialAdCheck.INSTANCE.isEnable();
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
                if (!AdControlManager.INSTANCE.getAdControlBean().getUseInstlFullWhenSwitch()) {
                    InterstitialAd.INSTANCE.showAd(activity, new SimpleInterstitialListener() {
                        @Override
                        public void onAdError(int code, String errorMsg) {
                            super.onAdError(code, errorMsg);
                            Logger.d("晒单页插屏加载广告错误---- code = $code ,msg =  $errorMsg ");
                        }

                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            PageMoniterCheck.INSTANCE.showAdSuccess("home_fragment");
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
                            PageMoniterCheck.INSTANCE.showAdSuccess("home_fragment");
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        isFirst = true;
        //显示顶部距离,达到侵入式状态栏
        ViewGroup.LayoutParams lp = mDataBinding.homeSearchLayout.getLayoutParams();
        lp.height = lp.height + BarUtils.getStatusBarHeight();
        mDataBinding.homeSearchLayout.setLayoutParams(lp);
        mDataBinding.homeSearchLayout.setPadding(
                mDataBinding.homeSearchLayout.getPaddingLeft(),
                mDataBinding.homeSearchLayout.getPaddingTop() + BarUtils.getStatusBarHeight(),
                mDataBinding.homeSearchLayout.getPaddingRight(),
                mDataBinding.homeSearchLayout.getPaddingBottom()
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = this.getContext();
        mFragmentAdapter = new FragmentAdapter(this);
        mDataBinding.homeCategoryVp2.setAdapter(mFragmentAdapter);
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
                tab1.setCustomView(new TabItem(mContext));
            }
            TabItem tabItem = (TabItem) tab1.getCustomView();
            assert tabItem != null;
            if (mHomeBean == null || mHomeBean.getList() == null) {
                return;
            }
            tabItem.setTitle(mHomeBean.getList().get(position).getCname());
            /*}*/
        });
        tab.attach();

        mDataBinding.homeCategoryTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.unSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabItem tabItem = (TabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }
        });
        mDataBinding.homeSearchRl.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HomeSearchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        mDataBinding.homeJddHelp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HomeHelpActivity.class);
            startActivity(intent);
        });

        mDataBinding.homeBannerLl.setOnClickListener(v ->
                ARouter.getInstance().build(RouterActivityPath.Home.CRAZY_LIST_DETAIL).navigation());
        mDataBinding.homeSaleLayout.setOnClickListener(v->
                        ARouter.getInstance().build(RouterActivityPath.Home.FACTORY_SALE).navigation()
                );

        mDataBinding.homeTitleTb.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                .withString("from", "tb")
                .navigation());
        mDataBinding.homeTitlePdd.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                .withString("from", "pdd")
                .navigation());
        mDataBinding.homeTitleJd.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                .withString("from", "jd")
                .navigation());
        mDataBinding.homeTitleElm.setOnClickListener(v -> GotoUtil.gotoElem(mContext, "https://s.click.ele.me/PvxZeeu"));
        initSrl();

        loadCategory();
        loadUserList();
//        loadSecKilData();
        loadRankListData();
        factorySale();
        loadTopIcons();
        mDataBinding.homeHeadBanner
                .setLifecycleRegistry(getActivity().getLifecycle())
                .setIndicatorVisibility(View.GONE)
                .setRoundCorner(ConvertUtils.dp2px(10))
                .setAdapter(new BaseBannerAdapter<String>() {
                    @Override
                    protected void bindData(BaseViewHolder<String> holder, String data, int position, int pageSize) {
                        ImageView icon = holder.itemView.findViewById(R.id.home_head_banner_iv);
                        GlideUtils.loadImageView(getActivity(), data, icon);
                    }

                    @Override
                    public int getLayoutId(int viewType) {
                        return R.layout.home_head_banner_item;
                    }

                })
                .setCanLoop(true)
                .setAutoPlay(true)
                .create();
        mDataBinding.homeHeadBanner.refreshData(bannerStr);
    }

    private void initSrl() {
        mDataBinding.homeSrl.setOnRefreshListener(refreshLayout -> {
            loadCategory();
//            loadSecKilData();
            loadRankListData();
            loadUserList();
            loadTopIcons();
            mDataBinding.homeSrl.finishRefresh();
        });
    }

    private void loadTopIcons() {
        mViewModel.getTopIcons().observe(this.getViewLifecycleOwner(), topIconsBean -> {
            if (topIconsBean == null || topIconsBean.getItems() == null || topIconsBean.getItems().size() <= 0) {
                return;
            }
            showTopIcons(topIconsBean);
        });
    }

    private int mPageId = 1;

    private void loadRankListData() {
        RealTimeBean realTimeBean = GoodsCache.readGoodsBean(RealTimeBean.class, "home_banner");
        showRankListBean(realTimeBean);
        mPageId++;
        mViewModel.getRankListData(mPageId).observe(this.getViewLifecycleOwner(), realTimeBean1 -> {
            if (realTimeBean1 == null) {
                mPageId = 1;
                return;
            }
            showRankListBean(realTimeBean1);
            GoodsCache.saveGoodsBean(realTimeBean1, "home_banner");
        });
    }

    /*private void loadSecKilData() {
        SecKilBean tmpSecKilBean = GoodsCache.readGoodsBean(SecKilBean.class, "home_banner");
        showSecKilBean(tmpSecKilBean);

        mViewModel.getSecKilData().observe(getViewLifecycleOwner(), secKilBean -> {
            showSecKilBean(secKilBean);
            GoodsCache.saveGoodsBean(secKilBean, "home_banner");
        });
    }*/


    //获取限时抢购
    private void factorySale() {
        mViewModel.getFactorySale().observe(getViewLifecycleOwner(), factorySaleBean -> {
            if (factorySaleBean == null) {
            } else {
                showFactorySale(factorySaleBean);
                GoodsCache.saveGoodsBean(factorySaleBean, "home_sale");
            }
        });
    }


    private void loadCategory() {
        mHomeBean = GoodsCache.readGoodsBean(HomeCategoryBean.class, "home_category");
        if (mHomeBean != null && mHomeBean.getList() != null && mFragmentAdapter != null) {
            mFragmentAdapter.refreshData(mHomeBean.getList());
        }
        mViewModel.getHomeCategoryBean().observe(getViewLifecycleOwner(), homeBean -> {
            if (homeBean == null) {
                return;
            }
            mHomeBean = homeBean;
            // 处理正常的逻辑。
            if (mFragmentAdapter != null) {
                mFragmentAdapter.refreshData(mHomeBean.getList());
            }
            GoodsCache.saveGoodsBean(mHomeBean, "home_category");
        });
    }


    private void showFactorySale(FactorySaleBean factorySaleBean) {
        if (factorySaleBean.getList().size() >= 4) {
            Glide.with(this).load(UrlUtils.formatUrlPrefix(factorySaleBean.getList().get(0).getMainPic())).into(mDataBinding.homeSaleLift01);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(factorySaleBean.getList().get(1).getMainPic())).into(mDataBinding.homeSaleLift02);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(factorySaleBean.getList().get(2).getMainPic())).into(mDataBinding.homeSaleLift03);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(factorySaleBean.getList().get(3).getMainPic())).into(mDataBinding.homeSaleLift04);
            mDataBinding.homeSalePrice01.setText(factorySaleBean.getList().get(0).getActualPrice()+"");
            mDataBinding.homeSalePrice02.setText(factorySaleBean.getList().get(1).getActualPrice()+"");
            mDataBinding.homeSalePrice03.setText(factorySaleBean.getList().get(2).getActualPrice()+"");
            mDataBinding.homeSalePrice04.setText(factorySaleBean.getList().get(3).getActualPrice()+"");
        }
    }


    @SuppressLint("SetTextI18n")
    private void showSecKilBean(SecKilBean secKilBean) {
        if (secKilBean == null || secKilBean.getGoodsList() == null || secKilBean.getRoundsList().size() <= 0
                || secKilBean.getGoodsList().size() < 2) {
            mDataBinding.homeBannerLl.setVisibility(View.GONE);
            return;
        }

        mDataBinding.homeBannerLl.setVisibility(View.VISIBLE);

        SecKilBean.goodsInfo goodsInfo = secKilBean.getGoodsList().get(0);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv1);
        mDataBinding.homeSeckilTv1.setText("直降" + goodsInfo.getDiscounts());
        mDataBinding.homeSeckilPriceTv1.setText("￥" + goodsInfo.getActualPrice());
        goodsInfo = secKilBean.getGoodsList().get(1);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv2);
        mDataBinding.homeSeckilTv2.setText("直降" + goodsInfo.getDiscounts());
        mDataBinding.homeSeckilPriceTv2.setText("￥" + goodsInfo.getActualPrice());
        if (secKilBean.getGoodsList().size() > 2) {
            goodsInfo = secKilBean.getGoodsList().get(2);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv3);
            mDataBinding.homeSeckilTv3.setText("直降" + goodsInfo.getDiscounts());
            mDataBinding.homeSeckilPriceTv3.setText("￥" + goodsInfo.getActualPrice());
        }
        if (secKilBean.getGoodsList().size() > 3) {
            goodsInfo = secKilBean.getGoodsList().get(3);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv4);
            mDataBinding.homeSeckilTv4.setText("直降" + goodsInfo.getDiscounts());
            mDataBinding.homeSeckilPriceTv4.setText("￥" + goodsInfo.getActualPrice());
        }
    }

    @SuppressLint("SetTextI18n")
    private void showRankListBean(RealTimeBean realTimeBean) {
        if (realTimeBean == null || realTimeBean.getList() == null || realTimeBean.getList().size() <= 0
                || realTimeBean.getList().size() < 2) {
            mDataBinding.homeBannerLl.setVisibility(View.GONE);
            return;
        }

        mDataBinding.homeBannerLl.setVisibility(View.VISIBLE);

        RealTimeBean.goodsInfo goodsInfo = realTimeBean.getList().get(0);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv1);
        mDataBinding.homeSeckilTv1.setText("直降" + goodsInfo.getCouponPrice());
        mDataBinding.homeSeckilPriceTv1.setText("￥" + goodsInfo.getActualPrice());
        goodsInfo = realTimeBean.getList().get(1);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv2);
        mDataBinding.homeSeckilTv2.setText("直降" + goodsInfo.getCouponPrice());
        mDataBinding.homeSeckilPriceTv2.setText("￥" + goodsInfo.getActualPrice());
        if (realTimeBean.getList().size() > 2) {
            goodsInfo = realTimeBean.getList().get(2);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv3);
            mDataBinding.homeSeckilTv3.setText("直降" + goodsInfo.getCouponPrice());
            mDataBinding.homeSeckilPriceTv3.setText("￥" + goodsInfo.getActualPrice());
        }
        if (realTimeBean.getList().size() > 3) {
            goodsInfo = realTimeBean.getList().get(3);
            Glide.with(this).load(UrlUtils.formatUrlPrefix(goodsInfo.getMainPic())).into(mDataBinding.homeSeckilRiv4);
            mDataBinding.homeSeckilTv4.setText("直降" + goodsInfo.getCouponPrice());
            mDataBinding.homeSeckilPriceTv4.setText("￥" + goodsInfo.getActualPrice());
        }
    }

    private void loadUserList() {
        mViewModel.getUserList().observe(getViewLifecycleOwner(), userBean -> {
            if (userBean == null || userBean.getList() == null || userBean.getList().size() <= 0) {
                mDataBinding.homeRandomUsersFl.setVisibility(View.GONE);
                return;
            }
            mDataBinding.homeRandomUsersFl.setVisibility(View.VISIBLE);
            UserBean.UserInfo userInfo = userBean.getList().get(0);
            if (userInfo != null) {
                Glide.with(this).load(UrlUtils.formatUrlPrefix(userInfo.getAvatar())).into(mDataBinding.homeBannerHeaderRiv1);
            }
            userInfo = userBean.getList().get(1);
            if (userInfo != null) {
                Glide.with(this).load(UrlUtils.formatUrlPrefix(userInfo.getAvatar())).into(mDataBinding.homeBannerHeaderRiv2);
            }
            userInfo = userBean.getList().get(2);
            if (userInfo != null) {
                Glide.with(this).load(UrlUtils.formatUrlPrefix(userInfo.getAvatar())).into(mDataBinding.homeBannerHeaderRiv3);
            }
        });
    }

    private void showTopIcons(TopIconsBean topIconsBean) {
        if (!topIconsBean.getFlag()) {
            mDataBinding.homeTopIconLl.setVisibility(View.GONE);
            return;
        }
        if (topIconsBean.getItems().size() < 4) {
            return;
        }

        TopIconsBean.Icon icon = topIconsBean.getItems().get(0);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(icon.getIcon())).into(mDataBinding.homeTopIconIv1);
        mDataBinding.homeTopIconTv1.setText(icon.getName());
        mDataBinding.homeTitleTb.setTag(icon);
        icon = topIconsBean.getItems().get(1);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(icon.getIcon())).into(mDataBinding.homeTopIconIv2);
        mDataBinding.homeTopIconTv2.setText(icon.getName());
        mDataBinding.homeTitlePdd.setTag(icon);
        icon = topIconsBean.getItems().get(2);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(icon.getIcon())).into(mDataBinding.homeTopIconIv3);
        mDataBinding.homeTopIconTv3.setText(icon.getName());
        mDataBinding.homeTitleJd.setTag(icon);
        icon = topIconsBean.getItems().get(3);
        Glide.with(this).load(UrlUtils.formatUrlPrefix(icon.getIcon())).into(mDataBinding.homeTopIconIv4);
        mDataBinding.homeTopIconTv4.setText(icon.getName());
        mDataBinding.homeTitleElm.setTag(icon);

        mDataBinding.homeTitleTb.setOnClickListener(v -> gotoOther(v.getTag()));
        mDataBinding.homeTitlePdd.setOnClickListener(v -> gotoOther(v.getTag()));
        mDataBinding.homeTitleJd.setOnClickListener(v -> gotoOther(v.getTag()));
        mDataBinding.homeTitleElm.setOnClickListener(v -> gotoOther(v.getTag()));
    }

    private void gotoOther(Object tag) {
        TopIconsBean.Icon icon = (TopIconsBean.Icon) tag;
        if (icon == null) {
            return;
        }
        if (icon.getInner() == 1) {
            ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                    .withString("from", icon.getType())
                    .navigation();
        } else {
            GotoUtil.gotoElem(mContext, icon.getUrl());
        }
    }

    private void startTimer() {
        if (mLoadUserListTask == null) {
            mLoadUserListTask = new TimerTask() {
                @Override
                public void run() {
                    if (mIsResumed) {
                        HomeFragment.this.requireActivity().runOnUiThread(() -> loadUserList());
                    }
                }
            };
        }
        if (mLoadSecKilTask == null) {
            mLoadSecKilTask = new TimerTask() {
                @Override
                public void run() {
                    if (mIsResumed) {
                        HomeFragment.this.requireActivity().runOnUiThread(() -> loadRankListData());
                    }
                }
            };
        }
        if (mLoadUserAndLoadSecKilTimer == null) {
            mLoadUserAndLoadSecKilTimer = new Timer();
            mLoadUserAndLoadSecKilTimer.schedule(mLoadUserListTask, 5 * 1000, 5 * 1000);
            mLoadUserAndLoadSecKilTimer.schedule(mLoadSecKilTask, 5 * 1000, 15 * 1000);
        }
    }

    private void stopTimer() {
        if (mLoadUserAndLoadSecKilTimer != null) {
            mLoadUserAndLoadSecKilTimer.cancel();
            mLoadUserAndLoadSecKilTimer = null;
        }
        if (mLoadUserListTask != null) {
            mLoadUserListTask.cancel();
            mLoadUserListTask = null;
        }
        if (mLoadSecKilTask != null) {
            mLoadSecKilTask.cancel();
            mLoadSecKilTask = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
        mIsResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        mIsResumed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFragmentAdapter != null) {
            mFragmentAdapter.clear();
            mFragmentAdapter = null;
        }

        mHomeBean = null;
    }

}

