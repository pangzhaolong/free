package com.donews.home.fragment;


import android.content.Context;
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
import com.donews.home.adapter.GridAdapter;
import com.donews.home.adapter.TopBannerViewAdapter;
import com.donews.home.adapter.TopGoodsAdapter;
import com.donews.home.bean.TopBannerBean;
import com.donews.home.databinding.HomeFragmentTopBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.TopViewModel;
import com.donews.utilslibrary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class TopFragment extends MvvmLazyLiveDataFragment<HomeFragmentTopBinding, TopViewModel> implements GoodsDetailListener {


    private GridAdapter mGridAdapter;
    private TopGoodsAdapter mTopGoodsAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_top;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDataBinding.homeTopBannerViewPager
                .setLifecycleRegistry(getLifecycle())
                .setAdapter(new TopBannerViewAdapter(this.getContext())).create();

        mDataBinding.homeTopBannerViewPager.setCanLoop(true);
        LogUtil.e("TopFragment onViewCreated");
        mViewModel.getTopBannerData().observe(getViewLifecycleOwner(), dataBean -> {
            // 获取数据
            if (dataBean == null) {
                // 处理接口出错的问题
                return;
            }
            mDataBinding.homeTopBannerViewPager.refreshData(dataBean.getBannners());
            mGridAdapter.refreshData(dataBean.getSpecial_category());
            // 处理正常的逻辑。
        });

        mViewModel.getTopGoodsData().observe(getViewLifecycleOwner(), topGoodsBean -> {
            if (topGoodsBean == null) {
                return;
            }

            mTopGoodsAdapter.refreshData(topGoodsBean.getList());
        });

        mGridAdapter = new GridAdapter(this.getContext());
        mDataBinding.homeColumnGv.setAdapter(mGridAdapter);

        mTopGoodsAdapter = new TopGoodsAdapter(this.getContext(), this);
        mDataBinding.homeGoodProductRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        mDataBinding.homeGoodProductRv.setAdapter(mTopGoodsAdapter);
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
