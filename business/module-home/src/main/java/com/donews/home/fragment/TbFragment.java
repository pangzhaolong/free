package com.donews.home.fragment;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.SearchFindAdapter;
import com.donews.home.adapter.SearchHistoryAdapter;
import com.donews.home.adapter.SearchSugTbAdapter;
import com.donews.home.databinding.HomeFragmentSearchTbBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.TbViewModel;

import java.util.ArrayList;
import java.util.List;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentSearchTbBinding, TbViewModel> implements GoodsDetailListener {


    private final List<String> mSearchHistoryList = new ArrayList<>();
    private final List<String> mSearchFindList = new ArrayList<>();

    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchFindAdapter mSearchFindAdapter;

    private SearchSugTbAdapter mSearchSugTbAdapter;

    public void search(String keyWord) {
        mDataBinding.homeSearchTbTipsLl.setVisibility(View.GONE);
        mDataBinding.homeSearchTbGoodsRv.setVisibility(View.VISIBLE);

        mViewModel.getSearchResultData(keyWord).observe(getViewLifecycleOwner(), searchResultTbBean -> {
            if (searchResultTbBean == null || searchResultTbBean.getList().size() <= 0) {
                mDataBinding.homeSearchTbTipsLl.setVisibility(View.VISIBLE);
                mDataBinding.homeSearchTbGoodsRv.setVisibility(View.GONE);
                return;
            }

            mSearchSugTbAdapter.refreshData(searchResultTbBean.getList());

            mDataBinding.homeSearchTbTipsLl.setVisibility(View.GONE);
            mDataBinding.homeSearchTbGoodsRv.setVisibility(View.VISIBLE);
        });
    }

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

        mSearchSugTbAdapter = new SearchSugTbAdapter(this.getContext(), this);
        mDataBinding.homeSearchTbGoodsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.homeSearchTbGoodsRv.setAdapter(mSearchSugTbAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
