package com.donews.mine.ui;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivitySettingBinding;
import com.donews.mine.viewModel.SettingViewModel;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Mine.PAGER_ACTIVITY_SETTING)
public class SettingActivity extends MvvmBaseLiveDataActivity<MineActivitySettingBinding, SettingViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_setting;
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
//        mDataBinding.titleBar
//                .setBackImageView(R.drawable.mine_win_write_back);
        initView();
    }

    public void initView() {
        mViewModel.mContext = this;
        mDataBinding.titleBar.setTitle("设置");
        mDataBinding.titleBar.findViewById(R.id.title_bar_root)
                .setBackgroundResource(R.color.white);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
    }
}