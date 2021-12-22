package com.module.integral.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.module_integral.R;
import com.example.module_integral.databinding.BenefitUpgradeLayoutBinding;
import com.module.lottery.dialog.BaseDialog;

import java.lang.ref.WeakReference;

public class BenefitUpgradeDialog extends BaseDialog<BenefitUpgradeLayoutBinding> implements DialogInterface.OnDismissListener {
    private BenefitUpgradeHandler mUnlockMaxCodeHandler = new BenefitUpgradeHandler(this);
    private OnStateListener mOnFinishListener;

    public BenefitUpgradeDialog(Context context) {
        super(context, R.style.dialogTransparent);
    }

    @Override
    public float setDimAmount() {
        return 0.9f;
    }

    @Override
    public int setLayout() {
        return R.layout.benefit_upgrade_layout;
    }

    @Override
    public float setSize() {
        return 1.0f;
    }
    //获取商品数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mUnlockMaxCodeHandler.sendMessageDelayed(message, 1000);
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

        mDataBinding.jumpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFinishListener != null) {
                    mOnFinishListener.onJump();
                }
            }
        });

    }


    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        void onJump();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mUnlockMaxCodeHandler != null) {
            mUnlockMaxCodeHandler.removeMessages(0);
            mUnlockMaxCodeHandler.removeMessages(1);
            mUnlockMaxCodeHandler.removeCallbacksAndMessages(null);
        }
    }


    private static class BenefitUpgradeHandler extends Handler {
        private WeakReference<BenefitUpgradeDialog> reference;   //

        BenefitUpgradeHandler(BenefitUpgradeDialog context) {
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
