package com.module.lottery.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dn.sdk.AdCustomError;
import com.dn.sdk.listener.interstitial.SimpleInterstitialFullListener;
import com.dn.sdk.listener.rewardvideo.IAdRewardVideoListener;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.donews.base.base.AppManager;
import com.donews.base.utils.ToastUtil;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.middle.adutils.RewardVideoAd;
import com.donews.middle.dialog.LoadAdErrorDialog;
import com.donews.middle.utils.CommonAnimationUtils;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.DateManager;
import com.donews.yfsdk.monitor.InterstitialFullAdCheck;
import com.donews.yfsdk.monitor.LotteryAdCheck;
import com.module_lottery.R;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

public class PlayAdUtilsTool {
    private static final String TAG = "PlayAdUtilsTool";
    public static final String CLOSURE_HINT = "抽奖失败,请稍后再试";
    public static final String CLOSURE_HINT_TIME_OUT = "获取视频超时,请稍后再试";
    private PlayAdUtilsToolHandler mPlayAdUtilsToolHandler = new PlayAdUtilsToolHandler(this);
    private Context mContext;
    private IStateListener mIStateListener;
    private boolean aAState = false;
    private int mNeedShowRetryDialog = 0;
    private LoadAdErrorDialog mLoadAdErrDialog = null;

    public PlayAdUtilsTool(Context context) {
        this.mContext = context;
    }

