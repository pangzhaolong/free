package com.donews.home.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.SearchSugTbAdapter;
import com.donews.home.databinding.HomeFragmentSearchTbBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.TbViewModel;
import com.donews.middle.bean.home.SearchGoodsBeanV2;
import com.donews.middle.bean.home.SearchHistory;
import com.donews.middle.bean.home.TmpSearchHistory;
import com.donews.middle.cache.GoodsCache;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentSearchTbBinding, TbViewModel> implements GoodsDetailListener {

    private SearchSugTbAdapter mSearchSugTbAdapter;

    private int mPosition = 0;
    private FragmentStatus mFragmentStatus = new FragmentStatus();

    private static class FragmentStatus {
        public int pageId = 1;
        public boolean firstLoaded = false;
    }

    public TbFragment(int position) {
        mPosition = position;
    }

    public void search(String keyWord) {
        if (keyWord == null || keyWord.equalsIgnoreCase("")) {
            return;
        }
        mDataBinding.homeSearchLoadingLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchTbSrl.setVisibility(View.GONE);

        if (!SearchHistory.Ins().getList().contains(keyWord)) {
            SearchHistory.Ins().addHistory(keyWord);
        }

        if (!TmpSearchHistory.Ins().getList().contains(keyWord)) {
            TmpSearchHistory.Ins().getList().add(keyWord);
        }

        mFragmentStatus.pageId = 1;
        SearchHistory.Ins().setCurKeyWord(keyWord);

        SearchGoodsBeanV2 searchResultTbBean = GoodsCache.readGoodsBean(SearchGoodsBeanV2.class, SearchHistory.Ins().getCurKeyWord() + mPosition);
        showSearchTbBean(searchResultTbBean);

        loadSearchData();
    }

    private void loadSearchData() {
        mFragmentStatus.firstLoaded = true;
        mFragmentStatus.pageId += 1;
        mViewModel.getSearchResultData(SearchHistory.Ins().getCurKeyWord(), mFragmentStatus.pageId, mPosition + 1).observe(getViewLifecycleOwner(), this::showSearchTbBean);
    }

    private void showSearchTbBean(SearchGoodsBeanV2 searchResultTbBean) {
        mDataBinding.homeSearchTbSrl.finishLoadMore();
        mDataBinding.homeSearchTbSrl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchLoadingLl.setVisibility(View.GONE);
        if (searchResultTbBean == null || searchResultTbBean.getList() == null || searchResultTbBean.getList().size() <= 0) {

            mFragmentStatus.pageId -= 1;
            return;
        }

        mSearchSugTbAdapter.refreshData(searchResultTbBean.getList(), mFragmentStatus.pageId == 1);
        if (mFragmentStatus.pageId == 1) {
            GoodsCache.saveGoodsBean(searchResultTbBean, SearchHistory.Ins().getCurKeyWord() + mPosition);
        }
    }

    public void showHistorySearchData(String keyWord) {
        mDataBinding.homeSearchLoadingLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchTbSrl.setVisibility(View.GONE);

        SearchGoodsBeanV2 searchGoodsBeanV2 = GoodsCache.readGoodsBean(SearchGoodsBeanV2.class, keyWord + mPosition);
        showSearchTbBean(searchGoodsBeanV2);
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

        mDataBinding.homeSearchTbSrl.setEnableRefresh(false);
        mDataBinding.homeSearchTbSrl.setOnLoadMoreListener(refreshLayout -> loadSearchData());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mFragmentStatus.firstLoaded) {
            search(SearchHistory.Ins().getCurKeyWord());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mFragmentStatus = null;
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
