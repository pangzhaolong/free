package com.module.lottery.dialog;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module_lottery.R;
import com.module_lottery.databinding.ExhibitCodeDialogLayoutBinding;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Random;
//展示生成的抽奖码
public class ExhibitCodeStartsDialog extends BaseDialog<ExhibitCodeDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private String mGoodsId;
    private LotteryCodeBean mLotteryCodeBean;
    private int time = 3000;
    private GenerateCodeBean mGenerateCodeBean;
    private  int mProgressMarginStart;
    public ExhibitCodeStartsDialog(Context context, String goodsId, LotteryCodeBean lotteryCodeBean, GenerateCodeBean generateCodeBean) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
        mGoodsId = goodsId;
        mGenerateCodeBean = generateCodeBean;
        this.mLotteryCodeBean = lotteryCodeBean;
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 2;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    @Override
    public int setLayout() {
        return R.layout.exhibit_code_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFinishListener != null) {
                    //放在倒计时结束后再次打开
                    freed();
                    mOnFinishListener.onFinish();
                }
            }
        });
        //读取dimen配置参数
        mProgressMarginStart = getContext().getResources().getDimensionPixelSize(R.dimen.lottery_constant_13);
        initProgressBar();
        initView();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (valueProbeAnimator != null) {
                    valueProbeAnimator.cancel();
                }
            }
        });
    }


    private void initProgressBar() {
        mDataBinding.setCodeBean(mGenerateCodeBean);
        int schedule = 0;
        int randomValue = 0;
        //抽奖码生成成功回调
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() != 0) {
            schedule = mLotteryCodeBean.getCodes().size();
            schedule = mDataBinding.includeProgressBar.progressBar.getMax() / 5 * (schedule);
            if (schedule != mDataBinding.includeProgressBar.progressBar.getMax()) {
                randomValue = generateRandomNumber(50);
            } else {
                randomValue = 0;
            }
        } else {
            randomValue = generateRandomNumber(55);
        }
        startProgressBar(schedule + randomValue);
    }

    void initView() {
        if (mLotteryCodeBean.getCodes().size() >= 5) {
            mDataBinding.jsonAnimationLayout.setVisibility(View.GONE);
            mDataBinding.hintLayout.setVisibility(View.VISIBLE);
            automaticJump();
        } else {
            mDataBinding.jsonAnimationLayout.setVisibility(View.VISIBLE);
            mDataBinding.hintLayout.setVisibility(View.GONE);

            mDataBinding.jsonAnimation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mOnFinishListener != null) {
                        mOnFinishListener.onLottery();
                    }
                }
            });
            // 增加中奖率
            mDataBinding.jsonAnimation.setImageAssetsFolder("images");
            mDataBinding.jsonAnimation.setAnimation("probability_button.json");
            mDataBinding.jsonAnimation.loop(true);
            mDataBinding.jsonAnimation.playAnimation();

            //手
            mDataBinding.jsonAnimationHand.setImageAssetsFolder("images");
            mDataBinding.jsonAnimationHand.setAnimation("hand.json");
            mDataBinding.jsonAnimationHand.loop(true);
            mDataBinding.jsonAnimationHand.playAnimation();

            //礼盒
            mDataBinding.giftBoxOff.setImageAssetsFolder("images");
            mDataBinding.giftBoxOff.setAnimation("gift_box_off.json");
            mDataBinding.giftBoxOff.loop(true);
            mDataBinding.giftBoxOff.playAnimation();
        }
    }


    private void automaticJump() {
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    private void freed() {
        mLotteryHandler.removeMessages(1);
        mLotteryHandler.removeCallbacksAndMessages(null);
        mLotteryHandler.removeMessages(0);
    }

    @Override
    public float setSize() {
        return 0.8f;
    }


    //在特定区间产生随机数
    @SuppressLint("LongLogTag")
    private int generateRandomNumber(int mark) {
        int value = new Random().nextInt(11) + mark;//{3    6}
        return value;
    }

    private void setProgressVariety() {
        float progressWidth = mDataBinding.includeProgressBar.progressBar.getWidth();
        if (progressWidth != 0) {
            float maxValue = mDataBinding.includeProgressBar.progressBar.getMax();
            float progress = mDataBinding.includeProgressBar.progressBar.getProgress();
            if (progress == 0) {
                progress = 1;
            }
            float x = progressWidth / (maxValue / progress);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mDataBinding.reminderIcon.getLayoutParams();
            layoutParams.leftMargin = (int) x + mProgressMarginStart;
            mDataBinding.reminderIcon.setLayoutParams(layoutParams);
        }
    }


    //进度条设置动画
    public void startProgressBar(int progress) {
        //停止之前的循环动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, progress);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //设置显示的百分比

                float percentage = value / mDataBinding.includeProgressBar.progressBar.getMax() * 100;

                DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String text = decimalFormat.format(percentage);//format 返回的是字符串
               if(value>mDataBinding.includeProgressBar.progressBar.getMax()-10){
                   mDataBinding.progressReminder.setText("明日10点开奖");
               }else{
                   mDataBinding.progressReminder.setText("超过" + text + "%的用户");
               }
                mDataBinding.includeProgressBar.progressBar.setProgress((int) value);
                //设置进度条上的百分比
                setProgressVariety();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (progress >= mDataBinding.includeProgressBar.progressBar.getMax()) {
                    //礼盒开
                    mDataBinding.giftBoxOff.setImageAssetsFolder("images");
                    mDataBinding.giftBoxOff.setAnimation("gift_box_open.json");
                    mDataBinding.giftBoxOff.loop(true);
                    mDataBinding.giftBoxOff.playAnimation();
                } else {
//                    完成后执行探头动画
                    startProbeAnimation(progress);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

    }


    ValueAnimator valueProbeAnimator;

    //进度条设置动画
    public void startProbeAnimation(int progress) {
        if (valueProbeAnimator != null) {
            valueProbeAnimator.cancel();
        }
        valueProbeAnimator = ValueAnimator.ofFloat(progress, progress - 20, progress);
        valueProbeAnimator.setDuration(960);
        valueProbeAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueProbeAnimator.setInterpolator(new AccelerateInterpolator());
        valueProbeAnimator.setRepeatCount(Animation.INFINITE);
        valueProbeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //设置显示的百分比
                mDataBinding.includeProgressBar.progressBar.setProgress((int) value);
            }
        });
        valueProbeAnimator.start();

    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);


    }




    public void setStateListener(OnStateListener l) {
        mOnFinishListener = l;
    }

    public interface OnStateListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onJumpAd();

        void onLottery();
    }


    private static class LotteryHandler extends Handler {
        private WeakReference<ExhibitCodeStartsDialog> reference;   //

        LotteryHandler(ExhibitCodeStartsDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && reference.get().mOnFinishListener != null) {
                        if (reference.get().time > 0) {
                            reference.get().mDataBinding.countdownHint.setText("恭喜！ 已解锁本商品最大中奖率" + "(" + reference.get().time / 1000 + ")");
                            reference.get().time = reference.get().time - 1000;
                            reference.get().automaticJump();
                        } else {
                            reference.get().mOnFinishListener.onFinish();
                        }
                    }
                    break;
                case 2:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.closure.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}