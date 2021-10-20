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
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.SearchFindAdapter;
import com.donews.home.adapter.SearchHistoryAdapter;
import com.donews.home.adapter.SearchSugTbAdapter;
import com.donews.home.bean.SearchHistory;
import com.donews.home.bean.SearchResultTbBean;
import com.donews.home.bean.TmpSearchHistory;
import com.donews.home.cache.GoodsCache;
import com.donews.home.databinding.HomeFragmentSearchTbBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.listener.SearchHistoryListener;
import com.donews.home.viewModel.TbViewModel;

import java.util.ArrayList;
import java.util.List;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentSearchTbBinding, TbViewModel> implements GoodsDetailListener, SearchHistoryListener {

    private final List<String> mSearchFindList = new ArrayList<>();

    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchFindAdapter mSearchFindAdapter;

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

        mSearchFindList.add("面膜");
        mSearchFindList.add("洗面奶");
        mSearchFindList.add("洗衣液");
        mSearchFindList.add("口红");
        mSearchFindList.add("螺蛳粉");
        mSearchFindList.add("洗发水");
        mSearchFindList.add("眼影");
        mSearchFindList.add("口罩");
        mSearchFindList.add("坚果");
        mSearchFindList.add("连衣裙");

        mSearchFindAdapter = new SearchFindAdapter(this.getContext(), this);
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

        if (SearchHistory.Ins().getList().size() <= 0) {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
        } else {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.VISIBLE);
        }

        mSearchHistoryAdapter = new SearchHistoryAdapter(this.getContext(), this);
        manager = new GridLayoutManager(this.getContext(), 40);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String strText = SearchHistory.Ins().getList().get(position);
                int strLen = strText.getBytes().length;
                if (strLen >= 40) {
                    return 40;
                }

                return strText.getBytes().length;
            }
        });

        mDataBinding.homeSearchHistoryRv.setLayoutManager(manager);
        mDataBinding.homeSearchHistoryRv.setAdapter(mSearchHistoryAdapter);
        mSearchHistoryAdapter.refreshData(SearchHistory.Ins().getList());

        mDataBinding.homeSearchHistoryCleanTv.setOnClickListener(v -> {
            mDataBinding.homeSearchHistoryTl.setVisibility(View.GONE);
            SearchHistory.Ins().write("");
            SearchHistory.Ins().getList().clear();
            mSearchHistoryAdapter.refreshData(SearchHistory.Ins().getList());
        });

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

        mSearchHistoryAdapter.refreshData(SearchHistory.Ins().getList());
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

    @Override
    public void onClick(String keyWord) {
        search(keyWord);
    }
}
