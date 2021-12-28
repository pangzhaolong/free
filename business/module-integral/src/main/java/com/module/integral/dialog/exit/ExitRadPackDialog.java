package com.module.integral.dialog.exit;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.module_integral.R;
import com.example.module_integral.databinding.BenefitUpgradeLayoutBinding;
import com.example.module_integral.databinding.DialogExitRadPackLayoutBinding;
import com.module.lottery.dialog.BaseDialog;

import java.lang.ref.WeakReference;

/**
 * 退出拦截红包弹窗
 */
public class ExitRadPackDialog extends BaseDialog<DialogExitRadPackLayoutBinding> implements DialogInterface.OnDismissListener {
    private OnSurListener mOnFinishListener;

    public ExitRadPackDialog(Context context) {
        super(context, R.style.dialogTransparent);
    }


    @Override
    public int setLayout() {
        return R.layout.dialog_exit_rad_pack_layout;
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
        setOnDismissListener(this);
        initView();
    }


    private void initView() {
        mDataBinding.closure.setOnClickListener(v -> dismiss());
        mDataBinding.integralTvJj.setOnClickListener(v->{
            dismiss();
        });

        mDataBinding.integralTvOk.setOnClickListener(v->{
            if (mOnFinishListener != null) {
                mOnFinishListener.onJump();
            }
        });
    }


    public void setStateListener(OnSurListener l) {
        mOnFinishListener = l;
    }

    public interface OnSurListener {
        void onJump();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
    }
}
