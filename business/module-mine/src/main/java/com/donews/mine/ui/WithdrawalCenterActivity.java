package com.donews.mine.ui;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.WalletRefreshEvent;
import com.donews.base.fragmentdialog.LoadingHintDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.business.callback.JddAdIdConfigManager;
import com.donews.common.ad.business.utils.JddAdUnits;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityWithdrawalCenterBinding;
import com.donews.mine.viewModel.WithdrawalCenterViewModel;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

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
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        AnalysisUtils.onEventEx(this, Dot.Page_Cash);
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
            if (!JddAdUnits.INSTANCE.isOpenAd()) {
                ToastUtil.showShort(this, "账户升级中，请稍后再试");
                return;
            }
            mDataBinding.mineDrawSubmit.setEnabled(false);
            showLoading();
            mViewModel.requestWithdraw(mDataBinding.mineDrawGrid);
        });
        mViewModel.withdrawDataLivData.observe(this, items -> {
            hideLoading();
            mViewModel.addGridDatas(mDataBinding.mineDrawGrid,
                    mDataBinding.mineDrawSubmit, mDataBinding.mineDrawDesc);
        });
        mViewModel.withdrawDatilesLivData.observe(this, items -> {
            if (items == null) {
                mDataBinding.mineDrawGridLoading.setVisibility(View.GONE);
                mDataBinding.mineDrawGridLoading.setText("数据加载错误");
                ToastUtil.showShort(this, "获取数据异常,请稍后重试");
            } else {
                mDataBinding.mineDrawGridLoading.setVisibility(View.GONE);
                mDataBinding.mineDrawYe.setText("" + items.total);
            }
        });
        mViewModel.withdrawLivData.observe(this, code -> {
            mViewModel.isWithdrawLoading = false;
            mDataBinding.mineDrawSubmit.setEnabled(true);
            if (code == 0 || code == 22102) {
                if (code == 0) {
                    ToastUtil.showShort(this, "提现成功!");
                }
                showLoading("加载中");
                mViewModel.getLoadWithdraWalletDite();
                mViewModel.getLoadWithdrawData(true); //更新配置信息
                EventBus.getDefault().post(new WalletRefreshEvent(1));
            } else {
                hideLoading();
            }
        });
        mViewModel.getLoadWithdraWalletDite();
        mViewModel.getLoadWithdrawData(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
    }
}