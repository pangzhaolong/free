package com.donews.unboxing.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterFragmentPath;
import com.donews.unboxing.R;
import com.donews.unboxing.databinding.UnboxingActivityBinding;
import com.donews.unboxing.viewmodel.UnboxingViewModel;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 晒单页面
 */
@Route(path = RouterFragmentPath.Unboxing.PAGER_UNBOXING_ACTIVITY)
public class UnboxingActivity extends
        MvvmBaseLiveDataActivity<UnboxingActivityBinding, UnboxingViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.unboxing_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initView() {
        ARouter.getInstance().inject(this);
        Fragment f = RouterFragmentPath.Unboxing.getUnboxingFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.unboxing_win_frm, f);
        ft.commitAllowingStateLoss();
    }

    private void initData() {
    }
}