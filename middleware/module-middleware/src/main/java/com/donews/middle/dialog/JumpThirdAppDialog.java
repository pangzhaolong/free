package com.donews.middle.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.donews.middle.R;
import com.donews.middle.databinding.MiddleJumpThirdAppDialogBinding;
import com.donews.middle.listener.JumpThirdAppListener;

public class JumpThirdAppDialog extends BaseDialog<MiddleJumpThirdAppDialogBinding> {

    private final int mSrc;
    private final JumpThirdAppListener mListener;

    private CountDownTimer mCountDownTimer;

    public JumpThirdAppDialog(Context context, int src, JumpThirdAppListener listener) {
        super(context, R.style.dialogTransparent);
        mSrc = src;
        mListener = listener;
    }

    @Override
    public int setLayout() {
        return R.layout.middle_jump_third_app_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        initView();
    }

    @Override
    public float setSize() {
        return 0.7f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {

        switch (mSrc) {
            case 1:
                mDataBinding.middleJumpRightIconIv.setImageResource(R.drawable.middle_item_tb);
                mDataBinding.middleJumpToTv.setText("正在为您跳转 淘宝");
                break;
            case 2:
                mDataBinding.middleJumpRightIconIv.setImageResource(R.drawable.middle_item_pdd);
                mDataBinding.middleJumpToTv.setText("正在为您跳转 拼多多");
                break;
            case 3:
                mDataBinding.middleJumpRightIconIv.setImageResource(R.drawable.middle_item_jd);
                mDataBinding.middleJumpToTv.setText("正在为您跳转 京东");
                break;
        }

        mCountDownTimer = new CountDownTimer(3000, 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                mDataBinding.middleJumpGoTv.setText(String.format("确认前往(%ds)", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                dismissEx(false);
            }
        };
        mCountDownTimer.start();

        mDataBinding.middleJumpCloseIv.setOnClickListener(v -> dismissEx(true));
        mDataBinding.middleJumpGoTv.setOnClickListener(v -> dismissEx(false));
    }

    private void dismissEx(boolean isClose) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mListener != null) {
            if (isClose) {
                mListener.onClose();
            } else {
                mListener.onGo();
            }
            dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}