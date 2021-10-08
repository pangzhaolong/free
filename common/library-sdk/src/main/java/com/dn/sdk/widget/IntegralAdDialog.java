package com.dn.sdk.widget;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.test.espresso.remote.EspressoRemoteMessage;

import com.bumptech.glide.Glide;
import com.dn.drouter.ARouteHelper;
import com.dn.drouter.annotation.DNMethodRoute;
import com.dn.sdk.R;
import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.databinding.SdkIntegralAdDialogBinding;
import com.dn.sdk.dialog.BaseFragmentDialog;
import com.dn.sdk.dialog.LoadingDialog;
import com.dn.sdk.lib.integral.IntegerDialogManager;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.receiver.PackageReceiver;
import com.dn.sdk.utils.ApkUtils;
import com.dn.sdk.widget.progressbtn.ProgressButton;
import com.donews.base.utils.ToastUtil;
import com.donews.common.download.DownloadListener;
import com.donews.common.download.DownloadManager;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.config.ServicesConfig;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.NetworkUtils;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * @author by SnowDragon
 * Date on 2021/4/9
 * Description:
 */
public class IntegralAdDialog extends BaseFragmentDialog {
    private volatile boolean isStartApp = false;
    private boolean isAlreadyHintNet = false;
    private boolean downloadComplete = false;


    private IntegralBean.DataBean bean;
    private SdkIntegralAdDialogBinding mDataBinding;
    private ProgressButton progressButton;
    DownloadManager downloadManager;
    private long appUseTimes = 0;
    private int downLoadProgress = 0;
    private static final long AWARD_TIME = 30;
    private String award;
    private LoadingDialog loadingDialog;
    private FragmentActivity activity;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.i("chyy", " handleMessage: " + appUseTimes);
            appUseTimes++;
            if (appUseTimes <= AWARD_TIME) {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };


