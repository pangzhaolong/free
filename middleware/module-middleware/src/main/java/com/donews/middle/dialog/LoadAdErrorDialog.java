package com.donews.middle.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.donews.middle.R;
import com.donews.middle.databinding.MiddleLoadAdErrorDialogBinding;

public class LoadAdErrorDialog extends BaseDialog<MiddleLoadAdErrorDialogBinding> {

    private RetryListener mListener;

    public LoadAdErrorDialog(Context context, RetryListener listener) {
        super(context, R.style.dialogTransparent);
        mListener = listener;
    }

    @Override
    public int setLayout() {
        return R.layout.middle_load_ad_error_dialog;
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

        mDataBinding.middleLoadAdErrCloseIv.setOnClickListener(v -> {
            Activity activity = this.getOwnerActivity();
            if (activity == null || activity.isFinishing() || activity.isDestroyed() || mListener == null) {
                dismiss();
                return;
            }
            mListener.onClose();
        });

        mDataBinding.middleLoadAdErrRetry.setOnClickListener(v -> {
            Activity activity = this.getOwnerActivity();
            if (activity == null || activity.isFinishing() || activity.isDestroyed() || mListener == null) {
                dismiss();
                return;
            }
            mListener.onClose();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface RetryListener {
        void onRetry();

        void onClose();
    }
}