package com.donews.main.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.LogUtils;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.NetworkChanageEvnet;
import com.dn.sdk.listener.IAdSplashListener;
import com.dn.sdk.listener.impl.SimpleInterstitialListener;
import com.dn.sdk.listener.impl.SimpleRewardVideoListener;
import com.dn.sdk.listener.impl.SimpleSplashListener;
import com.donews.base.base.AppManager;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.base.fragmentdialog.AbstractFragmentDialog;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.ad.business.bean.JddAdConfigBean;
import com.donews.common.ad.business.loader.AdManager;
import com.donews.common.ad.business.manager.JddAdConfigManager;
import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.contract.LoginHelp;
import com.donews.jpush.JPushHelper;
import com.donews.main.BuildConfig;
import com.donews.main.R;
import com.donews.main.databinding.MainActivitySplashBinding;
import com.donews.main.dialog.PersonGuideDialog;
import com.donews.main.utils.SplashUtils;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.base.SmSdkConfig;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.NetworkUtils;
import com.donews.utilslibrary.utils.SPUtils;
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

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity_splash;
    }

    @Override
    public void initView() {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_FORCE_KILLED);
        mIsBackgroundToFore = getIntent().getBooleanExtra(toForeGroundKey, false);
        EventBus.getDefault().register(this);
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
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("SplashActivity", "onResume");
        showPersonGuideDialog();
        Uri referrer = this.getReferrer();

        /*Log.e("Notify", "----------------->");
        if (referrer != null) {
            try {

                Log.e("Notify", referrer.toString());
                Log.e("Notify", referrer.getPath());
                Log.e("Notify", referrer.getLastPathSegment());
            } catch (Exception e) {

            }
        }
        Log.e("Notify", "-----------------<");*/
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
        //极光推送
        JPushHelper.setDebugMode(BuildConfig.DEBUG);
        JPushHelper.init(getApplication());

