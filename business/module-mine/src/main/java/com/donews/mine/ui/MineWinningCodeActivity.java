package com.donews.mine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.adapters.MineWinningCodeAdapter;
import com.donews.mine.adapters.MineWinningRecordAdapter;
import com.donews.mine.databinding.MineActivityWinningCodeBinding;
import com.donews.mine.databinding.MineActivityWinningRecordBinding;
import com.donews.mine.viewModel.MineWinningCodeViewModel;
import com.donews.mine.viewModel.MineWinningRecordViewModel;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心的开奖码
 */
@Route(path = RouterActivityPath.Mine.PAGER_MINE_WINNING_CODE_ACTIVITY)
public class MineWinningCodeActivity extends
        MvvmBaseLiveDataActivity<MineActivityWinningCodeBinding, MineWinningCodeViewModel> {

    private int headRes = R.layout.mine_activity_winning_code_head;
    private View headView;
    MineWinningCodeAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(false)
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
        adapter = new MineWinningCodeAdapter();
        mDataBinding.mineWinCodeList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.mineWinCodeList.setAdapter(adapter);
        addHead();
    }

    private List<Object> list = new ArrayList<>();

    private void initData() {
    }

    //添加Head
    private void addHead() {
        if (headView == null) {
            //创建视图并添加
            headView = View.inflate(this, headRes, null);
            ViewGroup.LayoutParams lp = headView.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            headView.setLayoutParams(lp);
            adapter.addHeaderView(headView);
        }
    }
}