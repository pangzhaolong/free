package com.module.integral.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.dn.sdk.bean.integral.IntegralStateListener;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.dn.sdk.utils.IntegralComponent;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.ABSwitch;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralWelfareLayoutBinding;
import com.gyf.immersionbar.ImmersionBar;
import com.module.integral.dialog.BenefitUpgradeDialog;
import com.module.integral.viewModel.IntegralViewModel;

import java.lang.ref.WeakReference;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//限时福利
@Route(path = RouterFragmentPath.Integral.PAGER_INTEGRAL)
public class WelfareActivity extends BaseActivity<IntegralWelfareLayoutBinding, IntegralViewModel> implements Toolbar.OnMenuItemClickListener {

    private static final int DELAY = 1000;

    @Autowired(name = "proxyIntegral")
    public ProxyIntegral proxyIntegral;
    private WelfareHandler mWelfareHandler = new WelfareHandler(this);
    //自动跳转的时间
    private int jumpTime = 0;
    //记录此时系统运行的时间（次数）
    private long experienceStartTime = 0;
    //配置的体验时长
    private long taskPlayTime = ABSwitch.Ins().getScoreTaskPlayTime() * 1000L;
    //记录此时系统运行的时间（次留）
    private long secondStayStartTime = 0;
    private ProxyIntegral mIntegralBean;
    /**
     * 是否处于体验阶段  false 没有默认  ,true 标识开始体验
     */
    private boolean experienceLogo = false;


    private boolean tipsLayoutIsShow = false;


