package com.donews.main.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.NetworkChanageEvnet;
import com.dn.sdk.listener.interstitialfull.SimpleInterstitialFullListener;
import com.dn.sdk.listener.rewardvideo.SimpleRewardVideoListener;
import com.dn.sdk.listener.splash.IAdSplashListener;
import com.dn.sdk.listener.splash.SimpleSplashListener;
import com.donews.base.base.AppManager;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.LoginHelp;
import com.donews.main.R;
import com.donews.main.databinding.MainActivitySplashBinding;
import com.donews.main.dialog.PersonGuideDialog;
import com.donews.main.utils.SplashUtils;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.adutils.DnSdkInit;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.middle.adutils.RewardVideoAd;
import com.donews.middle.adutils.SplashAd;
import com.donews.middle.adutils.adcontrol.AdControlBean;
import com.donews.middle.adutils.adcontrol.AdControlManager;
import com.donews.middle.centralDeploy.TurntableSwitch;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.front.LotteryConfigManager;
import com.donews.middle.viewmodel.BaseMiddleViewModel;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.base.SmSdkConfig;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.NetworkUtils;
import com.donews.utilslibrary.utils.SPUtils;
import com.donews.yfsdk.manager.AdConfigManager;
import com.donews.yfsdk.monitor.LotteryAdCheck;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用模块: 主业务模块
 * <p>
 * 类描述: 欢迎页面
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-26
 */
public class SplashActivity extends MvvmBaseLiveDataActivity<MainActivitySplashBinding, BaseLiveDataViewModel> {

    private static final String TAG = "SplashActivity";
    private static final String DEAL = "main_agree_deal";

    /**
     * 后台切回前台的进入启动页
     *
     * @param act activity
     */
    public static void toForeGround(Activity act) {
        Log.e("SplashActivity", "toForeGround");
        Log.e("SplashActivity", act.getClass().getName());
        if (act.getClass() == SplashActivity.class
                || act.getClass().getName().equalsIgnoreCase("com.donews.notify.launcher.NotifyActivity")
                || act.getClass().getName().equalsIgnoreCase("com.donews.notify.launcher.NotifyActionActivity")
                || act.getClass().getName().equalsIgnoreCase("com.donews.keepalive.DazzleActivity")) {
            return; //自己调用的话。终止操作，因为可能会导致广告和中间的流程跳过直接到首页
        }
        Intent intent = new Intent(act, SplashActivity.class);
        intent.putExtra(toForeGroundKey, true);
        act.startActivity(intent);
    }


    /**
     * 请求权限code
     */
    private static final int REQUEST_PERMISSIONS_CODE = 1024;

    //再其他页面后台太久回到前台。导师开屏页面被打开的标记
    private static final String toForeGroundKey = "toForeGround";

    private PersonGuideDialog personGuideDialog;
    //启动页正常的等待时间
    public static final long PROGRESS_DURATION = 10 * 1000;
    //是否为后台到前台。即:是有后台唤醒到前台并非正常启动流程，T:唤醒，F:正常的启动逻辑
    private boolean mIsBackgroundToFore = false;

    private ValueAnimator mLoadAdAnimator;

