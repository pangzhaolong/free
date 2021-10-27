package com.module.lottery.dialog;

import android.app.Dialog;
import android.content.Context;
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

import com.module_lottery.R;
import com.module_lottery.databinding.LotteryStartDialogLayoutBinding;

import java.lang.ref.WeakReference;

//抽奖码小于6个
public class LotteryCodeStartsDialog extends BaseDialog<LotteryStartDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    public LotteryCodeStartsDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
    }

    @Override
    public int setLayout() {
        return R.layout.lottery_start_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 3000);
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

        void onJumpAd();


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
                        reference.get().mOnFinishListener.onJumpAd();
                    }
                    break;
            }
        }
    }


}