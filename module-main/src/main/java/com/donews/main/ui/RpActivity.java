package com.donews.main.ui;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.donews.main.databinding.MainRpActivityBinding;
import com.donews.utilslibrary.utils.SoundHelp;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Rp.PAGE_RP)
public class RpActivity extends MvvmBaseLiveDataActivity<MainRpActivityBinding, BaseLiveDataViewModel> {

    @Autowired
    int type;
    @Autowired
    float score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);

        mDataBinding.mainRpDlgCashTv.setText(String.format("%.2f", score) + "");
        if (type == 0) {
            mDataBinding.mainRpTypeTv.setText("现金红包");
        }
        mDataBinding.mainRpGet.setOnClickListener(v -> {
            SoundHelp.newInstance().onStart();
            finish();
        });

        SoundHelp.newInstance().init(this);
        SoundHelp.newInstance().onStart();
    }

    @Override
    protected int getLayoutId() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.transparent)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
        return R.layout.main_rp_activity;
    }

    @Override
    public void initView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SoundHelp.newInstance().onRelease();
    }
}