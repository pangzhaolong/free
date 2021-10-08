package com.dn.sdk.dialog;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dn.sdk.R;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public class LoadingDialog extends BaseFragmentDialog {
    /**
     * 加载等待时长
     */
    public int loadingTime = 6 * 1000;
    /**
     * 是否显示关闭按钮，可以直接关闭加载框
     */
    private boolean showClose = false;

    private CountDownTimer countDownTimer;

    /**
     * 加载等待时的提示信息
     */
    private String description;

    public LoadingDialog() {
        super(false, false);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }


    @Override
    protected void initView() {

        // 关闭按钮
        ImageView imgClose = $(R.id.img_close);
        imgClose.setVisibility(showClose ? View.VISIBLE : View.INVISIBLE);

        imgClose.setOnClickListener(v -> {
            if (closeListener != null) {
                closeListener.onClose();
            }
            disMissDialog();
        });

        //等待提示
        TextView tvDescription = $(R.id.tv_loading_msg);
        if (!TextUtils.isEmpty(description)) {
            tvDescription.setText(description);
        }


        //加载动画
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.base_dialog_loading_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        $(R.id.loading).startAnimation(operatingAnim);


        //加载倒计时
        countDownTimer = new CountDownTimer(loadingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                if (closeListener != null) {
                    closeListener.onClose();
                }
                disMissDialog();

            }
        }.start();

    }

    /**
     * @param description
     * @return
     */
    public LoadingDialog setDescription(String description) {
        this.description = description;
        return this;
    }

    public LoadingDialog isShowClose() {
        showClose = true;
        return this;
    }

    /**
     * 设置最长等待时间
     *
     * @param seconds 秒
     */
    public LoadingDialog setLoadingTime(int seconds) {
        this.loadingTime = seconds * 1000;
        return this;
    }

    /**
     * 设置弹窗背景是否变暗
     *
     * @param backgroundDim true:弹窗背景变暗
     * @return
     */
    @Override
    public LoadingDialog setBackgroundDim(boolean backgroundDim) {
        super.setBackgroundDim(backgroundDim);
        return this;
    }

    /**
     * 点击返回键是否可以关闭弹窗
     *
     * @param dismissOnBackPressed true:返回键可以关闭弹窗
     * @return
     */
    @Override
    public LoadingDialog setDismissOnBackPressed(boolean dismissOnBackPressed) {
        super.setDismissOnBackPressed(dismissOnBackPressed);
        return this;
    }

    /**
     * 点击弹窗外部区域，弹窗是否消失
     *
     * @param dismissOnTouchOutside true：点击弹窗外部弹窗创效
     * @return
     */
    @Override
    public LoadingDialog setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        super.setDismissOnTouchOutside(dismissOnTouchOutside);
        return this;
    }

    /**
     * dialog 关闭监听
     *
     * @param closeListener listener
     * @return
     */
    @Override
    public LoadingDialog setCloseListener(DialogCloseListener closeListener) {
        super.setCloseListener(closeListener);
        return this;
    }

    /**
     * 返回键关闭弹窗
     */
    @Override
    public void onBackPressDismissBefore() {
        super.onBackPressDismissBefore();

        //取消倒计时
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }


    /**
     * 在倒计时没有结束的时候，取消倒计时
     */
    public void dismissCusDialog() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        disMissDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
