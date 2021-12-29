package com.donews.mine.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.WalletRefreshEvent;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.listener.impl.SimpleInterstitialListener;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.ad.business.loader.AdManager;
import com.donews.common.ad.business.manager.JddAdManager;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityWithdrawalCenterBinding;
import com.donews.mine.dialogs.MineCongratulationsDialog;
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

    private ScaleAnimation mScaleAnimation;

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
        mDataBinding.titleBar.getTitleView().setTypeface(Typeface.DEFAULT_BOLD);
        mDataBinding.titleBar.setSubmitButtonText("明细");
        mDataBinding.titleBar.setSubmitOnClick((v)->{
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL_RECORD)
                    .navigation();
        });
        if (!AppInfo.checkIsWXLogin()) {
            ARouter.getInstance()
                    .build(RouterActivityPath.User.PAGER_LOGIN)
                    .navigation();
            finish();
            return; //未登录直接去往登录页面
        }
        UserInfoBean uf = LoginHelp.getInstance().getUserInfoBean();
        mDataBinding.mineDrawWxName.setText(uf.getWechatExtra().getNickName());
        if (uf.getWechatExtra().getHeadimgurl() != null && uf.getWechatExtra().getHeadimgurl().length() > 0) {
            GlideUtils.loadImageView(
                    this, uf.getWechatExtra().getHeadimgurl(), mDataBinding.mineDrawWxIcon);
        }
        mDataBinding.mindYywJd.setOnClickListener(v -> {
            ToastUtil.showShort(this, "运营位点击了。。。");
        });
        mDataBinding.mineDrawMore.setOnClickListener(v -> {
            ARouter.getInstance()
                    .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL_RECORD)
                    .navigation();
        });
        mDataBinding.mineDrawSubmit.setOnClickListener(v -> {
            if (!JddAdManager.INSTANCE.isOpenAd()) {
                ToastUtil.showShort(this, "账户升级中，请稍后再试");
                return;
            }
            if (mViewModel.withdrawSelectDto == null) {
                ToastUtil.showShort(this, "请选择你要提现的金额");
                return;
            }
            if (mViewModel.withdrawSelectDto.external) {
                //检查随机金额
                getTaskList();
            } else {
                if (mViewModel.withdrawDatilesLivData.getValue().total < mViewModel.withdrawSelectDto.money) {
                    //去往首页
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                            .withInt("position", 0)
                            .navigation();
//                    HighValueGoodsBean.GoodsInfo item = mViewModel.getGoodInfo();
//                    if (item != null) {
//                        AnalysisUtils.onEventEx(this,
//                                Dot.But_Goto_Lottery, "提现页面>提现按钮");
//                        ARouter.getInstance()
//                                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
//                                .withString("goods_id", item.getGoodsId())
//                                .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
//                                .navigation();
//                    }
                    return; //余额不足
                }
                mDataBinding.mineDrawSubmit.setEnabled(false);
                showLoading();
                mViewModel.requestWithdraw(mDataBinding.mineDrawGrid);
            }
        });
        mViewModel.withdrawDataLivData.observe(this, items -> {
            hideLoading();
            mViewModel.addGridDatas(mDataBinding.mineDrawGrid,
                    mDataBinding.mineDrawSubmit, mDataBinding.mineDrawDescContent);
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
                    showCongratulationsDialog();
                } else {
                    showLoading("加载中");
                }
                mViewModel.getLoadWithdraWalletDite();
                mViewModel.getLoadWithdrawData(true); //更新配置信息
                EventBus.getDefault().post(new WalletRefreshEvent(1));
            } else {
                hideLoading();
            }
        });
        mViewModel.getLoadWithdraWalletDite();
        mViewModel.getLoadWithdrawData(false);
        AdManager.INSTANCE.loadInterstitialAd(this, new SimpleInterstitialListener() {
            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                super.onAdError(code, errorMsg);
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
            }
        });
        mViewModel.awardScrollDataLiveData.observe(this,resu->{
            if(resu != null){
                mDataBinding.mineDrawSubmitLbv.refreshData(resu.getList());
            }
        });
        //呼吸动画
        if (mScaleAnimation == null) {
            mScaleAnimation = new ScaleAnimation(1.15f, 0.9f, 1.15f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mScaleAnimation.setInterpolator(new LinearInterpolator());
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(Animation.INFINITE);
            mScaleAnimation.setDuration(1000);
            mDataBinding.mindYywJd.startAnimation(mScaleAnimation);
        }
        mViewModel.getWiningRotation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDataBinding.mineDrawSubmitLbv != null) {
            mDataBinding.mineDrawSubmitLbv.resumeScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDataBinding.mineDrawSubmitLbv != null) {
            mDataBinding.mineDrawSubmitLbv.pauseScroll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataBinding.mineDrawSubmitLbv != null) {
            mDataBinding.mineDrawSubmitLbv.stopScroll();
        }
    }

    //成功之后的抽奖弹窗
    private void showCongratulationsDialog() {
        if (mViewModel.withdrawSelectDto == null) {
            return;
        }
        double money = mViewModel.withdrawSelectDto.money;
        MineCongratulationsDialog mNoDrawDialog =
                new MineCongratulationsDialog(this, "" + money);
        mNoDrawDialog.setFinishListener(() -> mNoDrawDialog.dismiss());
        mNoDrawDialog.create();
        mNoDrawDialog.show(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initData() {
    }

    //积分任务
    private void getTaskList() {
        showLoading("查询中");
        IntegralComponent.getInstance().getIntegral(new IntegralComponent.IntegralHttpCallBack() {
            @Override
            public void onSuccess(ProxyIntegral integralBean) {
                runOnUiThread(() -> {
                    hideLoading();
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Integral.PAGER_INTEGRAL)
//                            .withSerializable("proxyIntegral", integralBean)
                            .navigation();
                    runOnUiThread(() -> {
                        finish();
                    });
                });
            }

            @Override
            public void onError(String var1) {
                runOnUiThread(() -> {
                    hideLoading();
                });
            }

            @Override
            public void onNoTask() {
                runOnUiThread(() -> {
                    hideLoading();
                    ToastUtil.showShort(WithdrawalCenterActivity.this, "暂无未完成任务");
                });
            }
        });
    }
}