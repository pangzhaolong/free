package com.donews.home.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.SearchFindAdapter;
import com.donews.home.adapter.SearchHistoryAdapter;
import com.donews.home.adapter.SearchSugTbAdapter;
import com.donews.home.databinding.HomeFragmentSearchTbBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.listener.SearchHistoryListener;
import com.donews.home.viewModel.TbViewModel;
import com.donews.middle.bean.home.SearchHistory;
import com.donews.middle.bean.home.SearchResultTbBean;
import com.donews.middle.bean.home.TmpSearchHistory;
import com.donews.middle.cache.GoodsCache;

import java.util.ArrayList;
import java.util.List;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentSearchTbBinding, TbViewModel> implements GoodsDetailListener {

    private SearchSugTbAdapter mSearchSugTbAdapter;

    private String mCurKeyWord = "";

    public void search(String keyWord) {

        mDataBinding.homeSearchLoadingLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchTbTipsLl.setVisibility(View.GONE);
        mDataBinding.homeSearchTbGoodsRv.setVisibility(View.GONE);

        if (!SearchHistory.Ins().getList().contains(keyWord)) {
            SearchHistory.Ins().addHistory(keyWord);
        }

        if (!TmpSearchHistory.Ins().getList().contains(keyWord)) {
            TmpSearchHistory.Ins().getList().add(keyWord);
        }

        mCurKeyWord = keyWord;

        SearchResultTbBean searchResultTbBean = GoodsCache.readGoodsBean(SearchResultTbBean.class, mCurKeyWord);
        showSearchTbBean(searchResultTbBean, true);

        mViewModel.getSearchResultData(keyWord).observe(getViewLifecycleOwner(), resultTbBean -> {
            showSearchTbBean(resultTbBean, true);
        });
    }

    private void showSearchTbBean(SearchResultTbBean searchResultTbBean, boolean needSave) {
        if (searchResultTbBean == null || searchResultTbBean.getList().size() <= 0) {
            mDataBinding.homeSearchTbTipsLl.setVisibility(View.VISIBLE);
            mDataBinding.homeSearchTbGoodsRv.setVisibility(View.GONE);
            mDataBinding.homeSearchLoadingLl.setVisibility(View.GONE);
            return;
        }

        mSearchSugTbAdapter.refreshData(searchResultTbBean.getList());

        mDataBinding.homeSearchTbGoodsRv.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchLoadingLl.setVisibility(View.GONE);
        mDataBinding.homeSearchTbTipsLl.setVisibility(View.GONE);

        GoodsCache.saveGoodsBean(searchResultTbBean, mCurKeyWord);
    }

    public void showHistorySearchData(String keyWord) {
        mDataBinding.homeSearchLoadingLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchTbTipsLl.setVisibility(View.GONE);
        mDataBinding.homeSearchTbGoodsRv.setVisibility(View.GONE);

        SearchResultTbBean searchResultTbBean = GoodsCache.readGoodsBean(SearchResultTbBean.class, keyWord);
        showSearchTbBean(searchResultTbBean, false);
    }

    public void showDefaultLayout() {
        mDataBinding.homeSearchTbTipsLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchTbGoodsRv.setVisibility(View.GONE);
        mDataBinding.homeSearchLoadingLl.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_search_tb;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchSugTbAdapter = new SearchSugTbAdapter(this.getContext(), this);
        mDataBinding.homeSearchTbGoodsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 10;
            }
        });
        mDataBinding.homeSearchTbGoodsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.homeSearchTbGoodsRv.setAdapter(mSearchSugTbAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
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
