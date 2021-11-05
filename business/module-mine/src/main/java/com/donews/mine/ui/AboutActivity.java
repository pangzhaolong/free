package com.donews.mine.ui;

import static com.donews.common.router.RouterActivityPath.Mine.PAGER_MINE_ABOUT_ACTIVITY;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.AppUtils;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityAboutBinding;
import com.gyf.immersionbar.ImmersionBar;

/**
 * <p>
 * 关于我们得页面
 * <p>
 */
@Route(path = PAGER_MINE_ABOUT_ACTIVITY)
public class AboutActivity extends MvvmBaseLiveDataActivity<MineActivityAboutBinding, BaseLiveDataViewModel> {

    @Override
    protected int getLayoutId() {
        ScreenAutoAdapter.match(this, 375.0f);
        ImmersionBar.with(this)
                .statusBarColor(R.color.mine_f6f9fb)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        return R.layout.mine_activity_about;
    }

    @Override
    public void initView() {
        mDataBinding.mainAboutBack.setOnClickListener((v)->{
            finish();
        });
        mDataBinding.mainAboutVersion.setText(AppUtils.getAppVersionName());
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