//        Log.e("xxxx", "xxxxxxxxx" + JPushInterface.getRegistrationID(this));
    }

    private void showPersonGuideDialog() {
        //如果协议已经同意，不需要弹起弹框
        if (SPUtils.getInformain(KeySharePreferences.DEAL, false)) {
            initPushAndDnDi();
            return;
        }
        if (personGuideDialog != null && personGuideDialog.isAdded() && personGuideDialog.isVisible()) {
            Logger.d("personGuideDialog isAdded");
        } else {
            Logger.d("personGuideDialog no isAdded");
            personGuideDialog = new PersonGuideDialog();
            personGuideDialog.setSureListener(() -> {
                SplashUtils.INSTANCE.savePersonExit(true);
                SPUtils.setInformain(KeySharePreferences.DEAL, true);
                SPUtils.setInformain(KeySharePreferences.AGREEMENT, true);
                initPushAndDnDi();
                checkAndRequestPermission();
            }).setCancelListener(new AbstractFragmentDialog.CancelListener() {
                @Override
                public void onCancel() {
                    loadDisagreePrivacyPolicyAd();
                    moveTaskToBack(true);
                    personGuideDialog.dismiss();
                }
            }).show(getSupportFragmentManager(), "PersonGuideDialog");
        }
    }

    /**
     * 加载热启动广告
     */
    private void loadHotStartAd() {
        startProgressAnim();
        JddAdConfigManager.INSTANCE.addListener(() -> {
            JddAdConfigBean configBean = JddAdConfigManager.INSTANCE.getJddAdConfigBean();
            if (configBean.getHotStartAdEnable()) {
                if (configBean.getHotStartSplashStyle() == 1) {
                    setHalfScreen();
                } else {
                    setFullScreen();
                }
                boolean hotDoubleSplash = configBean.getHotStartDoubleSplashOpen()
                        && LoginHelp.getInstance().checkUserRegisterTime(configBean.getHotStartDoubleSplash());
                loadSplash(configBean.getHotStartSplashStyle() == 1, hotDoubleSplash);
            }
            return null;
        });
    }

    /**
     * 加载冷启动广告
     */
    private void loadClodStartAd() {
        startProgressAnim();
        JddAdConfigManager.INSTANCE.addListener(() -> {
            JddAdConfigBean configBean = JddAdConfigManager.INSTANCE.getJddAdConfigBean();
            //如果为唤醒模式、非冷启动。则不显示进度条,热启动
            if (configBean.getColdStartAdEnable()) {
                if (configBean.getColdStartSplashStyle() == 1) {
                    setHalfScreen();
                } else {
                    setFullScreen();
                }
                boolean coldDoubleSplash = configBean.getColdStartDoubleSplashOpen()
                        && LoginHelp.getInstance().checkUserRegisterTime(configBean.getColdStartDoubleSplash());
                loadSplash(configBean.getColdStartSplashStyle() == 1, coldDoubleSplash);
            }
            return null;
        });
    }

    /**
     * 加载广告
     */
    private void loadSplash(boolean halfScreen, boolean doubleSplash) {
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
                if (doubleSplash) {
                    loadSplash(halfScreen, false);
                } else {
                    goToMain();
                }
            }
        };
        if (halfScreen) {
            AdManager.INSTANCE.loadHalfScreenSplashAd(this, mDataBinding.adHalfScreenContainer, listener);
        } else {
            AdManager.INSTANCE.loadFullScreenSplashAd(this, mDataBinding.adFullScreenContainer, listener);
        }
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
        JddAdConfigManager.INSTANCE.addListener(() -> {
            JddAdConfigBean configBean = JddAdConfigManager.INSTANCE.getJddAdConfigBean();
            if (configBean.getDisagreePrivacyPolicyAdEnable()) {
                if (configBean.getDisagreePrivacyPolicyAdType() == 1) {
                    loadDisagreePrivacyPolicyInters();
                } else {
                    loadDisagreePrivacyPolicyRewardVideo();
                }
            } else {
                finish();
            }
            return null;
        });
    }

    private void loadDisagreePrivacyPolicyInters() {
        AdManager.INSTANCE.loadInterstitialAd(this, new SimpleInterstitialListener() {

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
        });
    }

    private void loadDisagreePrivacyPolicyRewardVideo() {
        AdManager.INSTANCE.loadInvalidRewardVideoAd(this, new SimpleRewardVideoListener() {

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
        });
    }

    private void goToMain() {
        stopProgressAnim();
        Log.e("xxxx", "xxx" + mIsBackgroundToFore);
        if (!mIsBackgroundToFore) {
            Log.e("xxxx", "xxx111111111111");
            if (AppStatusManager.getInstance().getAppStatus() != AppStatusConstant.STATUS_NORMAL) {
//                Log.e("xxxx", "xxx222222222222222");
                LotteryAdCount.INSTANCE.init();
                GuideActivity.start(this);
            }
        } else {
            Log.e("xxxx", "xxx22222222222222");
            if (AppManager.getInstance().getActivity(MainActivity.class) == null) {
                Log.e("xxxx", "xxx33333333333333333333");
                MainActivity.start(this);
            }
        }
        finish();
    }

    private void deviceLogin() {
        //设备登录
        SplashUtils.INSTANCE.loginDevice();
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> lackedPermission = new ArrayList<String>();
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
            }

            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
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
//        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
//            loadSplashConfig();
//        } else {
//            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
//            //  Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            finish();
//        }
    }

    private void hadPermissions() {
        if (NetworkUtils.isAvailableByPing()) {
            deviceLogin();
            loadClodStartAd();
        } else {
            ToastUtil.show(SplashActivity.this, "");
            goToMain();
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
        if (mLoadAdAnimator != null && mLoadAdAnimator.isRunning()) {
            mLoadAdAnimator.cancel();
        }
        mLoadAdAnimator = null;
    }
}
