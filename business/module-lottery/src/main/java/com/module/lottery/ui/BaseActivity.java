package com.module.lottery.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.gyf.immersionbar.ImmersionBar;
import com.module_lottery.R;

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseLiveDataViewModel> extends MvvmBaseLiveDataActivity<V,VM> {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .init();
    }

    /**
     * layoutId
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();


}
