package com.module.lottery.dialog;

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

import com.module_lottery.R;
import com.module_lottery.databinding.GenerateDialogLayoutBinding;

import java.lang.ref.WeakReference;

//生成抽奖码
public class GenerateCodeDialog extends BaseDialog<GenerateDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    public GenerateCodeDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
    }

    @Override
    public int setLayout() {
        return R.layout.generate_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 3000);
        initView();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDataBinding.lotteryText01.destroy();
                mDataBinding.lotteryText02.destroy();
                mDataBinding.lotteryText03.destroy();
                mDataBinding.lotteryText04.destroy();
                mDataBinding.lotteryText05.destroy();
                mDataBinding.lotteryText06.destroy();
                mDataBinding.lotteryText07.destroy();

            }
        });
    }


    private  void initView(){
        mDataBinding.lotteryText01.start();
        mDataBinding.lotteryText02.start();
        mDataBinding.lotteryText03.start();
        mDataBinding.lotteryText04.start();
        mDataBinding.lotteryText05.start();
        mDataBinding.lotteryText06.start();
        mDataBinding.lotteryText07.start();
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
        private WeakReference<GenerateCodeDialog> reference;   //

        LotteryHandler(GenerateCodeDialog context) {
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