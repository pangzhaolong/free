package com.donews.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.donews.common.ad.business.monitor.PageMonitor;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.adapter.FragmentAdapter;
import com.donews.home.databinding.HomeFragmentBinding;
import com.donews.home.viewModel.HomeViewModel;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.UserBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.views.TabItem;
import com.donews.utilslibrary.utils.UrlUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PageMonitor().attach(this);
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
            if (position == 0) {
                if (tab1.getCustomView() == null) {
                    tab1.setCustomView(new TabItem(mContext));
                }
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                tabItem.setTitle("精品");
            } else {
                if (tab1.getCustomView() == null) {
                    tab1.setCustomView(new TabItem(mContext));
                }
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                if (mHomeBean == null || mHomeBean.getList() == null) {
                    return;
                }
                tabItem.setTitle(mHomeBean.getList().get(position - 1).getCname());
            }
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
            startActivity(intent);
        });
        mDataBinding.homeJddHelp.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), HomeHelpActivity.class);
            startActivity(intent);
        });

        mDataBinding.homeBannerLl.setOnClickListener(v ->
                ARouter.getInstance().build(RouterActivityPath.Home.CRAZY_LIST_DETAIL).navigation());

        mDataBinding.homeTitleTb.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                .withString("from", "tb")
                .navigation());
        mDataBinding.homeTitlePdd.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                .withString("from", "pdd")
                .navigation());
        mDataBinding.homeTitleJd.setOnClickListener(v -> ARouter.getInstance().build(RouterActivityPath.Home.Welfare_Activity)
                .withString("from", "jd")
                .navigation());
        mDataBinding.homeTitleElm.setOnClickListener(v -> {

        });

        initSrl();

        loadCategory();
        loadUserList();
        loadSecKilData();
    }

    private void initSrl() {
        mDataBinding.homeSrl.setOnRefreshListener(refreshLayout -> {
            loadCategory();
            loadSecKilData();
            loadUserList();
            mDataBinding.homeSrl.finishRefresh();
        });
    }

    private void loadSecKilData() {
        SecKilBean tmpSecKilBean = GoodsCache.readGoodsBean(SecKilBean.class, "home_banner");
        showSecKilBean(tmpSecKilBean);

        mViewModel.getSecKilData().observe(getViewLifecycleOwner(), secKilBean -> {
            showSecKilBean(secKilBean);
            GoodsCache.saveGoodsBean(secKilBean, "home_banner");
        });
    }

    private void loadCategory() {
        mHomeBean = GoodsCache.readGoodsBean(HomeCategoryBean.class, "home");
        if (mHomeBean != null && mHomeBean.getList() != null && mFragmentAdapter != null) {
            mFragmentAdapter.refreshData(mHomeBean.getList());
        }
        mViewModel.getNetHomeData().observe(getViewLifecycleOwner(), homeBean -> {
            if (homeBean == null) {
                return;
            }
            mHomeBean = homeBean;
            // 处理正常的逻辑。
            if (mFragmentAdapter != null) {
                mFragmentAdapter.refreshData(mHomeBean.getList());
            }
            GoodsCache.saveGoodsBean(mHomeBean, "home");
        });
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


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

