/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/11/16<br>
 * 版本：V1.0<br>
 */
package com.module.lottery.dialog;


import android.animation.Animator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.donews.base.utils.ToastUtil;
import com.donews.middle.dialog.BaseDialog;
import com.donews.middle.utils.PlayAdUtilsTool;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.module.lottery.ui.LotteryActivity;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryStartDialogLayoutBinding;

import java.lang.ref.WeakReference;

//抽奖码小于6个
public class LotteryCodeStartsDialog extends BaseDialog<LotteryStartDialogLayoutBinding>
        implements DialogInterface.OnDismissListener {
    private static final String TAG = "LotteryCodeStartsDialog";

    private LotteryActivity mContext;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);


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
        mLotteryHandler.sendMessageDelayed(mes, 500);
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


    @Override
    public float setSize() {
        return 0.7f;
    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

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
        if (mLotteryHandler != null) {
            mLotteryHandler.removeMessages(0);
            mLotteryHandler.removeCallbacksAndMessages(null);
        }
    }

    public interface OnStateListener {

        //开始请求激励视频
        void onLoadAd();

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
                        reference.get().mOnFinishListener.onLoadAd();
                    }
                    break;
                case 3:
                    if (reference.get() != null) {
                        if (reference.get().isShowing() && reference.get().mOnFinishListener != null) {
                            if (reference.get().getContext() != null) {
                                ToastUtil.showShort(reference.get().getContext(), PlayAdUtilsTool.CLOSURE_HINT_TIME_OUT);
                            }
                            if (reference.get().mContext != null && !reference.get().mContext.isFinishing() && !reference.get().mContext.isDestroyed()) {
                                reference.get().dismiss();
                            }
                        }
                    }
                    break;
            }
        }
    }


}