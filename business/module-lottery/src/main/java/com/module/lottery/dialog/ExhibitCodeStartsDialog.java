/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.module.lottery.dialog;

import static com.donews.common.config.CritParameterConfig.CRIT_STATE;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.dn.integral.jdd.IntegralComponent;
import com.dn.integral.jdd.integral.IntegralStateListener;
import com.dn.integral.jdd.integral.ProxyIntegral;
import com.donews.base.base.BaseApplication;
import com.donews.middle.dialog.BaseDialog;
import com.donews.yfsdk.monitor.LotteryAdCheck;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.utils.CriticalModelTool;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.DateManager;
import com.donews.utilslibrary.utils.SPUtils;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module_lottery.R;
import com.module_lottery.databinding.ExhibitCodeDialogLayoutBinding;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//展示生成的抽奖码
public class ExhibitCodeStartsDialog extends BaseDialog<ExhibitCodeDialogLayoutBinding> {
    private Context context;
    private OnStateListener mOnFinishListener;
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);
    private String mGoodsId;
    private int time = 3000;
    private GenerateCodeBean mGenerateCodeBean;
    private ValueAnimator valueProbeAnimator;
    private int mProgressMarginStart;
    private boolean isSendCloseEvent = true;
    private static final String TAG = "ExhibitCodeStartsDialog";

    public ExhibitCodeStartsDialog(Context context, String goodsId, GenerateCodeBean generateCodeBean) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.context = context;
        mGoodsId = goodsId;
        mGenerateCodeBean = generateCodeBean;
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
                    if (ClickDoubleUtil.Companion.isFastClick()) {
                        if (SPUtils.getInformain(CRIT_STATE, 0) == 1) {
                            dismiss();
                        } else {
                            freed();
                            mOnFinishListener.onFinish();
                        }

                    }
                }
            }
        });
        //读取dimen配置参数
        mProgressMarginStart = getContext().getResources().getDimensionPixelSize(R.dimen.lottery_constant_13);
        initProgressBar();
        initView();
        setOnDismissListener(dialog -> {
            if (isSendCloseEvent) {
                if (mGenerateCodeBean.getRemain() <= 0) {
                } else {
                }
            }
            if (valueProbeAnimator != null) {
                valueProbeAnimator.cancel();
            }
        });
    }

    private void setIntegralView(ProxyIntegral integralBean) {
        if (integralBean != null) {
            mDataBinding.critDownload.setVisibility(View.VISIBLE);
            Glide.with(getContext()).asDrawable().load(integralBean.getIcon()).into(mDataBinding.integralIcon);
            mDataBinding.integralName.setText(integralBean.getAppName());
            mDataBinding.integralDescribe.setText("体验下方App一分钟即可解锁暴击模式");

            List<View> clickViews = new ArrayList<>();
            clickViews.add(mDataBinding.integralBt);

            IntegralComponent.getInstance().setIntegralBindView(getContext(), integralBean, mDataBinding.critDownload, clickViews, new IntegralStateListener() {
                @Override
                public void onAdShow() {



                }

                @Override
                public void onAdClick() {
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(long l, long l1) {
                    mDataBinding.integralBt.post(new Runnable() {
                        @Override
                        public void run() {
                            if (l != 0) {
                                float value = (float) (Float.valueOf(l1) / Float.valueOf(l)) * 100f;
                                mDataBinding.integralBt.setText("下载中 " + (int) value + "%");
                            }
                        }
                    });

                }

                @Override
                public void onComplete() {
                    mDataBinding.integralBt.post(new Runnable() {
                        @Override
                        public void run() {
                            mDataBinding.integralBt.setText("立即安装");
                        }
                    });

                }

                @Override
                public void onInstalled() {
                    //安装完成 请求服务器是否开启暴击模式
                    if (mOnFinishListener != null) {
                        mDataBinding.integralBt.post(new Runnable() {
                            @Override
                            public void run() {
                                mDataBinding.integralBt.setText("体验" + ABSwitch.Ins().getScoreTaskPlayTime() + "秒");
                            }
                        });
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onRewardVerify() {

                    if (mOnFinishListener != null) {
                        mDataBinding.integralBt.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BaseApplication.getInstance(), "激活成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        mOnFinishListener.onStartCritMode(mGenerateCodeBean, integralBean);
                    }
                }

                @Override
                public void onRewardVerifyError(String s) {
                    mDataBinding.integralBt.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BaseApplication.getInstance(), "任务创建失败" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, true);


        }
    }


    private void initProgressBar() {
        mDataBinding.setCodeBean(mGenerateCodeBean);
        int schedule = 0;
        int randomValue = 0;
        //抽奖码生成成功回调
        if (mGenerateCodeBean != null) {
            schedule = 5 - mGenerateCodeBean.getRemain();
            schedule = mDataBinding.includeProgressBar.progressBar.getMax() / 5 * (schedule);
            if (schedule != mDataBinding.includeProgressBar.progressBar.getMax()) {
                randomValue = generateRandomNumber(60);
            } else {
                randomValue = 0;
            }
        } else {
            randomValue = generateRandomNumber(55);
        }
        startProgressBar(schedule + randomValue);
    }

    void initView() {
        if (mGenerateCodeBean.getRemain() == 0) {
            mDataBinding.jsonAnimationLayout.setVisibility(View.GONE);
            mDataBinding.hintLayout.setVisibility(View.VISIBLE);
            automaticJump();
        } else {
            mDataBinding.jsonAnimationLayout.setVisibility(View.VISIBLE);
            mDataBinding.hintLayout.setVisibility(View.GONE);

            mDataBinding.jsonAnimation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSendCloseEvent = false;
                    dismiss();
                    if (mOnFinishListener != null) {
                        mOnFinishListener.onLottery();
                    }
                }
            });

            //手
            mDataBinding.jsonAnimationHand.setImageAssetsFolder("images");
            mDataBinding.jsonAnimationHand.setAnimation("lottery_finger.json");
            mDataBinding.jsonAnimationHand.loop(true);
            mDataBinding.jsonAnimationHand.playAnimation();

            //礼盒
            mDataBinding.giftBoxOff.setImageAssetsFolder("images");
            mDataBinding.giftBoxOff.setAnimation("gift_box_off.json");
            mDataBinding.giftBoxOff.loop(true);
            mDataBinding.giftBoxOff.playAnimation();
        }
        //开启了暴击模式
        if (ABSwitch.Ins().getOpenCritModel()) {
            Logger.d(TAG + "开启了暴击模式");
            //每天参与次数
            if (DateManager.getInstance().isAllowCritical()) {
                Logger.d(TAG + "可以继续参与");
                //判断是否打开新手模式
                if (ABSwitch.Ins().isOpenCritModelByNewUser()) {
                    Logger.d(TAG + "新手模式");
                    //判断是否处于新手阶段
                    if (CriticalModelTool.isNewUser()) {
                        Logger.d(TAG + "新手阶段");
                        //普通次数模式
                        numberModel();
                    } else {
                        //老用户模式
                        oldUserModel();
                        Logger.d(TAG + "老用户模式 one");
                    }
                } else {
                    //老用户模式
                    oldUserModel();
                    Logger.d(TAG + "老用户模式 two");
                }
            } else {
                //不能触发了 每天参与的次数用完
                mDataBinding.critDraw.setVisibility(View.GONE);
                mDataBinding.critDownload.setVisibility(View.GONE);
            }
        } else {
            mDataBinding.critDraw.setVisibility(View.GONE);
            mDataBinding.critDownload.setVisibility(View.GONE);
        }
    }


    private void oldUserModel() {
        int number = LotteryAdCheck.INSTANCE.getCriticalModelLotteryNumber();
        //判断积分墙是否开启
        if (ABSwitch.Ins().isOpenScoreModelCrit() && number == 0) {
            //积分下载模式打开的 ，获取是否有积分任务
            //新用户并且抽奖次数未达到开启暴击条件
            CriticalModelTool.getScenesSwitch(new CriticalModelTool.IScenesSwitchListener() {
                @Override
                public void onIntegralNumber(ProxyIntegral integralBean) {
                    if (!getOwnerActivity().isDestroyed() && !getOwnerActivity().isFinishing()) {
                        if (integralBean != null) {
                            //下载解锁
                            setIntegralView(integralBean);
                        } else {
                            //普通次数模式
                            numberModel();
                        }
                    }
                }
            });
        } else {
            //普通次数模式
            numberModel();
        }
    }

    //次数模式
    private void numberModel() {
        //普通次数模式
        mDataBinding.critDraw.setVisibility(View.VISIBLE);
        mDataBinding.critDownload.setVisibility(View.GONE);
        //总共需要抽多少个抽奖码开始暴击模式
        int sumNumber;
        //已经参与的次数
        int participateNumber = LotteryAdCheck.INSTANCE.getCriticalModelLotteryNumber();
        if (CriticalModelTool.isNewUser()) {
            sumNumber = ABSwitch.Ins().getOpenCritModelByNewUserCount();
        } else {
            sumNumber = ABSwitch.Ins().getOpenCritModelByOldUserCount();
        }
        if ((sumNumber - participateNumber) == 0) {
            mDataBinding.critDraw.setVisibility(View.GONE);
            //暴击模式开启修改之前的状态
            mOnFinishListener.onChangeState();
        } else {
            mDataBinding.critDraw.setVisibility(View.VISIBLE);
            mDataBinding.numberCode.setText((sumNumber - participateNumber) < 0 ? 0 + "" : (sumNumber - participateNumber) + "");
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
        Logger.d("================== progress " + progress);
        //停止之前的循环动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, progress);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //设置显示的百分比
                showProgressBarIcon(value);
                float percentage = value / mDataBinding.includeProgressBar.progressBar.getMax() * 100;

//                DecimalFormat decimalFormat = new DecimalFormat("");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//                String text = decimalFormat.format(percentage);//format 返回的是字符串
                if (value > mDataBinding.includeProgressBar.progressBar.getMax() - 10) {
                    mDataBinding.progressReminder.setText("明日10点开奖");
                } else {
                    mDataBinding.progressReminder.setText("超过" + (int) percentage + "%的用户");
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


    //设置显示在进度条上的icon
    private void showProgressBarIcon(float schedule) {
        if (schedule > 0 && schedule < 100) {
            mDataBinding.includeProgressBar.round01.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
        }
        if (schedule > 100 && schedule < 200) {
            mDataBinding.includeProgressBar.round01.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round02.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
        }
        if (schedule > 200 && schedule < 300) {
            mDataBinding.includeProgressBar.round01.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round02.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round03.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
        }
        if (schedule > 300 && schedule < 400) {
            mDataBinding.includeProgressBar.round01.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round02.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round03.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round04.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
        }
        if (schedule > 400) {
            mDataBinding.includeProgressBar.round01.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round02.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round03.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round04.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
            mDataBinding.includeProgressBar.round05.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.hook_icon));
        }
    }


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

        void onLottery();

        void onChangeState();


        /**
         * 开启暴击模式
         */
        void onStartCritMode(GenerateCodeBean generateCodeBean, ProxyIntegral integralBean);

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