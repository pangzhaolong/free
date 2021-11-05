package com.donews.mine.ui;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineWithdrawalRecordAdapter;
import com.donews.mine.databinding.MineActivityWithdrawalRecordBinding;
import com.donews.mine.viewModel.WithdrawalRecordViewModel;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

/**
 * 提现记录
 */
@Route(path = RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL_RECORD)
public class WithdrawalRecordActivity extends
        MvvmBaseLiveDataActivity<MineActivityWithdrawalRecordBinding, WithdrawalRecordViewModel> {

    MineWithdrawalRecordAdapter adapter;
    private boolean isRefesh = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_withdrawal_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        initView();
    }

    public void initView() {
        mViewModel.setDataBinDing(mDataBinding, this);
        mDataBinding.titleBar.setTitle("提现记录");
        adapter = new MineWithdrawalRecordAdapter();
        mDataBinding.mineRefeshIclLayout.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.mineRefeshIclLayout.getRecyclerView().setAdapter(adapter);
        mDataBinding.mineRefeshIclLayout.getRefeshLayout().setOnRefreshListener(refreshLayout -> {
            isRefesh = true;
            mViewModel.loadRecordList(1, adapter.pageSize);
            adapter.getLoadMoreModule().setEnableLoadMore(false);
        });
        adapter.setOnLoadMoreListener((page, pageSize) -> {
            isRefesh = false;
            mViewModel.loadRecordList(page, pageSize);
        });
        mViewModel.recordListLiveData.observe(this, items -> {
            if (isRefesh) {
                if (items == null || items.isEmpty()) {
                    mDataBinding.mineRefeshIclLayout.getStateLayout().showEmpty(
                            R.layout.incl_mul_status_withdrawal_null,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                            ));
                    adapter.setNewData(new ArrayList<>());
                }else{
                    adapter.setNewData(items);
                }
            } else {
                adapter.addData(items);
                adapter.loadMoreFinish(items != null, items != null && items.size() < adapter.pageSize);
            }
            adapter.getLoadMoreModule().setAutoLoadMore(items != null && items.size() >= adapter.pageSize);
            mDataBinding.mineRefeshIclLayout.getRefeshLayout().closeHeaderOrFooter();
        });
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        mDataBinding.mineRefeshIclLayout.getRefeshLayout().autoRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
    }
}