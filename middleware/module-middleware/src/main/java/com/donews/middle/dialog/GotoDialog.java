package com.donews.middle.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.donews.middle.R;
import com.donews.middle.databinding.MiddleGotoDialogBinding;
import com.donews.middle.databinding.MiddleLoadAdErrorDialogBinding;
import com.donews.middle.utils.LottieUtil;

public class GotoDialog extends BaseDialog<MiddleGotoDialogBinding> {

    private dismissListener mListener;

    public GotoDialog(Context context, dismissListener listener) {
        super(context, R.style.dialogTransparent);
        mListener = listener;
    }

    @Override
    public int setLayout() {
        return R.layout.middle_goto_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
    }

    @Override
    public float setSize() {
        return 0.8f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        LottieUtil.initLottieView(mDataBinding.middleGotoLav, "middle_arrow.json");

        new Handler().postDelayed(() -> {
            if (mListener != null) {
                mListener.onDismiss();
            }
            dismiss();
        }, 1300);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        LottieUtil.cancelLottieView(mDataBinding.middleGotoLav);
    }

    public interface dismissListener {
        void onDismiss();
    }
}