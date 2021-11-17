package com.donews.home.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.SearchSugTbAdapter;
import com.donews.home.databinding.HomeFragmentSearchTbBinding;
import com.donews.home.listener.GoodsClickListener;
import com.donews.home.viewModel.TbViewModel;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.SearchHistory;
import com.donews.middle.bean.home.TmpSearchHistory;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.go.GotoUtil;

public class TbFragment extends MvvmLazyLiveDataFragment<HomeFragmentSearchTbBinding, TbViewModel> implements GoodsClickListener {

    private SearchSugTbAdapter mSearchSugTbAdapter;

    private int mPosition = 0;
    private FragmentStatus mFragmentStatus = new FragmentStatus();

    @Override
    public void onClick(String goodsId, String materialId, String searchId, int src) {
        GotoUtil.requestPrivilegeLinkBean(this.getContext(), goodsId, materialId, searchId, src);
    }

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

        HomeGoodsBean homeGoodsBean = GoodsCache.readGoodsBean(HomeGoodsBean.class, SearchHistory.Ins().getCurKeyWord() + mPosition);
        showSearchTbBean(homeGoodsBean);

        loadSearchData();
    }

    private void loadSearchData() {
        mFragmentStatus.firstLoaded = true;
        mFragmentStatus.pageId += 1;
        mViewModel.getSearchResultData(SearchHistory.Ins().getCurKeyWord(), mFragmentStatus.pageId, mPosition + 1)
                .observe(getViewLifecycleOwner(), this::showSearchTbBean);
    }

    private void showSearchTbBean(HomeGoodsBean homeGoodsBean) {
        mDataBinding.homeSearchTbSrl.finishLoadMore();
        if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
            mFragmentStatus.pageId -= 1;
            return;
        }

        mDataBinding.homeSearchTbSrl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchLoadingLl.setVisibility(View.GONE);

        mSearchSugTbAdapter.refreshData(homeGoodsBean.getList(), mFragmentStatus.pageId == 1);
        if (mFragmentStatus.pageId == 1) {
            GoodsCache.saveGoodsBean(homeGoodsBean, SearchHistory.Ins().getCurKeyWord() + mPosition);
        }
    }

    public void showHistorySearchData(String keyWord) {
        mDataBinding.homeSearchLoadingLl.setVisibility(View.VISIBLE);
        mDataBinding.homeSearchTbSrl.setVisibility(View.GONE);

        HomeGoodsBean homeGoodsBean = GoodsCache.readGoodsBean(HomeGoodsBean.class, keyWord + mPosition);
        showSearchTbBean(homeGoodsBean);
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
}
