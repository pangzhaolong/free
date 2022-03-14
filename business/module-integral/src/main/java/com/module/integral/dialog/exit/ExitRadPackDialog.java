package com.module.integral.dialog.exit;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.donews.base.utils.ToastUtil;
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

    private ScaleAnimation mScaleAnimation;

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
        mDataBinding.integralTvJj.setOnClickListener(v -> {
            dismiss();
        });

        mDataBinding.integralTvOk.setOnClickListener(v -> {
            if (mOnFinishListener != null) {
                ToastUtil.show(getContext(), "确定点击");
                mOnFinishListener.onJump();
            }
        });

        //呼吸动画
        if (mScaleAnimation == null) {
            mScaleAnimation = new ScaleAnimation(1.15f, 0.9f, 1.15f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mScaleAnimation.setInterpolator(new LinearInterpolator());
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(Animation.INFINITE);
            mScaleAnimation.setDuration(1000);
            mDataBinding.integrIbLing.startAnimation(mScaleAnimation);
        }
        //红包动画
        LottieAnimationView lottieAnimationView = mDataBinding.integrBbAnim;
        lottieAnimationView.setImageAssetsFolder("images");
        lottieAnimationView.setAnimation("integr_anim_hb.json");
        lottieAnimationView.loop(true);
        lottieAnimationView.playAnimation();
        //背景的光的动画
        LottieAnimationView bgAnimLot = mDataBinding.integralLtv;
        bgAnimLot.setImageAssetsFolder("images");
        bgAnimLot.setAnimation("exit_progress_lottie.json");
        bgAnimLot.loop(true);
        bgAnimLot.playAnimation();
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
