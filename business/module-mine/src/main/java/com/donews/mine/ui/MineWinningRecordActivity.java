package com.donews.mine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineParticipateRecordAdapter;
import com.donews.mine.adapters.MineWinningRecordAdapter;
import com.donews.mine.bean.resps.WinRecordResp;
import com.donews.mine.databinding.MineActivityParticipateRecordBinding;
import com.donews.mine.databinding.MineActivityWinningRecordBinding;
import com.donews.mine.viewModel.MineParticipateRecordViewModel;
import com.donews.mine.viewModel.MineWinningRecordViewModel;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖记录
 */
@Route(path = RouterActivityPath.Mine.PAGER_WINNING_RECORD)
public class MineWinningRecordActivity extends
        MvvmBaseLiveDataActivity<MineActivityWinningRecordBinding, MineWinningRecordViewModel> {
    //适配器对象
    private MineWinningRecordAdapter adapter;
    private boolean isRefesh = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    public void initView() {
        adapter = new MineWinningRecordAdapter();
        adapter.setOnLoadMoreListener((page, pageSize) -> {
            if(mViewModel.winRecordLivData.getValue() == null ||
                    mViewModel.winRecordLivData.getValue().size() < adapter.pageSize){
                adapter.loadMoreFinish(true, true);
            }else{
                isRefesh = false;
                mViewModel.loadDataList(page, pageSize);
            }
        });
        mDataBinding.mineWinRecodLayout.setRefeshOnListener(refreshLayout -> {
            isRefesh = true;
            refeshListData();
        });
        mDataBinding.mineWinRecodLayout.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mDataBinding.mineWinRecodLayout.setAdapter(adapter);
        mDataBinding.mineWinRecodBack.setOnClickListener((v) -> {
            finish();
        });
        mDataBinding.mineWinRecodLayout.getStateLayout().setOnRetryClickListener(v -> {
            mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
        });
        mViewModel.winRecordLivData.observe(this, (items) -> {
            List<WinRecordResp.ListDTO> list;
            if (items == null) {
                list = new ArrayList<>();
            } else {
                list = items;
            }
            if (isRefesh) {
                mDataBinding.mineWinRecodLayout.setRefeshComplete();
                adapter.loadMoreFinish(true, items.isEmpty() || items.size() < adapter.pageSize);
                adapter.setNewData(list);
                if (items == null) {
                    mDataBinding.mineWinRecodLayout.getStateLayout().showError();
                    mDataBinding.mineWinRecodLayout.getStateLayout().findViewById(R.id.error_view)
                            .setOnClickListener(v -> {
                                mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
                            });
                } else if (list.isEmpty()) {
                    mDataBinding.mineWinRecodLayout.getStateLayout().showEmpty();
                } else {
                    mDataBinding.mineWinRecodLayout.getStateLayout().showContent();
                }
            } else {
                mDataBinding.mineWinRecodLayout.getStateLayout().showContent();
                adapter.loadMoreFinish(true, items.isEmpty());
                adapter.addData(list);
            }
        });
        mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
    }

    private void initData() {
    }

    //下拉刷新数据
    private void refeshListData() {
        mViewModel.loadDataList(1, adapter.pageSize);
    }
}