package com.donews.mine.dialogs.news;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.bean.mine2.reqs.SignReq;
import com.donews.middle.dialog.BaseBindingFragmentDialog;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.databinding.Mine2SigninRewardDialogBinding;
import com.donews.mine.viewModel.MineViewModel;
import com.donews.yfsdk.loader.AdManager;

/**
 * 个人中心任务 签到奖励弹窗(签到奖励、激励、任务奖励)
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_SIGN_REWARD_DIALOG)
public class SignInRewardMineDialog extends BaseBindingFragmentDialog<Mine2SigninRewardDialogBinding> {

    /**
     * 单利构建对象
     *
     * @param type 模式 0:激励模式，1:领取模式(带自带倒计时关闭)，2：任务奖励模式
     * @return
     */
    public static SignInRewardMineDialog getInstance(int type) {
        SignInRewardMineDialog dialog = new SignInRewardMineDialog();
        if (dialog.getArguments() == null) {
            dialog.setArguments(new Bundle());
        }
        dialog.getArguments().putInt("uiType", type);
        return dialog;
    }

    // ViewModel 对象
    private MineViewModel mineViewModel;
    private int count = 5;
    //是否自己发起的网络签到请求(不是一定准确)
    private boolean isMeRequeSign = false;
    // 延迟计时任务
    private Runnable updateTextRun = new Runnable() {
        @Override
        public void run() {
            if (count <= 0) {
                dismiss();
                return;
            }
            count--;
            dataBinding.signGoText.setText("开心收下(" + count + "s)");
            dataBinding.signGoText.postDelayed(updateTextRun, 1000);
        }
    };

    // 模式 0:激励模式，1:领取模式(带自带倒计时关闭),2:任务奖励领取模式
    @Autowired(name = "uiType")
    public int modeType = -1;

    public SignInRewardMineDialog() {
        super(R.layout.mine2_signin_reward_dialog);
    }

    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        //获取ViewModel对象
        ARouter.getInstance().inject(this);
        if (modeType == -1) {
            //防止注入失效
            modeType = getArguments().getInt("uiType", 0);
        }
        mineViewModel = createViewModel(getActivity(), MineViewModel.class);
        initDatabinding();
        if (modeType == 1) {
            //领取通知模式。显示倒计时
            dataBinding.signGoText.setText("开心收下(" + count + "s)");
            dataBinding.signGoText.postDelayed(updateTextRun, 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatabinding();
    }

    @Override
    public void dismiss() {
        if (dataBinding.signGoText.getHandler() != null) {
            dataBinding.signGoText.getHandler().removeCallbacks(updateTextRun);
        }
        super.dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (dataBinding.signGoText.getHandler() != null) {
            dataBinding.signGoText.getHandler().removeCallbacks(updateTextRun);
        }
        super.onDismiss(dialog);
    }

    /**
     * 顶部图片的资源获取方法
     *
     * @return
     */
    public int getTitleIconResId() {
        switch (modeType) {
            case 0:
                return R.drawable.sign_reward_mine_dialog_top;
            case 1:
                return R.drawable.sign_reward_mine_dialog_top_double;
            case 2:
                return R.drawable.sign_reward_mine_dialog_top_rw;
        }
        return 0;
    }

    /**
     * 翻倍领取按钮的点击
     */
    public void doubleReceiveClick(View view) {
        if (modeType == 0) {
            //翻倍领取
            isMeRequeSign = true;
            //开启激励视频
            AdManager.INSTANCE.loadRewardVideoAd(getActivity(), new SimpleRewardVideoListener() {
                boolean isVerifyReward = false;

                @Override
                public void onAdError(int code, @Nullable String errorMsg) {
                    super.onAdError(code, errorMsg);
                    if (BuildConfig.DEBUG) {
                        ToastUtil.showShort(getActivity(), "加载失败,code=" + code + ",msg=" + errorMsg);
                    } else {
                        ToastUtil.showShort(getActivity(), "加载失败,请稍后重试");
                    }
                }

                @Override
                public void onRewardVerify(boolean result) {
                    isVerifyReward = true;
                    if (result) {
                        //可以发放奖励
                        if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                            ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).showLoading("发放奖励中");
                        }
                        SignReq req = new SignReq();
                        req.double_ = true;
                        mineViewModel.requestSign(req, true);
                    }
                }

                @Override
                public void onAdClose() {
                    //可以发放奖励
                    if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                        ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                    }
                    if (isVerifyReward) {
                        ToastUtil.showShort(getActivity(), "需要参与活动才能翻倍领取哦~");
                    }
                    super.onAdClose();
                }
            });
        } else {
            //开心收下
            dismiss();
        }
    }

    //初始化dataBinding数据绑定
    private void initDatabinding() {
        dataBinding.setThiz(this);
        dataBinding.setVModel(mineViewModel);
        if (modeType == 0) {
            mineViewModel.mineSignDounleResult.observe(this, (res) -> {
                if (isMeRequeSign) {
                    isMeRequeSign = false;
                    dismiss();
                    //显示发放奖励弹窗
                    getInstance(1)
                            .show(getActivity().getSupportFragmentManager(), "ssss");
                }
            });
        }
    }


}