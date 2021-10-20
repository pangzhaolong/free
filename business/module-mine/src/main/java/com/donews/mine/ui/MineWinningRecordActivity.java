package com.donews.mine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineParticipateRecordAdapter;
import com.donews.mine.adapters.MineWinningRecordAdapter;
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

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.mine_f6f9fb)
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
            loadMoreListData();
        });
        mDataBinding.mineWinRecodLayout.setRefeshOnListener(refreshLayout -> {
            refeshListData();
        });
        mDataBinding.mineWinRecodLayout.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mDataBinding.mineWinRecodLayout.setAdapter(adapter);
        mDataBinding.mineWinRecodBack.setOnClickListener((v) -> {
            finish();
        });
        mDataBinding.mineWinRecodLayout.getRefeshLayout().autoRefresh();
    }

    private List<Object> list = new ArrayList<>();

    private void initData() {
    }

    Handler h = new Handler();

    //下拉刷新数据
    private void refeshListData() {
        h.postDelayed(() -> {
            mDataBinding.mineWinRecodLayout.setRefeshComplete();
            list.clear();
            for (int i = 0; i < 50; i++) {
                list.add("" + i);
            }
            adapter.setNewData(list);
        }, 1000);
    }

    //上拉加载更多
    private void loadMoreListData() {
        h.postDelayed(() -> {
            for (int i = 0; i < 10; i++) {
                list.add("" + i);
            }
            adapter.loadMoreFinish(true, false);
            adapter.addData(list);
        }, 1000);
    }
}