package com.module.integral.dialog.exit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.VibrateUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.utils.CommonAnimationUtils;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralScheduleDialogLayoutBinding;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.dialog.BaseDialog;
import com.module.lottery.dialog.ExclusiveLotteryCodeDialog;
import com.module.lottery.dialog.LogToWeChatDialog;

import java.lang.ref.WeakReference;

/**
 * 退出拦截弹框(点击过下载)
 */
public class ExitProgressInterceptDialog extends BaseDialog<IntegralScheduleDialogLayoutBinding> {
    ExitProgressHandler mExitProgressHandle;

    public ExitProgressInterceptDialog(@NonNull Context context) {
        super(context, R.style.dialogTransparent);
    }
    private OnFinishListener mOnFinishListener;

    @Override
    public int setLayout() {
        return R.layout.integral_schedule_dialog_layout;
    }

    @Override
    public float setSize() {
        return 1.0f;
    }

    @Override
    public void show(Context context) {
        super.show(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExitProgressHandle = new ExitProgressHandler(this);
        initView();
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mExitProgressHandle.sendMessageDelayed(message, 1000);
        initLottie(mDataBinding.bgAnimation,"exit_progress_lottie.json");
    }
    private void initLottie(LottieAnimationView view, String json) {
        if ((view != null && !view.isAnimating())) {
            view.setImageAssetsFolder("images");
            view.clearAnimation();
            view.setAnimation(json);
            view.loop(true);
            view.playAnimation();
        }
    }
    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }


    public interface OnFinishListener {
        //继续体验
        public void onExperience();

    }


    private void initView() {
        //点击试玩
        mDataBinding.experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnFinishListener!=null){
                    mOnFinishListener.onExperience();
                }
                dismiss();
            }
        });
        //点击关闭按钮
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //点击放弃
        mDataBinding.quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDataBinding.experience.startAnimation(CommonAnimationUtils.setScaleAnimation(1000));
    }

    private static class ExitProgressHandler extends Handler {
        private WeakReference<ExitProgressInterceptDialog> reference;   //

        ExitProgressHandler(ExitProgressInterceptDialog context) {
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
