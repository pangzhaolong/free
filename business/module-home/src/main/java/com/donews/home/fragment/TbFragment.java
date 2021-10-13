package com.donews.home.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.GridAdapter;
import com.donews.home.adapter.TopBannerViewAdapter;
import com.donews.home.adapter.TopGoodsAdapter;
import com.donews.home.databinding.HomeFragmentTopBinding;
import com.donews.home.viewModel.TopViewModel;
import com.donews.utilslibrary.utils.LogUtil;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentTopBinding, TopViewModel> {


    private GridAdapter mGridAdapter;
    private TopGoodsAdapter mTopGoodsAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_search_tb;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        mDataBinding.homeTopBannerViewPager
                .setLifecycleRegistry(getLifecycle())
                .setAdapter(new TopBannerViewAdapter(this.getContext())).create();

        mDataBinding.homeTopBannerViewPager.setCanLoop(true);
        LogUtil.e("TopFragment onViewCreated");
        mViewModel.getTopBannerData().observe(getViewLifecycleOwner(), dataBean -> {
            // 获取数据
            if (dataBean == null) {
                // 处理接口出错的问题
                return;
            }
            mDataBinding.homeTopBannerViewPager.refreshData(dataBean.getBannners());
            mGridAdapter.refreshData(dataBean.getSpecial_category());
            // 处理正常的逻辑。
        });

        mViewModel.getTopGoodsData().observe(getViewLifecycleOwner(), topGoodsBean -> {
            if (topGoodsBean == null) {
                return;
            }

            mTopGoodsAdapter.refreshData(topGoodsBean.getList());
        });

        mGridAdapter = new GridAdapter(this.getContext());
        mDataBinding.homeColumnGv.setAdapter(mGridAdapter);

        mTopGoodsAdapter = new TopGoodsAdapter(this.getContext());
        mDataBinding.homeGoodProductRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeGoodProductRv.setAdapter(mTopGoodsAdapter);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
