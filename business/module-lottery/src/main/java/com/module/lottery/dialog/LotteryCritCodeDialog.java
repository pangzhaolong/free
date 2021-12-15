package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.module.lottery.bean.CritCodeBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module_lottery.R;
import com.module_lottery.databinding.LotteryCritDialogLayoutBinding;

import java.lang.ref.WeakReference;

//暴击模式下,恭喜获得抽奖码,弹框
public class LotteryCritCodeDialog extends  BaseDialog<LotteryCritDialogLayoutBinding> implements DialogInterface.OnDismissListener{


    private LotteryCritCodeHandler  mLotteryCritCodeHandler = new LotteryCritCodeHandler(this);
    private OnStateListener mOnFinishListener;
    public LotteryCritCodeDialog(@NonNull Context context) {
        super(context,  R.style.dialogTransparent);
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
        return R.layout.lottery_crit_dialog_layout;
    }

    @Override
    public float setSize() {
        return 0.8f;
    }

    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        /**
         * 开始暴击
         */
        void onStartCrit();


    }


    private static class LotteryCritCodeHandler extends Handler {
        private WeakReference<LotteryCritCodeDialog> reference;   //

        LotteryCritCodeHandler(LotteryCritCodeDialog context) {
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
