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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener;
import com.donews.base.base.AppManager;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.cache.AdVideoCacheUtils;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.utils.DateManager;
import com.module_lottery.R;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

public class PlayAdUtilsTool {
    private static final String TAG = "PlayAdUtilsTool";
    public static final String CLOSURE_HINT = "抽奖失败,请稍后再试";
    private PlayAdUtilsToolHandler mPlayAdUtilsToolHandler = new PlayAdUtilsToolHandler(this);
    private Context mContext;
    private IStateListener mIStateListener;
    private boolean aAState = false;

    public PlayAdUtilsTool(Context context) {
        this.mContext = context;
    }


    public void showRewardVideo(final Dialog dialog) {
        IAdRewardVideoListener listener = new IAdRewardVideoListener() {
            @Override
            public void onError(int code, String msg) {
                loadError(dialog);
                Logger.e(TAG + msg + "");
            }

            @Override
            public void onLoadCached() {

            }

            @Override
            public void onLoad() {

            }

            @Override
            public void onLoadFail(int code, String error) {

            }

            @Override
            public void onLoadTimeout() {

            }

            @Override
            public void onRewardAdShow() {
                //关闭dialog
                dismissDialog(dialog);
                //广告显示成功  延时出现页面提示Toast
                showToast();
            }

            @Override
            public void onRewardBarClick() {

            }

            //点击关闭视频
            @Override
            public void onRewardedClosed() {
                closedVideoViewToast();
            }

            //视屏播放完成
            @Override
            public void onRewardVideoComplete() {
                onVideoComplete();
            }

            @Override
            public void onRewardVideoError() {

            }

            @Override
            public void onRewardVideoAdShowFail(int code, String message) {

            }

            @Override
            public void onRewardVerify(boolean result) {
                if (result) {
                    DateManager.getInstance().putLotteryCount(DateManager.LOTTERY_COUNT);
                }
                aAState = result;
            }

            //点击跳过
            @Override
            public void onSkippedRewardVideo() {
                onVideoComplete();
            }
        };

        AdVideoCacheUtils.INSTANCE.showRewardVideo(listener);
    }


    private void onVideoComplete() {
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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
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

    private void loadError(Dialog dialog) {
        dismissDialog(dialog);
        if (mContext != null) {
            ToastUtil.showShort(mContext, CLOSURE_HINT);
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
                    linearLayout.setAnimation(LotteryAnimationUtils.setScaleAnimation(1000));
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
