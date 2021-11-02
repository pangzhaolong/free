package com.module.lottery.dialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dn.sdk.sdk.interfaces.listener.impl.SimpleRewardVideoListener;
import com.donews.common.ad.business.loader.AdManager;
import com.module.lottery.ui.LotteryActivity;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryStartDialogLayoutBinding;

import java.lang.ref.WeakReference;

//抽奖码小于6个
public class LotteryCodeStartsDialog extends BaseDialog<LotteryStartDialogLayoutBinding> {
    private LotteryActivity mContext;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    boolean aAState = false;
    public LotteryCodeStartsDialog(LotteryActivity context) {
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
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDataBinding.lotteryText01.destroy();
                mDataBinding.lotteryText02.destroy();
                mDataBinding.lotteryText03.destroy();
                mDataBinding.lotteryText04.destroy();
            }
        });

    }


    private  void initView(){
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



    private  void loadAd(){
        AdManager.INSTANCE.loadRewardVideoAd(mContext, new SimpleRewardVideoListener() {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
               if(mOnFinishListener!=null){
                   mOnFinishListener.onFinish();
               }
            }

            @Override
            public void onRewardedClosed() {
                super.onRewardedClosed();
                //有效关闭
                if (aAState) {
                    if(mOnFinishListener!=null){
                        mOnFinishListener.onJumpAdFinish();
                    }
                }
                if(mOnFinishListener!=null){
                    mOnFinishListener.onFinish();
                }
            }

            @Override
            public void onRewardVerify(boolean result) {
                super.onRewardVerify(result);
                aAState = result;
            }
        });

    }


    @Override
    public float setSize() {
        return 0.7f;
    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (mOnFinishListener != null) {
            mOnFinishListener.onFinish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
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
            }
        }
    }


}