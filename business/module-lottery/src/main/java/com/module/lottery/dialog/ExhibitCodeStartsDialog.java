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

import com.module_lottery.R;
import com.module_lottery.databinding.ExhibitCodeDialogLayoutBinding;

import java.lang.ref.WeakReference;

//展示生成的抽奖码
public class ExhibitCodeStartsDialog extends BaseDialog implements View.OnClickListener {
    private Context context;
    private ExhibitCodeDialogLayoutBinding exhibitCodeDialogLayout;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    public ExhibitCodeStartsDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibitCodeDialogLayout = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.exhibit_code_dialog_layout, null, false);
        setContentView(exhibitCodeDialogLayout.getRoot());
        Message mes = new Message();
        mes.what = 1;
        mLotteryHandler.sendMessageDelayed(mes, 3000);
        exhibitCodeDialogLayout.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFinishListener != null) {
                    mOnFinishListener.onFinish();
                }
            }
        });

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
        private WeakReference<ExhibitCodeStartsDialog> reference;   //

        LotteryHandler(ExhibitCodeStartsDialog context) {
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