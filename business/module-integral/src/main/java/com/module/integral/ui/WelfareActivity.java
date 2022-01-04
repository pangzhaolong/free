package com.module.integral.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
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
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.service.CritLotteryService;
import com.example.module_integral.R;
import com.example.module_integral.databinding.IntegralWelfareLayoutBinding;
import com.gyf.immersionbar.ImmersionBar;
import com.module.integral.bean.IntegralDownloadStateDean;
import com.module.integral.dialog.BenefitUpgradeDialog;
import com.module.integral.dialog.exit.ExitProgressInterceptDialog;
import com.module.integral.dialog.exit.ExitRadPackDialog;
import com.module.integral.viewModel.IntegralViewModel;
import com.module.lottery.ui.BaseParams;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//限时福利
@Route(path = RouterFragmentPath.Integral.PAGER_INTEGRAL)
public class WelfareActivity extends BaseActivity<IntegralWelfareLayoutBinding, IntegralViewModel> implements Toolbar.OnMenuItemClickListener {
    private static final String TAG = "WelfareActivity";
    private static final int DELAY = 1000;

    private WelfareHandler mWelfareHandler = new WelfareHandler(this);
    //自动跳转的时间
    private int jumpTime = 0;
    //配置的体验时长
    private long taskPlayTime = ABSwitch.Ins().getScoreTaskPlayTime() * 1000L;
    private ProxyIntegral mIntegralBean;
    IntegralDownloadStateDean mIntegralDownloadStateDean;
    private Timer mTimer;
    private boolean tipsLayoutIsShow = false;
    private int mStartTime = -1;
    /**
     * 返回拦截的dialog是否显示
     */
    private boolean dialogShow = false;
    /**
     * 应用是否当前可见
     */
    boolean mReception = false;


    /**
     * 任务状态 false 未完成 true 完成
     */
    boolean mTaskState = false;

    /**
     * mClickStatus 用来判断返回拦截使用
     * mClickStatus false 表示未安装
     * mClickStatus true 表示安装
     */
    private boolean mClickInstalledStatus = false;


    private boolean ifTimerRun = false;

