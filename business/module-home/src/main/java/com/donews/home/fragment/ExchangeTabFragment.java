package com.donews.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.home.R;
import com.donews.home.adapter.ExchangeFragmentTabGoodsAdapter;
import com.donews.home.databinding.HomeFragmentExchangeTabBinding;
import com.donews.home.viewModel.NorViewModel;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.decoration.GridSpacingItemDecoration;

public class ExchangeTabFragment extends MvvmLazyLiveDataFragment<HomeFragmentExchangeTabBinding, NorViewModel>
        implements ExchangeFragmentTabGoodsAdapter.GoodsClickListener {

    private HomeCategoryBean.CategoryItem mCategoryItem;
    private ExchangeFragmentTabGoodsAdapter mNorGoodsAdapter;

    private int mPageId = 0;
    private RecyclerView.ItemDecoration mItemDecoration;

    public ExchangeTabFragment(HomeCategoryBean.CategoryItem categoryItem) {
        mCategoryItem = categoryItem;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_exchange_tab;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPageId = 0;
        mDataBinding.homeNorSrl.setVisibility(View.GONE);
        mDataBinding.homeNorLoadingLl.setVisibility(View.VISIBLE);

        mNorGoodsAdapter = new ExchangeFragmentTabGoodsAdapter(this.getContext(), this);

        int nItemDecorationCount = mDataBinding.homeNorGoodsRv.getItemDecorationCount();
        for (int i = 0; i < nItemDecorationCount; i++) {
            mDataBinding.homeNorGoodsRv.removeItemDecorationAt(i);
        }
        mItemDecoration = new GridSpacingItemDecoration(2);
        mDataBinding.homeNorGoodsRv.addItemDecoration(mItemDecoration);
        mDataBinding.homeNorGoodsRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeNorGoodsRv.setAdapter(mNorGoodsAdapter);

        if (mCategoryItem != null) {
            HomeGoodsBean tmpHomeGoodsBean = GoodsCache.readGoodsBean(HomeGoodsBean.class, mCategoryItem.getCid());
            showNorGoodsBean(tmpHomeGoodsBean);
        }

        loadMoreData();
        initSrl();
    }

    /**
     * 对话按钮的点击
     *
     * @param item 商品信息
     */
    @Override
    public void onExchanageClick(HomeGoodsBean.GoodsInfo item) {
        ToastUtil.showShort(getContext(), "兑换按钮点击了....");
    }

    private void initSrl() {
        mDataBinding.homeNorSrl.setEnableRefresh(false);
        mDataBinding.homeNorSrl.setEnableAutoLoadMore(false);
        mDataBinding.homeNorSrl.setOnLoadMoreListener(refreshLayout -> loadMoreData());
    }

    private void loadMoreData() {
        if (mCategoryItem == null) {
            return;
        }
        mPageId++;
        mViewModel.getNorGoodsData(mCategoryItem.getCid(), mPageId).observe(getViewLifecycleOwner(), homeGoodsBean -> {
            mDataBinding.homeNorSrl.finishLoadMore();

            if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                mPageId--;
                return;
            }
            showNorGoodsBean(homeGoodsBean);
        });
    }

    private void showNorGoodsBean(HomeGoodsBean homeGoodsBean) {
        if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
            return;
        }

        mNorGoodsAdapter.refreshData(homeGoodsBean.getList(), mPageId == 1);

        if (mCategoryItem != null) {
            GoodsCache.saveGoodsBean(homeGoodsBean, mCategoryItem.getCid());
        }
        mDataBinding.homeNorLoadingLl.setVisibility(View.GONE);
        mDataBinding.homeNorSrl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mItemDecoration = null;
    }
}
