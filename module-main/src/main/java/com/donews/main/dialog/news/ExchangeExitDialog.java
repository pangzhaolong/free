package com.donews.main.dialog.news;


import android.content.DialogInterface;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.donews.main.databinding.MainExchangeExitDialogBinding;
import com.donews.middle.BuildConfig;
import com.donews.middle.bean.mine2.reqs.SignReq;
import com.donews.middle.dialog.BaseBindingFragmentDialog;
import com.donews.middle.events.HomeGoodGetJbSuccessEvent;
import com.donews.yfsdk.loader.AdManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 首页 -> 退出弹窗(超级奖励)
 */
@Route(path = RouterActivityPath.Main.PAGER_EXCHANGE_EXIT_DIALOG)
public class ExchangeExitDialog extends BaseBindingFragmentDialog<MainExchangeExitDialogBinding> {

    /**
     * 单利构建对象
     *
     * @return
     */
    public static ExchangeExitDialog getInstance() {
        ExchangeExitDialog dialog = new ExchangeExitDialog();
        return dialog;
    }

    public ExchangeExitDialog() {
        super(R.layout.main_exchange_exit_dialog);
    }

    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        initDatabinding();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatabinding();
    }

    @Override
    public void dismiss() {
        if (getActivity() instanceof MvvmBaseLiveDataActivity) {
            ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
        }
        super.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    /**
     * 获取描述内容
     *
     * @return
     */
    public Spanned getDescText() {
        return Html.fromHtml("最高可得<font color='#FF0000'>1000</font>金币");
    }

    /**
     * 领取按钮的点击
     */
    public void gotoReceiveClick(View view) {
        view.setEnabled(false);
        if (getActivity() instanceof MvvmBaseLiveDataActivity) {
            ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).showLoading("获取奖励");
        }
        //开启激励视频
        AdManager.INSTANCE.loadRewardVideoAd(getActivity(), new SimpleRewardVideoListener() {
            boolean isVerifyReward = false;

            @Override
            public void onVideoCached() {
                super.onVideoCached();
                if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                }
                dismiss();
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
                if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                }
                dismiss();
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                if (getActivity() instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) getActivity()).hideLoading();
                }
                view.setEnabled(true);
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
                    EventBus.getDefault().post(new HomeGoodGetJbSuccessEvent());
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
    }

    //初始化dataBinding数据绑定
    private void initDatabinding() {
        dataBinding.setThiz(this);
    }


}