    private void showBenefitUpgradeDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });
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
        setObserveList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mWelfareHandler != null) {
            mWelfareHandler.removeMessages(0);
            mWelfareHandler.removeMessages(1);
            mWelfareHandler.removeCallbacksAndMessages(null);
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
    }


    //安装的情况下
    private void refreshSecondStayPageView(ProxyIntegral integralBean) {
        mDataBinding.downloadBt.setText("打开APP玩1分钟");
        mDataBinding.downloadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExperienceApp(integralBean);
            }
        });
    }

    //打开体验app
    private void startExperienceApp(ProxyIntegral integralBean) {
        jumpToApk(integralBean);
        //开始一分钟(中台配置)倒计时任务
        startTask(integralBean);
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
        mReception = true;
        //判断倒计时是否在运行
        if (ifTimerRun && mStartTime > 0 && !tipsLayoutIsShow) {
            //在运行，体验时间不足，显示下方tips
            unTaskCompleted();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReception = false;
    }

    /**
     * 任务完成
     */
    private void taskCompleted() {
        Logger.d(TAG + "执行跳转");
        if (mIntegralDownloadStateDean != null && mIntegralDownloadStateDean.getHandout()) {
            cleanMessage();
            //满足了体验时间  弹框准备跳转
            showBenefitUpgradeDialog();
        }
    }


    /**
     * 未任务完成
     */
    private void unTaskCompleted() {
        //体验时间不到
        showTipsView(mIntegralBean);//体验时间不到
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
                            mDataBinding.downloadBt.setText("下载中 " + (int) value + "%");
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
                Logger.d(TAG + "安装完成");
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataBinding.downloadBt.setText("安装完成");
                    }
                });

            }

            @Override
            public void onError(Throwable throwable) {

            }


            //激活成功
            @Override
            public void onRewardVerify() {
                Logger.d(TAG + "激活成功");
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataBinding.downloadBt.setText("激活完成");
                    }
                });
                mClickInstalledStatus = true;
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshSecondStayPageView(integralBean);
                        startTask(integralBean);
                    }
                });
            }

            @Override
            public void onRewardVerifyError(String s) {
                Logger.d(TAG + " 任务 激活失败" + s);
                mDataBinding.downloadBt.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataBinding.downloadBt.setText("激活失败" + s);
                    }
                });
            }
        }, true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            returnIntercept();
        }
        return super.onOptionsItemSelected(item);

    }


    /**
     * 请求服务器下载的任务是否完成
     */
    private void requestServiceData(ProxyIntegral integralBean) {
        Map<String, String> params = BaseParams.getMap();
        params.put("req_id", integralBean.getWallRequestId());
        params.put("type", "1");
        mViewModel.getDownloadStatus(CritLotteryService.INTEGRAL_REWARD, params);
    }

    public void setObserveList() {
        mViewModel.getMutableLiveData().observe(this, IntegralDownloadStateDean -> {
            if (IntegralDownloadStateDean == null || !IntegralDownloadStateDean.getHandout()) {
                if (IntegralDownloadStateDean != null) {
                    Logger.d(TAG + "激活失败");
                }
                return;
            }
            Logger.d(TAG + "==" + IntegralDownloadStateDean.getHandout());
            //任务成功
            mIntegralDownloadStateDean = IntegralDownloadStateDean;
            if (!ifTimerRun) {
                taskCompleted();
            }
        });
    }


    private void startTask(ProxyIntegral integralBean) {
        if (mTimer != null) {
            Logger.d(TAG + "不能重复创建");
            return;
        }
        //初始化暴击体验时长
        mStartTime = ABSwitch.Ins().getScoreTaskPlayTime();
        if (mTimer != null) {
            mTimer.cancel();
            ifTimerRun = false;
            mTimer = null;
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ifTimerRun = true;
                //只有应用不在前台才会继续倒计时
                if (!mReception) {
                    if (mStartTime > 0) {
                        mStartTime = mStartTime - 1;
                        if (mStartTime == (ABSwitch.Ins().getScoreTaskPlayTime() / 2)) {
                            //请求服务器处理结果
                            Logger.d(TAG + "请求服务器获取结果");
                            if (integralBean != null) {
                                requestServiceData(integralBean);
                            }

                        }
                    } else {
                        Logger.d(TAG + "可以开始暴击模式了");
                    }
                } else {
                    if (mStartTime <= 0 && mReception) {
                        mTimer.cancel();
                        mTimer = null;
                        ifTimerRun = false;
                        //倒计时结束 任务完成
                        if (mIntegralDownloadStateDean == null || !mIntegralDownloadStateDean.getHandout()) {
                            //请求服务器处理结果
                            mStartTime = 0;
                            if (integralBean != null) {
                                Logger.d(TAG + "上次没有请求到或者请求失败，重新请求");
                                requestServiceData(integralBean);
                            }
                        } else {
                            Logger.d(TAG + "上次请求成功");
                            taskCompleted();
                        }

                    } else {
                        Logger.d(TAG + "在前台，体验体验时间不足");
                    }
                }
            }
        }, 0, 1000);
    }


    private void returnIntercept() {
        if (dialogShow || mTaskState) {
            finish();
        }
        //是否完成了安装
        if (mClickInstalledStatus) {
            showExitProgressInterceptDialog();
        } else {
            showReceiveDialog();
        }
        dialogShow = true;
    }


    @SuppressLint("ResourceType")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnIntercept();
        }
        return true;
    }

    //显示进度条的弹框
    private void showExitProgressInterceptDialog() {
        mTaskState = true;
        ExitProgressInterceptDialog exitProgressInterceptDialog = new ExitProgressInterceptDialog(WelfareActivity.this);
        exitProgressInterceptDialog.setFinishListener(new ExitProgressInterceptDialog.OnFinishListener() {
            @Override
            public void onExperience() {
                if (mIntegralBean != null) {
                    startExperienceApp(mIntegralBean);
                }
            }
        });
        exitProgressInterceptDialog.show(WelfareActivity.this);


    }

    //显示立刻领取dialog
    private void showReceiveDialog() {
        ExitRadPackDialog exitRadPackDialog = new ExitRadPackDialog(WelfareActivity.this);
        exitRadPackDialog.setStateListener(new ExitRadPackDialog.OnSurListener() {
            @Override
            public void onJump() {
                exitRadPackDialog.dismiss();
                mDataBinding.downloadBt.performClick();
            }
        });
        exitRadPackDialog.show(WelfareActivity.this);
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


            }
        }
    }


}