    public void showRewardVideo(Activity activity, final Dialog dialog) {
        aAState = false;
        IAdRewardVideoListener listener = new SimpleRewardVideoListener() {
            @Override
            public void onAdStartLoad() {
                Logger.e(TAG + "onAdStartLoad()");
            }

            @Override
            public void onAdStatus(int code, @Nullable Object any) {

            }

            @Override
            public void onAdLoad() {

            }

            @Override
            public void onAdShow() {
                //关闭dialog
                dismissDialog(dialog);
                //广告显示成功  延时出现页面提示Toast
                showToast();
            }

            @Override
            public void onAdVideoClick() {

            }

            @Override
            public void onRewardVerify(boolean result) {
                if (result) {
                    DateManager.getInstance().putLotteryCount(DateManager.LOTTERY_COUNT);
                }
                aAState = result;
            }

            @Override
            public void onAdClose() {
                if (InterstitialFullAdCheck.INSTANCE.isEnable() == AdCustomError.OK && LotteryAdCheck.INSTANCE.checkOpenAd()) {
                    loadInstlAd();
                } else {
                    closedVideoViewToast();
                }
            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onVideoComplete() {
                videoComplete();
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                mNeedShowRetryDialog++;
                if (mNeedShowRetryDialog > 2) {
                    mNeedShowRetryDialog = 1;
                }
                Logger.e(TAG + errorMsg + "");
                AnalysisUtils.onEventEx(mContext, Dot.VIDEO_FAILED, "" + code);
                if (code == AdCustomError.PreloadAdEmptyError.getCode()) {
                    ToastUtil.showShort(mContext, "暂无新视频，请稍后再试" + code);
                }
                if (code == AdCustomError.PreloadTimesError.getCode()) {
                    ToastUtil.showShort(mContext, "加载视频超时，请稍后再试" + code);
                }
                if (code == AdCustomError.PreloadAdStatusError.getCode()) {
                    ToastUtil.showShort(mContext, "加载视频异常，请稍后再试" + code);
                } else {
                    ToastUtil.showShort(mContext, CLOSURE_HINT + code);
                }

                if (code != AdCustomError.CloseAd.getCode() && code != AdCustomError.LimitAdError.getCode()) {
                    loadError(activity, dialog, true);
                } else {
                    loadError(activity, dialog, false);
                }
            }
        };

        RewardVideoAd.INSTANCE.showPreloadRewardVideo(activity, listener, false);
    }

    private void loadInstlAd() {
        Activity resultActivity = AppManager.getInstance().getTopActivity();
        if (!(resultActivity instanceof MvvmBaseLiveDataActivity)) {
            resultActivity = AppManager.getInstance().getSecondActivity();
            if (!(resultActivity instanceof MvvmBaseLiveDataActivity)) {
                closedVideoViewToast();
                return;
            }
        }

        MvvmBaseLiveDataActivity activity = ((MvvmBaseLiveDataActivity) resultActivity);

        activity.showLoading("加载中...");

        /*InterstitialAd.INSTANCE.showAd(resultActivity, new SimpleInterstitialListener() {

            @Override
            public void onAdShow() {
                super.onAdShow();
                activity.hideLoading();
            }

            @Override
            public void onAdError(int code, String errorMsg) {
                super.onAdError(code, errorMsg);
                activity.hideLoading();
                closedVideoViewToast();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                closedVideoViewToast();
                activity.hideLoading();
            }
        });*/

        InterstitialFullAd.INSTANCE.showAd(activity, new SimpleInterstitialFullListener() {

            @Override
            public void onAdError(int errorCode, @NonNull String errprMsg) {
                closedVideoViewToast();
                activity.hideLoading();
            }

            @Override
            public void onAdShow() {
                activity.hideLoading();
            }

            @Override
            public void onAdClose() {
                closedVideoViewToast();
                activity.hideLoading();
            }

            @Override
            public void onAdShowFail(int errCode, @NonNull String errMsg) {
                closedVideoViewToast();
                activity.hideLoading();
            }

            @Override
            public void onAdVideoError(int errCode, @NonNull String errMsg) {
                activity.hideLoading();
                closedVideoViewToast();
            }
        });
    }


    private void videoComplete() {
        if (ABSwitch.Ins().isOpenVideoToast()) {
            try {
                Activity activity = AppManager.getInstance().getTopActivity();
                if (activity != null) {
                    View decorView = activity.getWindow().getDecorView();
                    FrameLayout contentParent =
                            (FrameLayout) decorView.findViewById(android.R.id.content);
                    ConstraintLayout toastLayout = contentParent.findViewById(R.id.toast_layout);
                    if (toastLayout != null) {
                        LinearLayout linearLayout = toastLayout.findViewById(R.id.toast_view);
                        if (linearLayout != null) {
                            linearLayout.clearAnimation();
                        }
                        if (toastLayout != null) {
                            contentParent.removeView(toastLayout);
                        }
                    }

                }
            } catch (Exception e) {
                Logger.e("" + e.getMessage());
            }
        }

    }


    private void closedVideoViewToast() {
        Logger.d(TAG + aAState + "");
        if (aAState) {
            //有效关闭
            if (mIStateListener != null) {
                Logger.d(TAG + "onComplete");
                mIStateListener.onComplete();
            } else {
                Logger.d(TAG + "onComplete is null");
            }
        } else {
            //无效关闭
            Logger.d(TAG + "closedVideoViewToast  aAState is false");
        }
    }


    private void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing() && mContext != null && mContext instanceof Activity && !((Activity) mContext).isFinishing() && !((Activity) mContext).isDestroyed()) {
            dialog.dismiss();
        }
    }

    private void showToast() {
        if (mPlayAdUtilsToolHandler != null) {
            //延时出现
            Message mes = new Message();
            mes.what = 1;
            mPlayAdUtilsToolHandler.sendMessageDelayed(mes, 2000);
        }
    }

    private void loadError(Activity activity, Dialog dialog, boolean needShowRetryDialog) {
        Activity act = AppManager.getInstance().getTopActivity();
        if (act!=null && !act.getClass().getName().equalsIgnoreCase("com.module.lottery.ui.LotteryActivity")) {
            dismissDialog(dialog);
            return;
        }
        if (mNeedShowRetryDialog <= 1 && needShowRetryDialog) {
            if (mLoadAdErrDialog == null) {
                mLoadAdErrDialog = new LoadAdErrorDialog(activity, new LoadAdErrorDialog.RetryListener() {
                    @Override
                    public void onRetry() {
                        if (!activity.isFinishing()) {
                            showRewardVideo(activity, dialog);
                        } else {
                            if (mContext != null) {
                                ToastUtil.showShort(mContext, CLOSURE_HINT);
                            }
                        }
                        mLoadAdErrDialog.dismiss();
                    }

                    @Override
                    public void onClose() {
                        mLoadAdErrDialog.dismiss();
                        dismissDialog(dialog);
                        if (mContext != null) {
                            ToastUtil.showShort(mContext, CLOSURE_HINT);
                        }
                    }
                });
            }
            if (mLoadAdErrDialog.isShowing()) {
                mLoadAdErrDialog.dismiss();
            }
            mLoadAdErrDialog.setOwnerActivity(activity);
            mLoadAdErrDialog.show();
        } else {
            dismissDialog(dialog);
            if (mContext != null) {
                ToastUtil.showShort(mContext, CLOSURE_HINT);
            }
        }
    }


    private void addVideoViewToast() {
        try {
            Activity activity = AppManager.getInstance().getTopActivity();
            if (activity != null && !activity.getClass().getSimpleName().equals("MainActivity") && !activity.getClass().getSimpleName().equals("LotteryActivity")) {
                if (ABSwitch.Ins().isOpenVideoToast()) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.pop_ups_layout, null);
                    LinearLayout linearLayout = view.findViewById(R.id.toast_view);
                    View decorView = activity.getWindow().getDecorView();
                    FrameLayout contentParent =
                            (FrameLayout) decorView.findViewById(android.R.id.content);
                    contentParent.addView(view);
                    linearLayout.setAnimation(CommonAnimationUtils.setScaleAnimation(1000));
                }
            } else {
                ToastUtil.showShort(mContext, "完整观看视频即可获得抽奖码");
            }
        } catch (Exception e) {
            Logger.e("" + e.getMessage());
            ToastUtil.showShort(mContext, "完整观看视频即可获得抽奖码");
        }
    }

    public void setIStateListener(IStateListener iStateListener) {
        this.mIStateListener = iStateListener;
    }

    private static class PlayAdUtilsToolHandler extends Handler {
        private WeakReference<PlayAdUtilsTool> reference;   //

        PlayAdUtilsToolHandler(PlayAdUtilsTool playAdUtilsTool) {
            reference = new WeakReference(playAdUtilsTool);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        //显示Toast
                        reference.get().addVideoViewToast();
                    }
                    break;
            }
        }
    }


    public interface IStateListener {
        /**
         * 广告播放完成
         */
        void onComplete();

    }


}
