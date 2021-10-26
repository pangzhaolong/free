package com.donews.home.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.TopGoodsAdapter;
import com.donews.home.bean.TopGoodsBean;
import com.donews.home.cache.GoodsCache;
import com.donews.home.databinding.HomeFragmentTopBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.TopViewModel;
import com.donews.utilslibrary.utils.LogUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class TopFragment extends MvvmLazyLiveDataFragment<HomeFragmentTopBinding, TopViewModel> implements GoodsDetailListener {

    private TopGoodsAdapter mTopGoodsAdapter;

    private int mPageId = 1;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_top;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "CheckResult", "DefaultLocale"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTopGoodsAdapter = new TopGoodsAdapter(this.getContext(), this);
        LogUtil.e("TopFragment onViewCreated");

        TopGoodsBean goodsBean = GoodsCache.readGoodsBean(TopGoodsBean.class, "");
        if (goodsBean != null && goodsBean.getList() != null && goodsBean.getList().size() > 0) {
            LogUtil.e("TopFragment goodsBean in :" + goodsBean);
            mTopGoodsAdapter.refreshData(goodsBean.getList());
        }

        loadMoreData();

        mDataBinding.homeGoodProductRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 16;
                outRect.left = 5;
                outRect.right = 5;
            }
        });
        mDataBinding.homeGoodProductRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeGoodProductRv.setAdapter(mTopGoodsAdapter);

        initSrl();
    }

    private void initSrl() {
        mDataBinding.homeTopSrl.setEnableRefresh(false);
        mDataBinding.homeTopSrl.setEnableAutoLoadMore(false);
        mDataBinding.homeTopSrl.setOnLoadMoreListener(refreshLayout -> {
            loadMoreData();
        });
    }

    private void loadMoreData() {
        mPageId++;
        mViewModel.getTopGoodsData(mPageId).observe(getViewLifecycleOwner(), topGoodsBean -> {
            if (topGoodsBean == null || topGoodsBean.getList() == null || topGoodsBean.getList().size() <= 0) {
                mPageId--;
                mDataBinding.homeTopSrl.finishLoadMoreWithNoMoreData();
                return;
            }
            mDataBinding.homeTopSrl.finishLoadMore();
            mTopGoodsAdapter.refreshData(topGoodsBean.getList());
            GoodsCache.saveGoodsBean(topGoodsBean, "");
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LogUtil.e("TopFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.e("TopFragment onCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.e("TopFragment onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.e("TopFragment onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataBinding.homeTopBannerViewPager.startLoop();
        LogUtil.e("TopFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataBinding.homeTopBannerViewPager.stopLoop();
        LogUtil.e("TopFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtil.e("TopFragment onDestroy");
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}
