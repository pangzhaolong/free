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
import com.donews.turntable.databinding.TurntableRuleDialogLayoutBinding;

import java.lang.ref.WeakReference;

//规则弹窗
public class RuleDialog extends BaseDialog<TurntableRuleDialogLayoutBinding> implements DialogInterface.OnDismissListener {


    private WeakRule mLotteryCritCodeHandler = new WeakRule(this);
    private OnStateListener mOnFinishListener;

    public RuleDialog(@NonNull Context context ) {
        super(context, R.style.dialogTransparent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryCritCodeHandler.sendMessageDelayed(message, 1000);
        setOnDismissListener(this);
        initView();

    }

    private void initView() {
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mLotteryCritCodeHandler != null) {
            mLotteryCritCodeHandler.removeMessages(0);
            mLotteryCritCodeHandler.removeMessages(1);
            mLotteryCritCodeHandler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    public int setLayout() {
        return R.layout.turntable_rule_dialog_layout;
    }

    @Override
    public float setSize() {
        return 0.8f;
    }

    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        void onStartCrit();


    }


    private static class WeakRule extends Handler {
        private WeakReference<RuleDialog> reference;   //

        WeakRule(RuleDialog context) {
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
