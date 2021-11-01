package com.donews.mine.ui;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.WalletRefreshEvent;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityWithdrawalCenterBinding;
import com.donews.mine.viewModel.WithdrawalCenterViewModel;
import com.donews.utilslibrary.utils.AppInfo;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

/**
 * 提现中心
 */
@Route(path = RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL)
public class WithdrawalCenterActivity extends
        MvvmBaseLiveDataActivity<MineActivityWithdrawalCenterBinding, WithdrawalCenterViewModel> {

    private LoadingHintDialog loadingHintDialog;

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
    }

    public void initView() {
        mViewModel.setDataBinDing(mDataBinding, this);
        mDataBinding.titleBar.setTitle("提现中心");
        if (!AppInfo.checkIsWXLogin()) {
            ARouter.getInstance()
                    .build(RouterActivityPath.User.PAGER_LOGIN)
                    .navigation();
            finish();
            return; //未登录直接去往登录页面
        }
        UserInfoBean uf = LoginHelp.getInstance().getUserInfoBean();
        mDataBinding.mineDrawWxName.setText(uf.getWechatExtra().getNickName());
        mDataBinding.mineDrawMore.setOnClickListener(v -> {
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL_RECORD)
                    .navigation();
        });
        mDataBinding.mineDrawSubmit.setOnClickListener(v -> {
            loadingHintDialog = new LoadingHintDialog();
            loadingHintDialog.setDismissOnBackPressed(true)
                    .setDescription("提交中...")
                    .show(getSupportFragmentManager(), "withdrawal");
            mViewModel.requestWithdraw(mDataBinding.mineDrawGrid);
        });
        mViewModel.withdrawDataLivData.observe(this, items -> {
            mViewModel.addGridDatas(mDataBinding.mineDrawGrid,
                    mDataBinding.mineDrawSubmit,mDataBinding.mineDrawDesc);
        });
        mViewModel.withdrawDatilesLivData.observe(this, items -> {
            if (items == null) {
                mDataBinding.mineDrawGridLoading.setText("数据加载错误");
                ToastUtil.showShort(this, "获取数据异常,请稍后重试");
            } else {
                mDataBinding.mineDrawGridLoading.setVisibility(View.GONE);
                mDataBinding.mineDrawYe.setText("" + items.total);
            }
        });
        mViewModel.withdrawLivData.observe(this, code -> {
            hideLoad();
            if (code == 0) {
                mViewModel.getLoadWithdrawData(); //更新配置信息
                ToastUtil.showShort(this, "提现成功!");
                EventBus.getDefault().post(new WalletRefreshEvent(1));
            }else if (code == 22104){
                ToastUtil.showShort(this, "提现失败,余额不足");
            }
        });
        mViewModel.getLoadWithdraWalletDite();
        mViewModel.getLoadWithdrawData();
    }

    private void hideLoad(){
        if (loadingHintDialog != null) {
            loadingHintDialog.disMissDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
    }
}