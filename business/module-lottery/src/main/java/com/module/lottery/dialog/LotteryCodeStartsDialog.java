/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/11/16<br>
 * 版本：V1.0<br>
 */
package com.module.lottery.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener;
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleRewardVideoListener;
import com.dn.sdk.sdk.interfaces.view.PreloadVideoView;
import com.donews.base.base.AppManager;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.business.loader.AdManager;
import com.donews.common.ad.cache.AdVideoCacheUtils;
import com.donews.middle.abswitch.ABSwitch;
import com.module.lottery.ui.LotteryActivity;
import com.module.lottery.utils.LotteryPreloadVideoView;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryStartDialogLayoutBinding;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

//抽奖码小于6个
public class LotteryCodeStartsDialog extends BaseDialog<LotteryStartDialogLayoutBinding>implements DialogInterface.OnDismissListener {
    private static final String TAG = "LotteryCodeStartsDialog";
    private static final String CLOSURE_HINT = "抽奖失败,请稍后再试";
    private LotteryActivity mContext;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    boolean aAState = false;
    public LotteryCodeStartsDialog(LotteryActivity context ) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
    }


    @Override
    public int setLayout() {
        return R.layout.lottery_start_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 1500);
        setOnDismissListener(this);
        Message delay = new Message();
        delay.what = 3;
        mLotteryHandler.sendMessageDelayed(delay, 20000);
    }


    private void initView() {
        mDataBinding.jsonAnimation.setImageAssetsFolder("images");
        mDataBinding.jsonAnimation.setAnimation("joystick_01.json");
        mDataBinding.jsonAnimation.loop(false);
        mDataBinding.jsonAnimation.playAnimation();
        mDataBinding.jsonAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mDataBinding.lotteryText01.start();
                mDataBinding.lotteryText02.start();
                mDataBinding.lotteryText03.start();
                mDataBinding.lotteryText04.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    private void addVideoViewToast() {
        try {
            Activity activity = AppManager.getInstance().getTopActivity();
            if (activity != null) {
                if (ABSwitch.Ins().isOpenVideoToast()) {
                    ScaleAnimation mScaleAnimation = new ScaleAnimation(1.1f, 0.88f, 1.1f, 0.88f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    mScaleAnimation.setInterpolator(new LinearInterpolator());
                    mScaleAnimation.setRepeatMode(Animation.REVERSE);
                    mScaleAnimation.setRepeatCount(Animation.INFINITE);
                    mScaleAnimation.setDuration(1000);
                    View view = LayoutInflater.from(mContext).inflate(R.layout.pop_ups_layout, null);
                    LinearLayout linearLayout = view.findViewById(R.id.toast_view);
                    View decorView = activity.getWindow().getDecorView();
                    FrameLayout contentParent =
                            (FrameLayout) decorView.findViewById(android.R.id.content);
                    contentParent.addView(view);
                    linearLayout.setAnimation(mScaleAnimation);

                }
            } else {
                ToastUtil.showShort(mContext, "完整观看视频即可获得抽奖码");
            }
        } catch (Exception e) {
            Logger.e("" + e.getMessage());
            ToastUtil.showShort(mContext, "完整观看视频即可获得抽奖码");
        }
        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish();
        }


    }

    private void closedVideoViewToast() {
        //有效关闭
        if (aAState) {
            if (mOnFinishListener != null) {
                mOnFinishListener.onJumpAdFinish();
            }
        }
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


    private void loadAd() {
        AdVideoCacheUtils.INSTANCE.playRewardVideo(new IAdRewardVideoListener() {
            @Override
            public void onError(int code, String msg) {
                loadError();
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
                //延时出现
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
                aAState = result;
            }
            //点击跳过
            @Override
            public void onSkippedRewardVideo() {
                onVideoComplete();
            }
        });
    }


    private void loadError() {

        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish();
            if (mContext != null) {
                ToastUtil.showShort(mContext, CLOSURE_HINT);
            }
        }

    }

    @Override
    public float setSize() {
        return 0.7f;
    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

    }


    private void showToast() {
        if (mLotteryHandler != null) {
            //延时出现
            Message mes = new Message();
            mes.what = 2;
            mLotteryHandler.sendMessageDelayed(mes, 2000);
        }
    }


    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mDataBinding.lotteryText01.destroy();
        mDataBinding.lotteryText02.destroy();
        mDataBinding.lotteryText03.destroy();
        mDataBinding.lotteryText04.destroy();
        if(mLotteryHandler!=null){
            mLotteryHandler.removeMessages(0);
            mLotteryHandler.removeCallbacksAndMessages(null);
        }
    }

    public interface OnStateListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onJumpAdFinish();


    }

    private static class LotteryHandler extends Handler {
        private WeakReference<LotteryCodeStartsDialog> reference;   //

        LotteryHandler(LotteryCodeStartsDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && reference.get().mOnFinishListener != null) {
                        //广告跳转
                        reference.get().loadAd();
                    }
                    break;
                case 2:
                    if (reference.get() != null) {
                        //广告跳转
                        reference.get().addVideoViewToast();
                    }
                    break;

                case 3:
                    if (reference.get() != null) {
                        if (reference.get().isShowing()) {
                            reference.get().dismiss();
                            if (reference.get().getContext() != null) {
                                ToastUtil.showShort(reference.get().getContext(), CLOSURE_HINT);
                            }
                        }
                    }
                    break;
            }
        }
    }


}