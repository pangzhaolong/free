package com.donews.home.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.R;
import com.donews.home.adapter.TopGoodsAdapter;
import com.donews.home.databinding.HomeFragmentTopBinding;
import com.donews.home.listener.GoodsClickListener;
import com.donews.home.viewModel.TopViewModel;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.decoration.GridSpaceItemDecoration;

public class TopFragment extends MvvmLazyLiveDataFragment<HomeFragmentTopBinding, TopViewModel> implements GoodsClickListener {

    private TopGoodsAdapter mTopGoodsAdapter;

    private int mPageId = 0;
    private RecyclerView.ItemDecoration mItemDecoration;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_top;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "CheckResult", "DefaultLocale"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPageId = 0;
        mTopGoodsAdapter = new TopGoodsAdapter(this.getContext(), this);
//        LogUtil.e("TopFragment onViewCreated");

        HomeGoodsBean goodsBean = GoodsCache.readGoodsBean(HomeGoodsBean.class, "");
        if (goodsBean != null && goodsBean.getList() != null && goodsBean.getList().size() > 0) {
//            LogUtil.e("TopFragment goodsBean in :" + goodsBean);
            mTopGoodsAdapter.refreshData(goodsBean.getList(), true);
        }

        loadMoreData();

        int nItemDecorationCount = mDataBinding.homeGoodProductRv.getItemDecorationCount();
        for (int i = 0; i < nItemDecorationCount; i++) {
            mDataBinding.homeGoodProductRv.removeItemDecorationAt(i);
        }
        mItemDecoration = new GridSpaceItemDecoration(2);
        mDataBinding.homeGoodProductRv.addItemDecoration(mItemDecoration);
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
            mTopGoodsAdapter.refreshData(topGoodsBean.getList(), mPageId == 1);
            GoodsCache.saveGoodsBean(topGoodsBean, "");
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        LogUtil.e("TopFragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogUtil.e("TopFragment onCreate");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        LogUtil.e("TopFragment onDestroyView");
        mItemDecoration = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        LogUtil.e("TopFragment onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataBinding.homeTopBannerViewPager.startLoop();
//        LogUtil.e("TopFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataBinding.homeTopBannerViewPager.stopLoop();
//        LogUtil.e("TopFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

/*    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }*/

    @Override
    public void onClick(String goodsId, String materialId, String searchId, int src) {

    }
}
