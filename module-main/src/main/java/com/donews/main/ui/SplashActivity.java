package com.donews.main.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.NetworkChanageEvnet;
import com.dn.sdk.AdLoadManager;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.listener.AdSplashListener;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.donews.main.common.CommonParams;
import com.donews.main.databinding.MainActivitySplashBinding;
import com.donews.main.dialog.PersonGuideDialog;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.main.entitys.resps.SplashDoubleADConfigResp;
import com.donews.main.utils.SplashUtils;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.base.SmSdkConfig;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

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
    private Handler mHandler = new Handler(Looper.myLooper());
    private static final String DEAL = "main_agree_deal";
    //再其他页面后台太久回到前台。导师开屏页面被打开的标记
    private static final String toForeGroundKey = "toForeGround";

    /**
     * 后台切回前台的进入启动页
     *
     * @param act
     */
    public static void toForeGround(Activity act) {
        if (act.getClass() == SplashActivity.class) {
            return; //自己调用的话。终止操作，因为可能会导致广告和中间的流程跳过直接到首页
        }
        act.startActivity(
                new Intent(act, SplashActivity.class).putExtra(toForeGroundKey, true)
        );
    }

    //启动页正常的等待时间
    public static final long SPLASH_WAIT_TIME = 1 * 1000;
    private CountDownTimer countDownTimer;
    private int adShowCount = 1; //广告播放的次数
    //是否为后台到前台。即:是有后台唤醒到前台并非正常启动流程，T:唤醒，F:正常的启动逻辑
    private boolean mIsBackgroundToFore = false;

    @Override
    protected int getLayoutId() {
        ScreenAutoAdapter.match(this, 375.0f);
        ImmersionBar.with(this)
//                .titleBar(findViewById(R.id.top_view))
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .statusBarDarkFont(true)
                .init();

        return R.layout.main_activity_splash;
    }

    @Override
    public void initView() {
        mIsBackgroundToFore = getIntent().getBooleanExtra(toForeGroundKey, false);
        EventBus.getDefault().register(this);
        checkDeal();
    }

    @Subscribe //网络状态变化监听
    public void eventNetworkChanage(NetworkChanageEvnet event) {
        LogUtils.v("网络状态发生了变化：" + event.getType());
    }

    @Subscribe //用户登录的通知
    public void eventUserLogin(LoginUserStatus event) {
        setSplashProgress(10);
        LogUtils.v("用户登录状态：" + event.getStatus());
        if (event.getStatus() != 1 && LoginHelp.getInstance().isLogin()) {
            //从未登录过。并且登录失败。那么开启登录通知在适当时候自动登录
            SplashUtils.INSTANCE.openLoginDeviceNotify();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("==onPause==");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //设置为之后启动为非冷启动模式
        SplashUtils.INSTANCE.setColdStart(false);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        //activity销毁时移除所有消息,防止内存泄漏
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void finish() {
        //检查是否多次拒绝，是否开启压榨模式
        if (!SplashUtils.INSTANCE.checkPersonExitStartAD(this, () -> {
            //压榨完成之后需要做的事
            SplashActivity.super.finish();
            return null;
        })) {
            //不需要播放激励视频
            super.finish();
        }
    }

    /**
     * 检查用户是否同意协议
     */
    private void checkDeal() {

        //如果协议已经同意，直接检查权限进入app
        if (SPUtils.getInformain(KeySharePreferences.DEAL, false)) {
            checkAndRequestPermission();
            return;
        }

        new PersonGuideDialog()
                .setSureListener(() -> {
                    SplashUtils.INSTANCE.savePersonExit(true);
                    SPUtils.setInformain(KeySharePreferences.DEAL, true);
                    SPUtils.setInformain(KeySharePreferences.AGREEMENT, true);
                    AnalysisHelp.setAnalysisInitUmeng(getApplication());

                    if (!AnalysisHelp.analysisRegister) {
                        AnalysisHelp.register(getApplication());
                    }
                    checkAndRequestPermission();
                })
                .show(getSupportFragmentManager(), null);
    }

    //获取启动页广告配置。主要是双屏广告
    private void loadSplashConfig() {
        //如果为唤醒模式、非冷启动。则不显示进度条
        if(mIsBackgroundToFore || !SplashUtils.INSTANCE.isColdStart()){
            mDataBinding.splashProgressLayout.setVisibility(View.GONE);
        }
        setSplashProgress(10);
        if (!NetworkUtils.isAvailableByPing()) {
            ToastUtil.show(this, "网络异常，请检查网络连接");
            setSplashProgress(100);
            goToMain();
            return;
        }
        SplashUtils.INSTANCE.loginDevice();
        setSplashProgress(30);

        SplashUtils.INSTANCE.getSplashDoubleADConfig(() -> {
            //配置获取完成之后。计算播放次数开始播放广告
            if (SplashUtils.INSTANCE.getSplashADConfig() != null) {
                //获取到了配置信息，处理参数
            }
            setSplashProgress(100);
            loadSplash();
            return null;
        });
    }

    //设置进度
    private void setSplashProgress(int addCount) {
        if (mDataBinding.splashProgress.getProgress() + addCount > 100) {
            mDataBinding.splashProgress.setProgress(100);
//            mDataBinding.splashProgressLayout.setVisibility(View.GONE);
        } else {
            mDataBinding.splashProgress.setProgress(
                    mDataBinding.splashProgress.getProgress() + addCount);
        }
    }

    /**
     * 加载广告
     */
    @SuppressLint("MissingPermission")
    private synchronized void loadSplash() {
        if (adShowCount > 0) {
            adShowCount--;
        }
        if (SplashUtils.INSTANCE.isColdStart()) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mDataBinding.adContainer.getLayoutParams();
            //冷启动半屏广告,底部流出 27% 的空余
            lp.bottomMargin = (int) (ScreenUtils.getScreenHeight() * 0.27F);
            mDataBinding.adContainer.setLayoutParams(lp);
        }
        SmSdkConfig.initData(UtilsConfig.getApplication());
        RequestInfo requestInfo = new RequestInfo(AdIdConfig.SPLASH_ID);
        requestInfo.container = mDataBinding.adContainer;

        startCountDown();

//        AdLoadManager.getInstance()
//                .loadSplash(this, requestInfo, new AdSplashListener() {
//                    @Override
//                    public void onNoAD(String s) {
//                        Log.i(TAG, "onNoAD " + s + " PackageName " + getPackageName());
//                        cancelCountDown();
//                        goToMain();
//                    }
//
//                    @Override
//                    public void onClicked() {
//                        Log.i(TAG, "onClicked");
//                    }
//
//                    @Override
//                    public void onShow() {
//                        cancelCountDown();
//                        Log.i(TAG, "onShow");
//                    }
//
//                    @Override
//                    public void onPresent() {
//                        cancelCountDown();
//                        Log.i(TAG, "onPresent");
//                    }
//
//                    @Override
//                    public void onADDismissed() {
//                        Log.i(TAG, "onADDismissed");
//                        goToMain();
//                    }
//
//                    @Override
//                    public void extendExtra(String s) {
//                        Log.i(TAG, "extendExtra" + s);
//                    }
//                });
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(SPLASH_WAIT_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                goToMain();
            }
        };
        countDownTimer.start();
    }

    private void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void goToMain() {
        if (AppStatusManager.getInstance().getAppStatus() != AppStatusConstant.STATUS_NORMAL) {
            MainActivity.start(this);
//            ARouteHelper.routeSkip(RouterActivityPath.User.PAGER_LOGIN);
        }
        finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
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

//        if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }


        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            loadSplashConfig();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            loadSplashConfig();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            //  Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }
}
