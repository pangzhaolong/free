package com.donews.mine.ui;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivitySettingBinding;
import com.donews.mine.databinding.MineActivityWithdrawalCenterBinding;
import com.donews.mine.viewModel.SettingViewModel;
import com.donews.mine.viewModel.WithdrawalCenterViewModel;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 提现中心
 */
@Route(path = RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL)
public class WithdrawalCenterActivity extends
        MvvmBaseLiveDataActivity<MineActivityWithdrawalCenterBinding, WithdrawalCenterViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_withdrawal_center;
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

    public void initView() {
        mViewModel.setDataBinDing(mDataBinding, this);
        mDataBinding.titleBar.setTitle("提现中心");
        mDataBinding.mineDrawMore.setOnClickListener(v -> {
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL_RECORD)
                    .navigation();
        });
        mViewModel.withdrawDataLivData.observe(this, items -> {
            mViewModel.addGridDatas(mDataBinding.mineDrawGrid);
        });
        mViewModel.withdrawDatilesLivData.observe(this, items -> {
            if (items == null) {
                ToastUtil.showShort(this, "获取数据异常,请稍后重试");
            } else {
                mDataBinding.mineDrawYe.setText("" + items.total);
            }
        });
        mViewModel.getLoadWithdraWalletDite();
        mViewModel.getLoadWithdrawData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
    }
}