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

import com.module.lottery.bean.LotteryCodeBean;
import com.module_lottery.R;
import com.module_lottery.databinding.ReceiveDialogLayoutBinding;

import java.lang.ref.WeakReference;

//领取抽奖
public class ReceiveLotteryDialog extends BaseDialog implements View.OnClickListener {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private LotteryCodeBean mLotteryCodeBean;
    private ReceiveDialogLayoutBinding receiveDialogLayout;

    public ReceiveLotteryDialog(Context context, LotteryCodeBean lotteryCodeBean) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
        this.mLotteryCodeBean = lotteryCodeBean;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveDialogLayout = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.receive_dialog_layout, null, false);
        receiveDialogLayout.setData(mLotteryCodeBean);
        setContentView(receiveDialogLayout.getRoot());

        receiveDialogLayout.closureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        receiveDialogLayout.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();


            }
        });
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 1000);

    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);


    }

    @Override
    public void onClick(View view) {
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

        void onJumpAd();


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
                        reference.get().receiveDialogLayout.closureButton.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}