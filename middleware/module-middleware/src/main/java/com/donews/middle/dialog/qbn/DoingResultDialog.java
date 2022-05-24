package com.donews.middle.dialog.qbn;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.donews.middle.R;
import com.donews.middle.bean.globle.TurntableBean;
import com.donews.middle.databinding.TurntableDoingDialogLayoutBinding;
import com.donews.middle.dialog.BaseDialog;

import java.lang.ref.WeakReference;

//活动结果
public class DoingResultDialog extends BaseDialog<TurntableDoingDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private WeakDoingResult weakDoingResult = new WeakDoingResult(this);
    private OnStateListener mOnFinishListener;
    TurntableBean.ItemsDTO mPrize;
    //直接指定金币数量
    int count = 0;
    //需要显示的icon资源id
    int iconRes = 0;

    public DoingResultDialog(@NonNull Context context, TurntableBean.ItemsDTO itemsDTO) {
        super(context, R.style.dialogTransparent);
        mPrize = itemsDTO;
    }

    public DoingResultDialog(@NonNull Context context, int count, int iconRes) {
        super(context, R.style.dialogTransparent);
        mPrize = null;
        this.count = count;
        this.iconRes = iconRes;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        weakDoingResult.sendMessageDelayed(message, 1000);
        setOnDismissListener(this);
        if (mPrize != null) {
            mDataBinding.award.setText(mPrize.getTitle());
        } else {
            mDataBinding.award.setText("" + count);
        }
        mDataBinding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mPrize != null) {
            mDataBinding.ivLocalIcon.setVisibility(View.GONE);
            Glide.with(getContext()).load(mPrize.getIcon()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(mDataBinding.icon);
        } else {
            mDataBinding.ivLocalIcon.setVisibility(View.VISIBLE);
            mDataBinding.ivLocalIcon.setImageResource(iconRes);
        }

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