    public static void showIntegralDialog(FragmentActivity activity, IntegralBean.DataBean bean) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        IntegralAdDialog dialog = new IntegralAdDialog();
        dialog.setBean(bean);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, "integralAdDialog")
                .commitAllowingStateLoss();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.sdk_integral_ad_dialog;
    }

    @Override
    protected void initView() {
        ARouteHelper.bind(this);
        activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            disMissDialog();
            return;
        }
        mDataBinding = DataBindingUtil.bind(rootView);
        if (mDataBinding == null || bean == null) {
            disMissDialog();
            return;
        }
        mDataBinding.ivClose.setOnClickListener(v -> {
            disMissDialog();
        });

        mDataBinding.tvTitle.setText(TextUtils.isEmpty(bean.text) ? bean.name : bean.text);
        mDataBinding.tvSubtitle.setText(bean.desc);
        mDataBinding.tvAwardHint.setText(String.format("下载并试玩10秒，获得%s元", bean.title));

        setSpan(mDataBinding.tvAwardHint, bean.title);

        //默认设置
        if (ApkUtils.isAppInstalled(bean.pkg)) {
            mDataBinding.proBtn.setCurrentText("立即试玩");
        } else {
            mDataBinding.proBtn.setCurrentText("立即下载");
        }
        if (!TextUtils.isEmpty(bean.appIcon)) {
            Glide.with(UtilsConfig.getApplication())
                    .load(bean.appIcon).into(mDataBinding.ivLogo);
        } else {
            mDataBinding.ivLogo.setVisibility(View.GONE);
        }


        createDownload();

        registerInstallListener();

        progressButton = mDataBinding.proBtn;
        RxView.clicks(progressButton)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(unit -> {
                    if (bean == null) {
                        disMissDialog();
                        return;
                    }
                    if (progressButton.getCurrentText().contains("立即提现") && bean.isWithdraw) {
                        loadingDialog = new LoadingDialog().setDismissOnBackPressed(false).setDismissOnTouchOutside(false)
                                .setDescription("加载中，请稍后");
                        loadingDialog.show(activity.getSupportFragmentManager(), "diaglog");
                        setCashMoney();
                        return;
                    }

                    if (ApkUtils.isAppInstalled(bean.pkg)) {

                        if (!TextUtils.isEmpty(bean.deepLink)) {
                            ApkUtils.startAppBySchema(bean.deepLink);
                        } else if (!TextUtils.isEmpty(bean.pkg)) {
                            ApkUtils.startAppByPackageName(bean.pkg);
                        }

                        intervalReport(5);
                        if (!isStartApp) {
                            isStartApp = true;
                            appUseTimes = 0;
                        }
                        return;
                    }
                    //wifi连接
                    if (NetworkUtils.isWifiConnected()) {
                        downLoadApp();
                    } else if (NetworkUtils.isAvailableByPing()) {
                        //非wifi连接

                        //如果已经提示过不再非wifi网络出弹窗
                        if (isAlreadyHintNet) {
                            downLoadApp();
                            return;
                        }
                        //非wifi网络弹窗提示
                        NetHintDialog.showDialog(getActivity(), () -> {
                            isAlreadyHintNet = true;
                            downLoadApp();
                        });
                    } else {
                        ToastUtil.show(UtilsConfig.getApplication(), "当前网络不可用！");
                    }
                });


    }

    private void registerInstallListener() {
        //注册安装激活监听
        PackageReceiver.installHashMap.put(PackageReceiver.RECEIVER_AD_CLICK,
                new PackageReceiver.InstallListener() {
                    @Override
                    public void installComplete(String packageName) {
                        Log.i("chyy", " installComplste");
                        if (bean != null && packageName.equalsIgnoreCase(bean.pkg)) {
                            bean.status = 4;
                            intervalReport(4);
                        }
                        if (progressButton != null) {
                            progressButton.setCurrentText("立即试玩");
                        }
                    }

                    @Override
                    public void activateComplete(String packageName) {
                        Log.i("chyy", " activateComplete");
                        if (bean != null && packageName.equalsIgnoreCase(bean.pkg)) {
                            intervalReport(5);
                            if (!isStartApp) {
                                isStartApp = true;
                                appUseTimes = 0;
                            }
                        }

                        if (mHandler != null && isStartApp && appUseTimes < AWARD_TIME) {
                            if (mHandler.hasMessages(0)) {
                                mHandler.removeMessages(0);
                            }
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                        }

                    }
                });
    }

    public void setSpan(TextView textView, String award) {
        this.award = award;
        String content = textView.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        int index = content.indexOf(award);
        if (index == -1) {
            return;
        }
        SpannableString span = new SpannableString(content);

        span.setSpan(new ForegroundColorSpan(0xffFF1A54), index, index + award.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(span);

    }

    public void setBean(IntegralBean.DataBean bean) {
        this.bean = bean;
    }

    @Keep
    @DNMethodRoute(RouterActivityPath.ClassPath.WEB_VIEW_OBJ_ACTIVITY_JAVASCRIPT)
    public void setCashMoney(){
        AdSdkHttp.getCashMoney(bean.money,bean.pkg).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer o) {
                if (o == CashType.TYPE_101.CODE) { // 表示需要绑定微信
                    ARouteHelper.routeAccessServiceForResult(ServicesConfig.User.LONGING_SERVICE,
                            "weChatBind", null);
                } else {
                    CashType[] cashType = CashType.values();
                    for (CashType c : cashType) {
                        if (c.CODE == o) {
                            if (activity == null || activity.isFinishing()) {
                                return;
                            }
                            CashHintDialog dialog = new CashHintDialog(c.MSG, c.okName);
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(dialog, "tag")
                                    .commitAllowingStateLoss();
                            break;

                        }
                    }
                    if (loadingDialog != null && !loadingDialog.isHidden()) {
                        loadingDialog.disMissDialog();
                    }
                    disMissDialog();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("chyy", " onResume：" + appUseTimes + " isHidden: " + isHidden());
        Log.i("chyy", " onResume：" + appUseTimes + " isStartApp: " +isStartApp);
        if (mHandler != null && mHandler.hasMessages(0)) {
            mHandler.removeMessages(0);
        }

        if (!isStartApp) {
            return;
        }
        if (appUseTimes >= AWARD_TIME) {
            if (bean == null) {
                disMissDialog();
                return;
            }
            Log.i("chyy", " onResume：" + bean.toString());
            //发送奖励
            if (!bean.isWithdraw) {
                IntegralAwardDialog.sendReward(activity, bean.pkg, award);
            } else {
                progressButton.setCurrentText("立即提现");
            }
        } else {
            ToastUtil.show(activity, String.format("在试玩%s秒获得奖励", AWARD_TIME - appUseTimes));
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("chyy", " onPause ：" + appUseTimes + " isHidden: " + isHidden());

        if (mHandler != null && isStartApp && appUseTimes < AWARD_TIME) {
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IntegerDialogManager.getInstance().update();
        Log.i("chyy", "ondestroy");
        ARouteHelper.unBind(this);
        if (downloadManager != null) {
            downloadManager.pause();
        }
        if (mHandler != null && mHandler.hasMessages(0)) {
            mHandler.removeMessages(0);
        }
        mHandler = null;
    }

    /**
     * 积分墙数据上报
     * 上报数据
     */
    private synchronized void intervalReport(final int status) {
        if (bean == null) {
            return;
        }
        AdSdkHttp adSdkHttp = new AdSdkHttp();
        IntegralDataSupply.getInstance().appActivateReport(bean);

        adSdkHttp.intervalReport(bean.pkg, bean.name, bean.downLoadUrl,
                bean.deepLink, bean.appIcon, status, bean.type, bean.text, bean.desc, new SimpleCallBack<IntegralBean>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(IntegralBean integralBean) {
                        bean = integralBean.appList.get(0);
                    }
                });

    }

    /**
     * 创建下载器
     */
    private void createDownload() {
        if (bean == null) {
            return;
        }
        downloadManager = new DownloadManager(UtilsConfig.getApplication(), bean.pkg, bean.downLoadUrl, new DownloadListener() {
            @Override
            public void updateProgress(int progress) {
                downLoadProgress = progress;
                if (progressButton != null) {
                    progressButton.setProgressText("下载中", progress);
                    progressButton.postInvalidate();
                }
            }

            @Override
            public void downloadComplete(String pkName, String path) {
                PackageReceiver.installPackageHashMap.put(pkName, path);
                downLoadProgress = 100;
                downloadComplete = true;
                intervalReport(3);
                if (progressButton != null) {
                    progressButton.setProgressText("下载中", 100);
                    progressButton.setCurrentText("安装中");
                    progressButton.postInvalidate();
                }
            }

            @Override
            public void downloadError(String error) {

            }
        });
    }


    private void downLoadApp() {
        if (progressButton != null && downLoadProgress == 0) {
            progressButton.setCurrentText("下载中...");
        }
        downloadComplete = false;

        downloadManager.start();

    }


}