    private void showBenefitUpgradeDialog() {
        BenefitUpgradeDialog benefitUpgradeDialog = new BenefitUpgradeDialog(WelfareActivity.this);
        benefitUpgradeDialog.setStateListener(new BenefitUpgradeDialog.OnStateListener() {
            @Override
            public void onJump() {
                if (benefitUpgradeDialog != null) {
                    benefitUpgradeDialog.dismiss();
                }
                finish();
                ARouter.getInstance()
                        .build(RouterActivityPath.Mine.PAGER_ACTIVITY_WITHDRAWAL)
                        .navigation();
            }
        });
        benefitUpgradeDialog.show(WelfareActivity.this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.integral_welfare_layout;
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(com.module_lottery.R.color.white)
                .navigationBarColor(com.module_lottery.R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        setSupportActionBar(mDataBinding.toolbar);
        ARouter.getInstance().inject(this);
        setData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWelfareHandler != null) {
            mWelfareHandler.removeMessages(0);
            mWelfareHandler.removeMessages(1);
            mWelfareHandler.removeCallbacksAndMessages(null);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (experienceLogo) {
            //记录此时系统运行的时间 (体验阶段)
            if (experienceStartTime == 0) {
                experienceStartTime = SystemClock.elapsedRealtime();
            }
        }

    }

    private void setData() {
        IntegralComponent.getInstance().getIntegral(new IntegralComponent.IntegralHttpCallBack() {
            @Override
            public void onSuccess(ProxyIntegral integralBean) {
                mIntegralBean = integralBean;
                //判断app是否安装
                if (AppUtils.isAppInstalled(integralBean.getPkName())) {
                    refreshSecondStayPageView(integralBean);

                } else {
                    mDataBinding.downloadBt.setText("立即下载");
                    mDataBinding.tipsLayout.setVisibility(View.GONE);
                }
                refreshPage(integralBean);
            }

            @Override
            public void onError(String var1) {

            }

            @Override
            public void onNoTask() {
                ToastUtils.showShort("无积分任务");
            }

        });

        IntegralComponent.getInstance().getSecondStayTask(new IntegralComponent.ISecondStayTaskListener() {
            @Override
            public void onSecondStayTask(ProxyIntegral var1) {
            }

            @Override
            public void onError(String var1) {

            }

            @Override
            public void onNoTask() {

            }
        });
    }


    //刷新次留页面UI
    private void refreshSecondStayPageView(ProxyIntegral integralBean) {
        mDataBinding.downloadBt.setText("打开APP玩1分钟");
        mDataBinding.downloadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToApk(integralBean);
                startTrialStatus();
            }
        });
    }


    //处理试玩状态(来判断返回页面后是否显示下方tips)
    private void startTrialStatus() {
        experienceLogo = true;
        //开始体验后，准备延时通知
        Message message = new Message();
        message.what = 2;
        mWelfareHandler.sendMessageDelayed(message, taskPlayTime);
    }


    //显示下方体验提示布局
    private void showTipsView(ProxyIntegral integralBean) {
        if (integralBean != null && !tipsLayoutIsShow) {
            tipsLayoutIsShow = true;
            Glide.with(WelfareActivity.this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.apkTipsIcon);
            mDataBinding.apkTipsName.setText(integralBean.getAppName() + "安装成功啦");
            mDataBinding.apkTipsTime.setText("赶紧打开体验3S后自动跳转");
            //开始倒计时自动跳转
            startAutomaticCountdown(integralBean);
            mDataBinding.tipsLayout.setVisibility(View.VISIBLE);
            mDataBinding.tipsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jumpToApk(integralBean);
                }
            });
        } else {
            mDataBinding.tipsLayout.setVisibility(View.INVISIBLE);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (experienceLogo) {
            cleanMessage();
            //判断是否完成 弹起完成的dialog
            if ((SystemClock.elapsedRealtime() - experienceStartTime) > taskPlayTime) {
                //满足了体验时间  弹框准备跳转
                showBenefitUpgradeDialog();
                experienceLogo = false;
                experienceStartTime = 0;
            } else {
                //体验时间不到
                showTipsView(mIntegralBean);//体验时间不到
            }
        }

        //打开次留任务回来时间需要大于5秒
        if (secondStayStartTime != 0) {
            if ((SystemClock.elapsedRealtime() - secondStayStartTime) >= 5000) {
                secondStayStartTime = 0;
                //弹起翻倍弹框(红包)
                Intent intent = new Intent(WelfareActivity.this, RpActivityDialog.class);
                startActivity(intent);
            }
        }
    }

    //开始自动跳转倒计时
    private void startAutomaticCountdown(ProxyIntegral integralBean) {
        jumpTime = 3;
        Message message = new Message();
        message.what = 1;
        message.obj = integralBean;
        mWelfareHandler.sendMessage(message);
    }


    /**
     * 跳转打开apk
     */
    public void jumpToApk(ProxyIntegral integralBean) {
        if (integralBean != null) {
            cleanMessage();
            IntegralComponent.getInstance().runIntegralApk(WelfareActivity.this, integralBean);
        }
    }


    private void cleanMessage() {
        mWelfareHandler.removeMessages(0);
        mWelfareHandler.removeMessages(1);
        mWelfareHandler.removeCallbacksAndMessages(null);
    }


    //刷新页面
    private void refreshPage(ProxyIntegral integralBean) {
        Glide.with(this).asDrawable().load(integralBean.getIcon()).into(mDataBinding.apkIcon);
        mDataBinding.apkName.setText(integralBean.getAppName());
        List<View> clickViews = new ArrayList<>();
        clickViews.add(mDataBinding.downloadBt);
        IntegralComponent.getInstance().setIntegralBindView(WelfareActivity.this, integralBean, mDataBinding.adView, clickViews, new IntegralStateListener() {
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
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        if (l != 0) {
                            float value = (float) (Float.valueOf(l1) / Float.valueOf(l)) * 100f;
                            DecimalFormat df = new DecimalFormat("0.00");
                            df.setRoundingMode(RoundingMode.HALF_UP);
                            mDataBinding.downloadBt.setText("下载中 " + df.format(value) + "%");
                        }
                    }
                });

            }

            @Override
            public void onComplete() {
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataBinding.downloadBt.setText("立即安装");
                    }
                });

            }

            //安装完成
            @Override
            public void onInstalled() {
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshSecondStayPageView(integralBean);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    private static class WelfareHandler extends Handler {
        private WeakReference<WelfareActivity> reference;   //

        WelfareHandler(WelfareActivity context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null && !reference.get().isDestroyed()) {
                        if (reference.get().jumpTime <= 0) {
                            reference.get().jumpTime = 0;
                            //倒计时结束，开始自动跳转
                            reference.get().jumpToApk((ProxyIntegral) (msg.obj));
                            reference.get().mDataBinding.tipsLayout.setVisibility(View.INVISIBLE);

                        } else {
                            //继续倒计时，更新UI
                            reference.get().jumpTime = reference.get().jumpTime -= 1;
                            reference.get().mDataBinding.apkTipsTime.setText("赶紧打开体验" + reference.get().jumpTime + "S后自动跳转");
                            Message message = new Message();
                            message.what = 1;
                            message.obj = msg.obj;
                            sendMessageDelayed(message, DELAY);
                        }
                    }
                    break;


                case 2:
                    ToastUtils.showShort("任务已完成");
                    break;


            }
        }
    }


}
