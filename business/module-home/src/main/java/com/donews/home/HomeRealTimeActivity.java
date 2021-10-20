package com.donews.home;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.databinding.HomeRealTimeActivityBinding;
import com.donews.home.utils.IndicatorLineUtil;
import com.donews.home.viewModel.RealTimeViewModel;
import com.google.android.material.tabs.TabLayout;

@Route(path = RouterActivityPath.RealTime.REALTIME_DETAIL)
public class HomeRealTimeActivity extends MvvmBaseLiveDataActivity<HomeRealTimeActivityBinding, RealTimeViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_real_time_activity;
    }

    @Override
    public void initView() {
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.addTab(mDataBinding.homeRealtimeTl.newTab());
        mDataBinding.homeRealtimeTl.getTabAt(0).setText("实时榜");
        mDataBinding.homeRealtimeTl.getTabAt(1).setText("全天榜");
        mDataBinding.homeRealtimeTl.getTabAt(2).setText("热推榜");
        mDataBinding.homeRealtimeTl.getTabAt(3).setText("复购榜");
        mDataBinding.homeRealtimeTl.getTabAt(4).setText("热词飙升榜");
        mDataBinding.homeRealtimeTl.getTabAt(5).setText("热词排行榜");
        mDataBinding.homeRealtimeTl.getTabAt(6).setText("综合热搜榜");

        mDataBinding.homeRealtimeTl.setTabMode(TabLayout.MODE_SCROLLABLE);

        mDataBinding.homeRealtimeTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}