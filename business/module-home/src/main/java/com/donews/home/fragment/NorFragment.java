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
import com.donews.home.adapter.NorGoodsAdapter;
import com.donews.home.databinding.HomeFragmentNorBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.NorViewModel;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.cache.GoodsCache;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

public class NorFragment extends MvvmLazyLiveDataFragment<HomeFragmentNorBinding, NorViewModel> implements GoodsDetailListener {

    private final HomeCategoryBean.CategoryItem mCategoryItem;
    private NorGoodsAdapter mNorGoodsAdapter;

    private int mPageId = 0;
    private RecyclerView.ItemDecoration mItemDecoration;

    public NorFragment(HomeCategoryBean.CategoryItem categoryItem) {
        mCategoryItem = categoryItem;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_nor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPageId = 0;
        mDataBinding.homeNorSrl.setVisibility(View.GONE);
        mDataBinding.homeNorLoadingLl.setVisibility(View.VISIBLE);

        mNorGoodsAdapter = new NorGoodsAdapter(this.getContext(), this);

        int nItemDecorationCount = mDataBinding.homeNorGoodsRv.getItemDecorationCount();
        for (int i = 0; i < nItemDecorationCount; i++) {
            mDataBinding.homeNorGoodsRv.removeItemDecorationAt(i);
        }
        mItemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 32;
            }
        };
        mDataBinding.homeNorGoodsRv.addItemDecoration(mItemDecoration);

        HomeGoodsBean tmpHomeGoodsBean = GoodsCache.readGoodsBean(HomeGoodsBean.class, mCategoryItem.getCid());
        showNorGoodsBean(tmpHomeGoodsBean);

        loadMoreData();

        mDataBinding.homeNorGoodsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.homeNorGoodsRv.setAdapter(mNorGoodsAdapter);

        initSrl();
    }

    private void initSrl() {
        mDataBinding.homeNorSrl.setEnableRefresh(false);
        mDataBinding.homeNorSrl.setEnableAutoLoadMore(false);
        mDataBinding.homeNorSrl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        mPageId++;
        mViewModel.getNorGoodsData(mCategoryItem.getCid(), mPageId).observe(getViewLifecycleOwner(), this::showNorGoodsBean);
    }

    private void showNorGoodsBean(HomeGoodsBean homeGoodsBean) {
        if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
            mDataBinding.homeNorSrl.finishLoadMoreWithNoMoreData();
            mPageId--;
            return;
        }

        mDataBinding.homeNorSrl.finishLoadMore();

        mNorGoodsAdapter.refreshData(homeGoodsBean.getList(), mPageId == 1);

        GoodsCache.saveGoodsBean(homeGoodsBean, mCategoryItem.getCid());

        mDataBinding.homeNorLoadingLl.setVisibility(View.GONE);
        mDataBinding.homeNorSrl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mItemDecoration = null;
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
