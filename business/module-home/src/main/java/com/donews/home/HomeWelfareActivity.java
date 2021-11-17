package com.donews.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.home.adapter.CrazyListAdapter;
import com.donews.home.adapter.PerfectGoodsAdapter;
import com.donews.home.databinding.HomeWelfareActivityBinding;
import com.donews.home.listener.GoodsDetailListener;
import com.donews.home.viewModel.WelfareViewModel;
import com.donews.middle.bean.home.PerfectGoodsBean;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Home.Welfare_Activity)
public class HomeWelfareActivity extends MvvmBaseLiveDataActivity<HomeWelfareActivityBinding, WelfareViewModel> implements GoodsDetailListener {

    private PerfectGoodsAdapter mPerfectGoodsAdapter;
    private int mPageId = 1;

    @Autowired
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);

        super.onCreate(savedInstanceState);

        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        if (from.equalsIgnoreCase("tb")) {
            mDataBinding.homeWelfareLogo.setImageResource(R.drawable.home_logo_tb);
        } else if (from.equalsIgnoreCase("pdd")) {
            mDataBinding.homeWelfareLogo.setImageResource(R.drawable.home_logo_pdd);
        } else if (from.equalsIgnoreCase("jd")) {
            mDataBinding.homeWelfareLogo.setImageResource(R.drawable.home_logo_jd);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_welfare_activity;
    }

    @Override
    public void initView() {
        mDataBinding.homeCrazyLoadingStatusTv.setVisibility(View.VISIBLE);
        mDataBinding.homeWelfareGoodsRv.setVisibility(View.GONE);
        mPerfectGoodsAdapter = new PerfectGoodsAdapter(this, this);
        mDataBinding.homeWelfareGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.homeWelfareGoodsRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 16;
            }
        });
        mDataBinding.homeWelfareGoodsRv.setAdapter(mPerfectGoodsAdapter);

        loadRefreshList();

        mDataBinding.homeCrazySrl.setEnableRefresh(false);
        mDataBinding.homeCrazySrl.setOnLoadMoreListener(refreshLayout -> loadMoreList());

        mDataBinding.homeWelfareBack.setOnClickListener(v -> finish());

        mDataBinding.homeWelfareEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase("")) {
                    return;
                }
            }
        });
    }

    private void loadRefreshList() {
        mViewModel.getPerfectGoodsData(from, 1).observe(this, perfectGoodsBean -> {
            if (perfectGoodsBean == null || perfectGoodsBean.getList() == null || perfectGoodsBean.getList().size() <= 0) {
                mDataBinding.homeCrazyLoadingStatusTv.setText("数据加载失败，点击重试.");
                mDataBinding.homeCrazySrl.finishRefresh();
                return;
            }
            showPerfectGoodsData(perfectGoodsBean, false);
        });
    }

    private void loadMoreList() {
        mPageId++;
        mViewModel.getPerfectGoodsData(from, mPageId).observe(this, perfectGoodsBean -> {
            if (perfectGoodsBean == null || perfectGoodsBean.getList() == null || perfectGoodsBean.getList().size() <= 0) {
                mPageId--;
                mDataBinding.homeCrazyLoadingStatusTv.setText("数据加载失败，点击重试.");
                mDataBinding.homeCrazySrl.finishRefresh();
                return;
            }
            showPerfectGoodsData(perfectGoodsBean, true);
        });
    }

    private void showPerfectGoodsData(PerfectGoodsBean perfectGoodsBean, boolean isAdd) {
        mPerfectGoodsAdapter.refreshData(perfectGoodsBean.getList(), isAdd);

        mDataBinding.homeWelfareGoodsRv.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}