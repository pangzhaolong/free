package com.donews.home.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.SearchFindAdapter;
import com.donews.home.adapter.SearchHistoryAdapter;
import com.donews.home.databinding.HomeFragmentSearchTbBinding;
import com.donews.home.viewModel.TbViewModel;

import java.util.ArrayList;
import java.util.List;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentSearchTbBinding, TbViewModel> {


    private final List<String> mSearchHistoryList = new ArrayList<>();
    private final List<String> mSearchFindList = new ArrayList<>();

    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchFindAdapter mSearchFindAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_search_tb;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchFindList.add("测试");
        mSearchFindList.add("测试测试");
        mSearchFindList.add("测试测试测试");
        mSearchFindList.add("测试测试测试测试");
        mSearchFindList.add("测试测试测试测试测试");
        mSearchFindList.add("测试");
        mSearchFindList.add("测试测试");
        mSearchFindList.add("测试测试测试");
        mSearchFindList.add("测试测试测试测试");
        mSearchFindList.add("测试测试测试测试测试");

        mSearchFindAdapter = new SearchFindAdapter(this.getContext());
        GridLayoutManager manager = new GridLayoutManager(this.getContext(), 40);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String strText = mSearchFindList.get(position);
                int strLen = strText.getBytes().length;
                if (strLen >= 40) {
                    return 40;
                }

                return strText.getBytes().length;
            }
        });

        mDataBinding.homeSearchFindRv.setLayoutManager(manager);
        mDataBinding.homeSearchFindRv.setAdapter(mSearchFindAdapter);
        mSearchFindAdapter.refreshData(mSearchFindList);

        if (mSearchHistoryList.size() <= 0) {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
        } else {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
        }

        mSearchHistoryList.add("哈哈");
        mSearchHistoryList.add("测试");
        mSearchHistoryList.add("测试测试");
        mSearchHistoryList.add("测试");
        mSearchHistoryList.add("测试测试");
        mSearchHistoryList.add("测试");
        if (mSearchHistoryList.size() <= 0) {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
        } else {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
        }

        mSearchHistoryAdapter = new SearchHistoryAdapter(this.getContext());
        manager = new GridLayoutManager(this.getContext(), 40);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String strText = mSearchHistoryList.get(position);
                int strLen = strText.getBytes().length;
                if (strLen >= 40) {
                    return 40;
                }

                return strText.getBytes().length;
            }
        });

        mDataBinding.homeSearchHistoryRv.setLayoutManager(manager);
        mDataBinding.homeSearchHistoryRv.setAdapter(mSearchHistoryAdapter);
        mSearchHistoryAdapter.refreshData(mSearchHistoryList);

        mDataBinding.homeSearchHistoryCleanTv.setOnClickListener(v -> {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
            mSearchHistoryList.clear();
            mSearchHistoryAdapter.refreshData(mSearchHistoryList);
        });
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