    private int mNetworkIsAvailable = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_splash;
    }

    @Override
    public void initView() {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
        mIsBackgroundToFore = getIntent().getBooleanExtra(toForeGroundKey, false);
        EventBus.getDefault().register(this);

        init();

        mDataBinding.splashNetworkErrBtn.setOnClickListener(v -> init());
        mDataBinding.splashNetworkErrCheck.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mDataBinding.splashNetworkErrCheck.setOnClickListener(v -> {
            if (mNetworkIsAvailable == 2) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else if (mNetworkIsAvailable == 1) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    private void init() {
        if (checkNetWork() != 0) {
            return;
        }
        showPersonGuideDialog();
        if (mIsBackgroundToFore) {
            if (NetworkUtils.isAvailableByPing()) {
                loadHotStartAd();
            } else {
                goToMain();
            }
        } else {
            //冷启动,做app启动初始化相关
            SmSdkConfig.initData(UtilsConfig.getApplication());
            //检测隐私协议
            checkDeal();
        }

        ABSwitch.Ins().addCallBack(new ABSwitch.CallBack() {
            @Override
            public void onSuccess() {
                if (ABSwitch.Ins().isShowSplashScaleBtn()) {
                    mDataBinding.splashScaleTv.setVisibility(View.VISIBLE);
                    startGuideViewAnim();
                } else {
                    mDataBinding.splashScaleTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail() {
                if (ABSwitch.Ins().isShowSplashScaleBtn()) {
                    mDataBinding.splashScaleTv.setVisibility(View.VISIBLE);
                    startGuideViewAnim();
                } else {
                    mDataBinding.splashScaleTv.setVisibility(View.GONE);
                }
            }
        });
    }

    private final Handler mAnimatorHandler = new Handler(Looper.getMainLooper());
    private ObjectAnimator mGuideViewAnim;

    private void startGuideViewAnim() {
        cancelGuideAnim();
        mGuideViewAnim = ObjectAnimator.ofFloat(mDataBinding.splashScaleTv, "rotation", 0, 3f, 0, -3f, 0);
        mGuideViewAnim.setInterpolator(new LinearInterpolator());
        mGuideViewAnim.setRepeatMode(ValueAnimator.RESTART);
        mGuideViewAnim.setRepeatCount(2);
        mGuideViewAnim.setDuration(150);
        mGuideViewAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimatorHandler.removeCallbacksAndMessages(null);
                mAnimatorHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startGuideViewAnim();
                    }
                }, 500);
            }
        });
        mGuideViewAnim.start();
    }


    private void cancelGuideAnim() {
        if (mGuideViewAnim != null) {
            mGuideViewAnim.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("SplashActivity", "onResume");
        if (mNetworkIsAvailable != 0) {
            init();
        } else {
            showPersonGuideDialog();
        }
    }

    @Subscribe //网络状态变化监听
    public void eventNetworkChanage(NetworkChanageEvnet event) {
        LogUtils.v("网络状态发生了变化：" + event.getType());
    }

    @Subscribe //用户登录的通知
    public void eventUserLogin(LoginUserStatus event) {
        LogUtils.v("用户登录状态：" + event.getStatus());
        if (event.getStatus() != 1 && LoginHelp.getInstance().isLogin()) {
            //从未登录过。并且登录失败。那么开启登录通知在适当时候自动登录
            SplashUtils.INSTANCE.openLoginDeviceNotify();
        }
    }

    @Override
    protected void onDestroy() {
        Log.e("SplashActivity", "onDestroy");
        //设置为之后启动为非冷启动模式
//        SplashUtils.INSTANCE.setColdStart(false);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * 检查用户是否同意协议
     */
    private void checkDeal() {
        if (mNetworkIsAvailable != 0) {
            return;
        }
        if (SPUtils.getInformain(KeySharePreferences.DEAL, false)) {
            initSdk();
            return;
        }
        //如果协议已经同意，直接检查权限进入app
        if (SPUtils.getInformain(KeySharePreferences.DEAL, false)) {
            checkAndRequestPermission();
        }
    }

    private void initPushAndDnDi() {
        AnalysisHelp.setAnalysisInitUmeng(getApplication());
        if (!AnalysisHelp.analysisRegister) {
            AnalysisHelp.register(getApplication());
        }
    }

    private boolean mIsInitSdk = false;

    private void initSdk() {
        if (mIsInitSdk || mNetworkIsAvailable != 0) {
            return;
        }
        mIsInitSdk = true;

        DnSdkInit.INSTANCE.init(this.getApplication());

        SplashUtils.INSTANCE.savePersonExit(true);
        SPUtils.setInformain(KeySharePreferences.DEAL, true);
        SPUtils.setInformain(KeySharePreferences.AGREEMENT, true);
        //初始化大数据/友盟sdk
        initPushAndDnDi();
        checkAndRequestPermission();
    }

    private void showPersonGuideDialog() {
        if (personGuideDialog != null && personGuideDialog.isAdded() && personGuideDialog.isVisible()) {
            return;
        }

        //如果协议已经同意，不需要弹起弹框
        if (SPUtils.getInformain(KeySharePreferences.DEAL, false)) {
            initSdk();
            return;
        }
        if (personGuideDialog == null) {
            Logger.d("personGuideDialog no isAdded");
            personGuideDialog = new PersonGuideDialog();
            personGuideDialog.setSureListener(this::initSdk).setCancelListener(() -> {
                loadDisagreePrivacyPolicyAd();
                moveTaskToBack(true);
                personGuideDialog.dismiss();
            }).show(getSupportFragmentManager(), "PersonGuideDialog");
        }
    }

    /**
     * 加载热启动广告
     */
    private void loadHotStartAd() {
        startProgressAnim();
        if (!AdConfigManager.INSTANCE.getMNormalAdBean().getEnable()) {
            goToMain();
            return;
        }

        setFullScreen();

        loadSplash(false, true, false);
    }

    /**
     * 加载冷启动广告
     */
    private void loadClodStartAd() {
        startProgressAnim();
        if (!AdConfigManager.INSTANCE.getMNormalAdBean().getEnable()) {
            goToMain();
            return;
        }

        setHalfScreen();

        loadSplash(true, true, false);
    }

    /**
     * 加载广告
     */
    private void loadSplash(boolean halfScreen, boolean hotStart, boolean doubleSplash) {
        IAdSplashListener listener = new SimpleSplashListener() {
            @Override
            public void onAdShow() {
                super.onAdShow();
                stopProgressAnim();
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                super.onAdError(code, errorMsg);
                Logger.d("code = " + code + "  msg = " + errorMsg);
                goToMain();
//                loadCsjSplash(halfScreen, hotStart, doubleSplash);
            }

            @Override
            public void onAdDismiss() {
                super.onAdDismiss();
                if (doubleSplash) {
                    loadSplash(halfScreen, hotStart, false);
                } else {
                    goToMain();
                }
            }
        };

        SplashAd.INSTANCE.loadSplashAd(this, hotStart, mDataBinding.adHalfScreenContainer, listener, halfScreen);
    }

    private void loadCsjSplash(boolean halfScreen, boolean hotStart, boolean doubleSplash) {
        IAdSplashListener listener = new SimpleSplashListener() {
            @Override
            public void onAdShow() {
                super.onAdShow();
                stopProgressAnim();
            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                super.onAdError(code, errorMsg);
                Logger.d("code = " + code + "  msg = " + errorMsg);
                goToMain();
            }

            @Override
            public void onAdDismiss() {
                super.onAdDismiss();
/*                if (doubleSplash) {
                    loadSplash(halfScreen, hotStart, false);
                } else {*/
                goToMain();
//                }
            }
        };

        SplashAd.INSTANCE.loadCsjSplashAd(this, hotStart, mDataBinding.adHalfScreenContainer, listener, halfScreen);
    }

    //半屏显示广告
    private void setHalfScreen() {
        mDataBinding.adHalfScreenContainer.setVisibility(View.VISIBLE);
        mDataBinding.adFullScreenContainer.setVisibility(View.GONE);
    }

    //全屏显示广告
    private void setFullScreen() {
        mDataBinding.adFullScreenContainer.setVisibility(View.VISIBLE);
        mDataBinding.adHalfScreenContainer.setVisibility(View.GONE);
    }

    private long mPreClickTime = 0;

    private void loadDisagreePrivacyPolicyAd() {
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - mPreClickTime < 2000) {
            mPreClickTime = curClickTime;
            Toast.makeText(this, "点击频率过高", Toast.LENGTH_SHORT).show();
            return;
        }
        mPreClickTime = curClickTime;

        startProgressAnim();
//        AdControlManager.INSTANCE.addListener(() -> {
        AdControlBean configBean = AdControlManager.INSTANCE.getAdControlBean();
        if (configBean.getDisagreePrivacyPolicyAdEnable()) {
            if (configBean.getDisagreePrivacyPolicyAdType() == 1) {
                loadDisagreePrivacyPolicyInters();
            } else {
                loadDisagreePrivacyPolicyRewardVideo();
            }
        } else {
            finish();
        }
//            return null;
//        });
    }

    private void loadDisagreePrivacyPolicyInters() {
        InterstitialFullAd.INSTANCE.showAd(this, new SimpleInterstitialFullListener() {
            @Override
            public void onAdError(int errorCode, @NonNull String errprMsg) {
                super.onAdError(errorCode, errprMsg);
                finish();
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
                stopProgressAnim();
            }

            @Override
            public void onAdClose() {
                super.onAdClose();
                finish();
            }
        });
        /*InterstitialAd.INSTANCE.showAd(this, new SimpleInterstitialListener() {
            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                super.onAdError(code, errorMsg);
                finish();
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
                stopProgressAnim();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }
        });*/
    }

    private void loadDisagreePrivacyPolicyRewardVideo() {
        RewardVideoAd.INSTANCE.loadRewardVideoAd(this, new SimpleRewardVideoListener() {

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                super.onAdError(code, errorMsg);
                finish();
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
                stopProgressAnim();
            }

            @Override
            public void onRewardVerify(boolean result) {
                super.onRewardVerify(result);
                finish();
            }
        }, false);
    }

    private int checkNetWork() {
        if (!NetworkUtils.isConnected()) {
            mNetworkIsAvailable = 1;
        } else {
            if (!NetworkUtils.isAvailableByPing()) {
                mNetworkIsAvailable = 2;
            } else {
                mNetworkIsAvailable = 0;
                mDataBinding.splashNetworkError.setVisibility(View.GONE);
            }
        }

        if (mNetworkIsAvailable != 0) {
            ToastUtil.showShort(this, "网络不可达，请检查网络环境！");
            mDataBinding.splashNetworkError.setVisibility(View.VISIBLE);
            stopProgressAnim();
        } else {
            mDataBinding.splashNetworkError.setVisibility(View.GONE);
        }
        return mNetworkIsAvailable;
    }

    private void goToMain() {
        stopProgressAnim();
        cancelGuideAnim();
        mDataBinding.splashScaleTv.clearAnimation();

        if (checkNetWork() != 0) {
            return;
        }

        if (!mIsBackgroundToFore) {
            if (AppStatusManager.getInstance().getAppStatus() != AppStatusConstant.STATUS_NORMAL) {
                LotteryAdCheck.INSTANCE.init();
                GuideActivity.start(this);
            }
        } else {
            if (AppManager.getInstance().getActivity(MainActivity.class) == null) {
                MainActivity.start(this);
            }
        }
        mHadPermissions = false;
        finish();
    }

    private void deviceLogin() {
        //设备登录
        SplashUtils.INSTANCE.loginDevice();
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> lackedPermission = new ArrayList<String>();
            if ((checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.INTERNET);
            }

            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
            }

            if ((checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            // 权限都已经有了，那么直接调用SDK
            if (lackedPermission.size() == 0) {
                hadPermissions();
            } else {
                String[] requestPermissions = new String[lackedPermission.size()];
                lackedPermission.toArray(requestPermissions);
                requestPermissions(requestPermissions, REQUEST_PERMISSIONS_CODE);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //修改为不需要处理拒绝的逻辑。只要回来就直接过
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            hadPermissions();
        }
    }

    private boolean mHadPermissions = false;

    private void hadPermissions() {
        if (mHadPermissions) {
            return;
        }

        // 打个补丁,by dw
        AdConfigManager.INSTANCE.init();
        //拉取中台开关配置
        ABSwitch.Ins().init();
        //获取大转盘的配置
        TurntableSwitch.Ins().init();
        //首页运营位
        FrontConfigManager.Ins().init();
        //抽奖页运营位
        LotteryConfigManager.Ins().init();
        //拉去首页相关签到配置
        BaseMiddleViewModel.getBaseViewModel()
                .getDailyTasks(null);
        mHadPermissions = true;
        if ((SPUtils.getInformain(KeySharePreferences.IS_FIRST_IN_APP,
                0) <= 0 && ABSwitch.Ins().isSkipSplashAd4NewUser())
                || !NetworkUtils.isAvailableByPing()) {
            LogUtil.e("hadPermissions()： gomain");
            goToMain();
        } else {
            LogUtil.e("hadPermissions()： load ad");
            deviceLogin();
            loadClodStartAd();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        if (grantResults == null) {
            return true;
        }
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    private void startProgressAnim() {
        if (mLoadAdAnimator != null) {
            mLoadAdAnimator.cancel();
        }
        if (mDataBinding != null) {
            mDataBinding.pbProgress.setProgress(0);
        }

        mLoadAdAnimator = ValueAnimator.ofInt(0, 100);
        mLoadAdAnimator.setDuration(PROGRESS_DURATION);
        mLoadAdAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                if (mDataBinding != null) {
                    if (mDataBinding.groupProgress.getVisibility() != View.VISIBLE) {
                        mDataBinding.groupProgress.setVisibility(View.VISIBLE);
                    }
                    mDataBinding.pbProgress.setProgress(progress);
                }
            }
        });
        mLoadAdAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        mLoadAdAnimator.start();
    }

    private void stopProgressAnim() {
        if (!this.isFinishing()) {
            runOnUiThread(() -> {
                try {
                    if (mLoadAdAnimator != null && mLoadAdAnimator.isRunning()) {
                        mLoadAdAnimator.cancel();
                    }
                    mLoadAdAnimator = null;
                } catch (Exception e) {
                }
            });
        }
    }
}
