/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.module.lottery.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.module.lottery.bean.LotteryCodeBean;
import com.module_lottery.R;
import com.module_lottery.databinding.EnvelopeStateDialogLayoutBinding;
import com.module_lottery.databinding.LessMaxDialogLayoutBinding;

import java.lang.ref.WeakReference;

//首页包状态开完 EnvelopeState 开完后
public class EnvelopeStateDialog extends BaseDialog<EnvelopeStateDialogLayoutBinding> {
    private Context mContext;

    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    private OnFinishListener mOnFinishListener;
    private boolean isSendCloseEvent = true;

    public EnvelopeStateDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }

    @Override
    public int setLayout() {
        return R.layout.envelope_state_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public float setSize() {
        return 0.8f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        mDataBinding.jsonAnimation.setImageAssetsFolder("images");
        mDataBinding.jsonAnimation.setAnimation("orange_process.json");
        mDataBinding.jsonAnimation.loop(true);
        mDataBinding.jsonAnimation.playAnimation();
        mDataBinding.jsonHand.setImageAssetsFolder("images");
        mDataBinding.jsonHand.setAnimation("lottery_finger.json");
        mDataBinding.jsonHand.loop(true);
        mDataBinding.jsonHand.playAnimation();


        setOnDismissListener((d) -> {
            if (isSendCloseEvent) {
                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Increase_ChancesDialog_Close);
            }
        });
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDataBinding.lotteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSendCloseEvent = false;
                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Increase_ChancesDialog_Continue);
                if (mOnFinishListener != null) {
                    mOnFinishListener.onFinishAd();
                    dismiss();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        return true;
    }


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onFinishAd();
    }


    private static class LotteryHandler extends Handler {
        private WeakReference<EnvelopeStateDialog> reference;   //

        LotteryHandler(EnvelopeStateDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.closure.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}