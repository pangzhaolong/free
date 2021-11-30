package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.module.lottery.bean.LotteryCodeBean;
import com.module_lottery.R;
import com.module_lottery.databinding.ReceiveDialogLayoutBinding;

import java.lang.ref.WeakReference;

//领取抽奖
public class ReceiveLotteryDialog extends BaseDialog<ReceiveDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private LotteryCodeBean mLotteryCodeBean;

    private boolean mIfQuit;


    public ReceiveLotteryDialog(Context context, LotteryCodeBean lotteryCodeBean, boolean ifQuit) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
        this.mIfQuit = ifQuit;
        this.mLotteryCodeBean = lotteryCodeBean;
    }

    @Override
    public int setLayout() {
        return R.layout.receive_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding.closureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setQuantity();
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 1000);
        initView();
    }


    private boolean isSendCloseEvent = true;

    @Override
    public float setSize() {
        return 1.0f;
    }

    private void initView() {
        if (mIfQuit) {
            mDataBinding.titleForward.setText(getContext().getResources().getString(R.string.return_receive_title_forward));
            mDataBinding.titleRear.setText(getContext().getResources().getString(R.string.return_receive_title_rear));
            mDataBinding.subtitle.setText(getContext().getResources().getString(R.string.return_receive_subtitle));
            mDataBinding.playIcon.setVisibility(View.GONE);
            mDataBinding.buttonText.setText(getContext().getResources().getString(R.string.return_receive_button_text));
        }
        mDataBinding.jsonAnimationHand.setImageAssetsFolder("images");
        mDataBinding.jsonAnimationHand.setAnimation("hand.json");
        mDataBinding.jsonAnimationHand.loop(true);
        mDataBinding.jsonAnimationHand.playAnimation();

        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnFinishListener != null) {
                    AnalysisUtils.onEventEx(context, Dot.Lottery_Exit_Drawing_Count_Receive);
                    mOnFinishListener.onLottery();
                }
            }
        });
        setOnDismissListener((d) -> {
            if (isSendCloseEvent) {
                AnalysisUtils.onEventEx(context, Dot.Lottery_Exit_Drawing_Count_Close);
            }
        });
    }

    private void setQuantity() {
        if (mLotteryCodeBean != null) {
            //设置显示的数量
            if (mLotteryCodeBean.getCodes().size() > 0 && mLotteryCodeBean.getCodes().size() < 6) {
                mDataBinding.quantity.setText(" " + (6 - (mLotteryCodeBean.getCodes().size())) + " ");
            }
            //抽奖码满了
            if (mLotteryCodeBean.getCodes().size() == 6) {
            }

        }


    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);


    }


    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onJumpAd();


        void onLottery();
    }

    private static class LotteryHandler extends Handler {
        private WeakReference<ReceiveLotteryDialog> reference;   //

        LotteryHandler(ReceiveLotteryDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        //广告跳转
                        reference.get().mDataBinding.closureButton.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}