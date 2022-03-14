package com.donews.home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.adapter.CrazyListAdapter;
import com.donews.home.databinding.HomeCrazyListActivityBinding;
import com.donews.home.listener.GoodsClickListener;
import com.donews.home.viewModel.CrazyViewModel;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.middle.bean.home.HomeGoodsBean;
import com.donews.middle.dialog.JumpThirdAppDialog;
import com.donews.middle.go.GotoUtil;
import com.donews.middle.listener.JumpThirdAppListener;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Home.CRAZY_LIST_DETAIL)
public class HomePerfectActivity extends MvvmBaseLiveDataActivity<HomeCrazyListActivityBinding, CrazyViewModel> implements GoodsClickListener {

    private CrazyListAdapter mCrazyListAdapter;
    private int mPageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
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
                outRect.top = 16;
            }
        });
        mDataBinding.homeCrazyGoodsRv.setAdapter(mCrazyListAdapter);

        loadRefreshList();

        mDataBinding.homeCrazySrl.setOnRefreshListener(refreshLayout -> loadRefreshList());
        mDataBinding.homeCrazySrl.setOnLoadMoreListener(refreshLayout -> loadMoreList());

        mDataBinding.homeCrazyBack.setOnClickListener(v -> finish());
    }

    private void loadRefreshList() {
        mViewModel.getCrazyListData(1, "1").observe(this, realTimeBean -> {
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
        mViewModel.getCrazyListData(mPageId, "1").observe(this, homeGoodsBean -> {
            if (homeGoodsBean == null || homeGoodsBean.getList() == null || homeGoodsBean.getList().size() <= 0) {
                mPageId--;
                mDataBinding.homeCrazyLoadingStatusTv.setText("数据加载失败，点击重试.");
                mDataBinding.homeCrazySrl.finishRefresh();
                return;
            }
            showCrazyData(homeGoodsBean, true);
        });
    }

    private void showCrazyData(HomeGoodsBean realTimeBean, boolean isAdd) {
        mCrazyListAdapter.refreshData(realTimeBean.getList(), isAdd);

        mDataBinding.homeCrazyGoodsRv.setVisibility(View.VISIBLE);
        mDataBinding.homeCrazyLoadingStatusTv.setVisibility(View.GONE);
        mDataBinding.homeCrazySrl.finishRefresh();
        mDataBinding.homeCrazySrl.finishLoadMore();
    }

    @Override
    public void onClick(String goodsId, String materialId, String searchId, int src) {
        Context context = this;

        if (!OtherSwitch.Ins().isOpenJumpDlg()) {
            GotoUtil.requestPrivilegeLinkBean(context, goodsId, materialId, searchId, src);
            return;
        }

        new JumpThirdAppDialog(context, src, new JumpThirdAppListener() {
            @Override
            public void onClose() {

            }

            @Override
            public void onGo() {
                GotoUtil.requestPrivilegeLinkBean(context, goodsId, materialId, searchId, src);
            }
        }).show();
//        GotoUtil.requestPrivilegeLinkBean(this, goodsId, materialId, searchId, src);
    }
}