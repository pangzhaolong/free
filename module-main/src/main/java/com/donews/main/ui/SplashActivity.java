package com.donews.main.ui;

import android.Manifest;
import android.annotation.TargetApi;
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

import androidx.annotation.Nullable;

import com.dn.drouter.ARouteHelper;
import com.dn.sdk.AdLoadManager;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.listener.AdSplashListener;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.donews.main.common.CommonParams;
import com.donews.main.databinding.MainActivitySplashBinding;
import com.donews.main.dialog.PersonGuideDialog;
import com.donews.base.base.AppStatusConstant;
import com.donews.base.base.AppStatusManager;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.base.SmSdkConfig;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.SPUtils;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

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
public class SplashActivity extends MvvmBaseLiveDataActivity<MainActivitySplashBinding, BaseLiveDataViewModel>   {

    private static final String TAG = "SplashActivity";
    private Handler mHandler = new Handler(Looper.myLooper());
    private static final String DEAL = "main_agree_deal";

    public static final long SPLASH_WAIT_TIME = 3 * 1000;
    private CountDownTimer countDownTimer;

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
        checkDeal();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


    /**
     * 加载广告
     */
    private void loadSplash() {
        SmSdkConfig.initData(UtilsConfig.getApplication());
        CommonParams.setNetWork();
        RequestInfo requestInfo = new RequestInfo(AdIdConfig.SPLASH_ID);
        requestInfo.container = mDataBinding.adContainer;

        startCountDown();

        AdLoadManager.getInstance()
                .loadSplash(this, requestInfo, new AdSplashListener() {
                    @Override
                    public void onNoAD(String s) {
                        Log.i(TAG, "onNoAD " + s + " PackageName " + getPackageName());
                        cancelCountDown();
                        goToMain();
                    }

                    @Override
                    public void onClicked() {
                        Log.i(TAG, "onClicked");
                    }

                    @Override
                    public void onShow() {
                        cancelCountDown();
                        Log.i(TAG, "onShow");
                    }

                    @Override
                    public void onPresent() {
                        cancelCountDown();
                        Log.i(TAG, "onPresent");
                    }

                    @Override
                    public void onADDismissed() {
                        Log.i(TAG, "onADDismissed");
                        goToMain();
                    }

                    @Override
                    public void extendExtra(String s) {
                        Log.i(TAG, "extendExtra" + s);
                    }
                });
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

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d("==onPause==");

    }


    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("==onStop==");
        finish();
    }

    private void goToMain() {
        if (AppStatusManager.getInstance().getAppStatus() != AppStatusConstant.STATUS_NORMAL) {
            MainActivity.start(this);
//            ARouteHelper.routeSkip(RouterActivityPath.User.PAGER_LOGIN);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁时移除所有消息,防止内存泄漏
        mHandler.removeCallbacksAndMessages(null);
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
            loadSplash();
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
            loadSplash();
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
