package com.donews.home.dialogs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.R;
import com.donews.home.databinding.HomeExchangeGoodNotDialogBinding;
import com.donews.home.viewModel.ExchangeViewModel;
import com.donews.home.viewModel.HomeViewModel;
import com.donews.middle.adutils.FeedNativeAndTemplateAd;
import com.donews.middle.bean.home.HomeCoinCritConfigBean;
import com.donews.middle.bean.home.HomeEarnCoinReq;
import com.donews.middle.dialog.BaseBindingFragmentDialog;
import com.donews.middle.dialog.qbn.DoingResultDialog;
import com.donews.middle.viewmodel.BaseMiddleViewModel;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.yfsdk.loader.AdManager;

/**
 * 首页 -> 立即兑换 -> 金币不足的弹窗
 */
@Route(path = RouterFragmentPath.Home.PAGER_EXCHANGE_GOOD_JB_FRAGMENT_DIALOG)
public class ExchangeGoodJbFragmentDialog extends BaseBindingFragmentDialog<HomeExchangeGoodNotDialogBinding> {

    /**
     * 单利构建对象
     *
     * @param type  模式 0:缺少金币模式，1:缺少活跃模式
     * @param diffe 还差多少
     * @return
     */
    public static ExchangeGoodJbFragmentDialog getInstance(int type, int diffe) {
        ExchangeGoodJbFragmentDialog dialog = new ExchangeGoodJbFragmentDialog();
        if (dialog.getArguments() == null) {
            dialog.setArguments(new Bundle());
        }
        dialog.getArguments().putInt("uiType", type);
        dialog.getArguments().putInt("diffe", diffe);
        return dialog;
    }

    // ViewModel 对象
    private HomeViewModel homeViewModel;
    // ViewModel 对象
    private ExchangeViewModel exchanViewMovdel;

    public HomeCoinCritConfigBean config;

    // 模式 0:缺少金币模式，1:缺少活跃模式
    @Autowired(name = "uiType")
    public int modeType = -1;

    // 还差多少
    @Autowired(name = "diffe")
    public int diffe = 0;

    public ExchangeGoodJbFragmentDialog() {
        super(R.layout.home_exchange_good_not_dialog);
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
            diffe = getArguments().getInt("diffe", 0);
        }
        homeViewModel = createViewModel(getActivity(), HomeViewModel.class);
        exchanViewMovdel = createViewModel(getActivity(), ExchangeViewModel.class);
        if (modeType == 0) {
            config = exchanViewMovdel.querySaveCoinCritConfig();
        }
        initDatabinding();
        showClose();
        //加载信息流广告
        showFeed();
    }

    //显示关闭弹窗
    private void showClose() {
        ValueAnimator alAm = ValueAnimator.ofFloat(0, 1);
        alAm.addUpdateListener(animation -> {
            float valu = (float) animation.getAnimatedValue();
            if (valu > 1) {
                dataBinding.tvClose.setAlpha(1);
                return;
            }
            dataBinding.tvClose.setAlpha(valu);
        });
        alAm.setDuration(3500).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatabinding();
        checkNewUserGuide(0);
    }

    //获取各种状态的Text
    public CharSequence getStatusText() {
        if (modeType == 0) {
            //金币模式
            if (config != null && config.open) {
                return Html.fromHtml("再看" + config.open_times + "次即可获得<font color='#F5562A'>“超暴击”金币</font>");
            } else {
                return Html.fromHtml("还差<font color='#F5562A'>" + diffe + "金币</font>即可兑换了哦~");
            }
        } else {
            //积分模式
            return Html.fromHtml("目前还需要<font color='#F5562A'>" + diffe + "活跃度即可</font>兑换");
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    /**
     * 右侧按钮的点击事件
     */
    public void rightGoTo(View view) {
        if (modeType == 0) {
            //赚金币。看视频
            loadAdVideo();
        } else {
            //赚活跃。去活动页面
            ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                    .withInt("position", 2)
                    .navigation();
            dismiss();
        }
    }

    /**
     * 左侧按钮的点击事件
     */
    public void leftGoTo(View view) {
        if (dataBinding.tvClose.getAlpha() < 1) {
            return;
        }
        dismiss();
    }

    //初始化dataBinding数据绑定
    private void initDatabinding() {
        dataBinding.setThiz(this);
    }

    //加载视频
    private void loadAdVideo() {
        if (getActivity() == null) {
            ToastUtil.showShort(getContext(), "视频获取失败,请稍后再试");
            return;
        }
        dataBinding.butNext.setEnabled(false);
        FragmentActivity activity = getActivity();
        if (activity instanceof MvvmBaseLiveDataActivity) {
            ((MvvmBaseLiveDataActivity<?, ?>) activity).showLoading("加载中");
        }
        AdManager.INSTANCE.loadRewardVideoAd(activity, new SimpleRewardVideoListener() {
            //是否发放了奖励
            boolean isVerifyReward = false;

            @Override
            public void onVideoCached() {
                super.onVideoCached();
                if (activity instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) activity).hideLoading();
                }
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
                dismiss();
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                ToastUtil.showShort(activity, "视频加载异常,请稍后再试!");
                super.onAdError(code, errorMsg);
                if (activity instanceof MvvmBaseLiveDataActivity) {
                    ((MvvmBaseLiveDataActivity<?, ?>) activity).showLoading("加载中");
                }
                dismiss();
            }

            @Override
            public void onRewardVerify(boolean result) {
                isVerifyReward = result;
                if (isVerifyReward) {
                    HomeEarnCoinReq req = new HomeEarnCoinReq();
                    req.user_id = AppInfo.getUserId();
                    exchanViewMovdel.getEarnCoin(req)
                            .observe(activity, (item) -> {
                                if (item != null) {
                                    if (BaseMiddleViewModel.getBaseViewModel().mine2JBCount.getValue() != null) {
                                        BaseMiddleViewModel.getBaseViewModel().mine2JBCount.postValue(
                                                BaseMiddleViewModel.getBaseViewModel().mine2JBCount.getValue() + item.coin);
                                    } else {
                                        BaseMiddleViewModel.getBaseViewModel().mine2JBCount.postValue(item.coin);
                                    }
                                    //显示金币弹窗
                                    showDoingResultDialog(activity, item.coin);
                                } else {
                                    ToastUtil.showShort(activity, "奖励发放失败,请稍后重试!");
                                }
                            });
                }
            }

            @Override
            public void onAdClose() {
                if (!isVerifyReward) {
                    ToastUtil.showLong(activity, "需要参与广告互动才能领取奖励哦");
                }
            }
        });
    }

    // 显示活动奖励弹窗
    private void showDoingResultDialog(FragmentActivity activity, int count) {
        DoingResultDialog dialog = new DoingResultDialog(activity, count, R.drawable.sign_reward_mine_dialog_djb);
        dialog.setStateListener(() -> {
        });
        dialog.show(activity);
    }

    //显示信息流
    private void showFeed() {
        Activity act = getActivity();
        FeedNativeAndTemplateAd.INSTANCE.loadFeedTemplateAdNew(act, dataBinding.adLayout, null);
    }

    /**
     * 检查新用户引导
     *
     * @param type 0:兑换弹窗的确定按钮引导
     *             1: 兑换页弹窗取消按钮对抽奖Tab的引导
     *             1: 兑换页弹窗取消按钮对活动Tab的引导
     */
    private void checkNewUserGuide(int type) {
//        showGuideType0();
    }

}