package com.donews.home.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.FragmentAdapter;
import com.donews.home.adapter.FragmentCategoryAdapter;
import com.donews.home.adapter.NorGoodsAdapter;
import com.donews.home.bean.HomeBean;
import com.donews.home.bean.NorGoodsBean;
import com.donews.home.cache.GoodsCache;
import com.donews.home.databinding.HomeFragmentNorBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.NorViewModel;
import com.donews.utilslibrary.utils.LogUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NorFragment extends MvvmLazyLiveDataFragment<HomeFragmentNorBinding, NorViewModel> implements GoodsDetailListener {

    private final HomeBean.CategoryItem mCategoryItem;
    private NorGoodsAdapter mNorGoodsAdapter;

    public NorFragment(HomeBean.CategoryItem categoryItem) {
        mCategoryItem = categoryItem;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_nor;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding.homeNorShowSrl.setVisibility(View.GONE);
        mDataBinding.homeNorLoadingLl.setVisibility(View.VISIBLE);

        mNorGoodsAdapter = new NorGoodsAdapter(this.getContext(), this);
        mDataBinding.homeNorGoodsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 10;
            }
        });

        NorGoodsBean tmpNorGoodsBean = GoodsCache.readGoodsBean(NorGoodsBean.class, mCategoryItem.getCid());
        showNorGoodsBean(tmpNorGoodsBean);

        mDataBinding.homeNorGoodsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mDataBinding.homeNorGoodsRv.setAdapter(mNorGoodsAdapter);

        mViewModel.getNorGoodsData(mCategoryItem.getCid()).observe(getViewLifecycleOwner(), this::showNorGoodsBean);

        mDataBinding.homeNorShowSrl.setOnRefreshListener(() -> new Handler().postDelayed(() -> mDataBinding.homeNorShowSrl.setRefreshing(false), 1000));
    }

    private void showNorGoodsBean(NorGoodsBean norGoodsBean) {
        if (norGoodsBean == null) {
            return;
        }

        mNorGoodsAdapter.refreshData(norGoodsBean.getList());

        GoodsCache.saveGoodsBean(norGoodsBean, mCategoryItem.getCid());

        mDataBinding.homeNorLoadingLl.setVisibility(View.GONE);
        mDataBinding.homeNorShowSrl.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.e("NorFragment onDestroy");
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
