package com.donews.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.adapter.CrazyListAdapter;
import com.donews.home.bean.RealTimeBean;
import com.donews.home.databinding.HomeCrazyListActivityBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.CrazyViewModel;

@Route(path = RouterActivityPath.CrazyList.CRAZY_LIST_DETAIL)
public class HomeCrazyListActivity extends MvvmBaseLiveDataActivity<HomeCrazyListActivityBinding, CrazyViewModel> implements GoodsDetailListener {

    private CrazyListAdapter mCrazyListAdapter;
    private int mPageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_crazy_list_activity;
    }

    @Override
    public void initView() {
        mDataBinding.homeCrazyLoadingStatusTv.setVisibility(View.VISIBLE);
        mDataBinding.homeCrazyGoodsRv.setVisibility(View.GONE);
        mCrazyListAdapter = new CrazyListAdapter(this, this);
        mDataBinding.homeCrazyGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.homeCrazyGoodsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 10;
            }
        });
        mDataBinding.homeCrazyGoodsRv.setAdapter(mCrazyListAdapter);

        loadRefreshList();

        mDataBinding.homeCrazySrl.setOnRefreshListener(refreshLayout -> loadRefreshList());
        mDataBinding.homeCrazySrl.setOnLoadMoreListener(refreshLayout -> loadMoreList());

        mDataBinding.homeCrazyBack.setOnClickListener(v -> finish());
    }

    private void loadRefreshList() {
        mViewModel.getCrazyListData(1).observe(this, realTimeBean -> {
            if (realTimeBean == null || realTimeBean.getList() == null || realTimeBean.getList().size() <= 0) {
                mDataBinding.homeCrazyLoadingStatusTv.setText("数据加载失败，点击重试.");
                mDataBinding.homeCrazySrl.finishRefresh();
                return;
            }
            showCrazyData(realTimeBean, false);
        });
    }

    private void loadMoreList() {
        mPageId++;
        mViewModel.getCrazyListData(mPageId).observe(this, realTimeBean -> {
            if (realTimeBean == null || realTimeBean.getList() == null || realTimeBean.getList().size() <= 0) {
                mPageId--;
                mDataBinding.homeCrazyLoadingStatusTv.setText("数据加载失败，点击重试.");
                mDataBinding.homeCrazySrl.finishRefresh();
                return;
            }
            showCrazyData(realTimeBean, true);
        });
    }

    private void showCrazyData(RealTimeBean realTimeBean, boolean isAdd) {
        mCrazyListAdapter.refreshData(realTimeBean.getList(), isAdd);

        mDataBinding.homeCrazyGoodsRv.setVisibility(View.VISIBLE);
        mDataBinding.homeCrazyLoadingStatusTv.setVisibility(View.GONE);
        mDataBinding.homeCrazySrl.finishRefresh();
        mDataBinding.homeCrazySrl.finishLoadMore();
    }

    @Override
    public void onClick(String id, String goodsId) {
        ARouter.getInstance().build(RouterActivityPath.GoodsDetail.GOODS_DETAIL)
                .withString("params_id", id)
                .withString("params_goods_id", goodsId)
                .navigation();
    }
}