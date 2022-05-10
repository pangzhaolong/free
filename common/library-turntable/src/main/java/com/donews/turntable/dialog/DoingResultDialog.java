package com.donews.turntable.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.donews.middle.dialog.BaseDialog;
import com.donews.turntable.R;
import com.donews.turntable.bean.TurntablePrize;
import com.donews.turntable.databinding.TurntableDoingDialogLayoutBinding;
import com.donews.turntable.databinding.TurntableRuleDialogLayoutBinding;

import java.lang.ref.WeakReference;

//活动结果
public class DoingResultDialog extends BaseDialog<TurntableDoingDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private WeakDoingResult weakDoingResult = new WeakDoingResult(this);
    private OnStateListener mOnFinishListener;
    TurntablePrize mPrize;

    public DoingResultDialog(@NonNull Context context, TurntablePrize prize) {
        super(context, R.style.dialogTransparent);
        mPrize = prize;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        weakDoingResult.sendMessageDelayed(message, 1000);
        setOnDismissListener(this);
        mDataBinding.award.setText(mPrize.getName());
        mDataBinding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (weakDoingResult != null) {
            weakDoingResult.removeMessages(0);
            weakDoingResult.removeMessages(1);
            weakDoingResult.removeCallbacksAndMessages(null);
        }
    }

    public void acceptClick(View view) {


    }


    @Override
    public int setLayout() {
        return R.layout.turntable_doing_dialog_layout;
    }

    @Override
    public float setSize() {
        return 0.8f;
    }

    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        void onOK();
    }


    private static class WeakDoingResult extends Handler {
        private WeakReference<DoingResultDialog> reference;   //

        WeakDoingResult(DoingResultDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
            }
        }
    }
}
