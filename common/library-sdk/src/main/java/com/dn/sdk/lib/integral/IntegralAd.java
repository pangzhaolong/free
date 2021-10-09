package com.dn.sdk.lib.integral;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.bumptech.glide.Glide;
import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.api.UrlCreator;
import com.dn.sdk.bean.IntegralAwardBean;
import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.receiver.PackageReceiver;
import com.dn.sdk.utils.ApkUtils;
import com.dn.sdk.utils.SdkLogUtils;
import com.dn.sdk.widget.IntegralView;
import com.dn.sdk.widget.NetHintDialog;
import com.dn.sdk.widget.TryPlayHintDialog;
import com.dn.sdk.widget.progressbtn.ProgressButton;
import com.donews.base.utils.ToastUtil;
import com.donews.common.download.DownloadListener;
import com.donews.common.download.DownloadManager;
import com.donews.common.utils.DensityUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.NetworkUtils;

/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description:
 */
public class IntegralAd implements LifecycleObserver {

    private volatile boolean giveAward = false;
    private volatile boolean isStartApp = false;
    private boolean isAlreadyHintNet = false;
    private boolean downloadComplete = false;

    private FragmentActivity activity;
    private IntegralBean.DataBean bean;
    private RequestInfo requestInfo;
    DownloadManager downloadManager;

    private long startAppTime = 0;
    long upClickTime = 0;
    private int downLoadProgress = 0;

    private AdSdkHttp adSdkHttp;
    protected ProgressButton progressButton;


    public void loadAd(FragmentActivity activity, Lifecycle lifecycle, boolean giveAward, RequestInfo requestInfo) {
        this.activity = activity;
        this.requestInfo = requestInfo;
        this.giveAward = giveAward;

        if (activity == null || lifecycle == null || requestInfo == null) {
            SdkLogUtils.i(SdkLogUtils.TAG, " activity/lifecycle/container is null");
            return;
        }
        if (requestInfo.container == null) {
            SdkLogUtils.i(SdkLogUtils.TAG, " container is null");
            return;
        }
        lifecycle.addObserver(this);

        //从缓存中获取广告数据
        bean = IntegralDataSupply.getInstance().getServerIntegralBean();

        if (bean == null) {
            refreshIntegralBean();
        } else {
            addView(activity);
        }
        //注册安装激活监听
        PackageReceiver.installHashMap.put(PackageReceiver.RECEIVER_AD_CLICK,
                new PackageReceiver.InstallListener() {
                    @Override
                    public void installComplete(String packageName) {
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

                        if (bean != null && packageName.equalsIgnoreCase(bean.pkg)) {
                            intervalReport(5);
                            if (!isStartApp) {
                                isStartApp = true;
                                startAppTime = System.currentTimeMillis();
                            }
                        }

                    }
                });
    }

    private void addView(FragmentActivity activity) {
        if (requestInfo == null || activity == null || activity.isFinishing()) {
            return;
        }
        if (bean == null) {
            //数据为null的是清除 积分数据展示
            if (requestInfo.container != null) {
                requestInfo.container.removeAllViews();
            }
            return;
        }

        //没有下载地址，直接返回
        if (TextUtils.isEmpty(bean.downLoadUrl)) {
            return;
        }

        isStartApp = false;
        IntegralView integralView = new IntegralView(activity.getApplicationContext());

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (requestInfo.width != 0) {
            params.width = (int) requestInfo.width;
        } else {
            params.width = DensityUtils.getScreenWidth();
        }
        progressButton = integralView.getTextBtn();
        integralView.setLayoutParams(params);
        integralView.setAward(bean.title);
        integralView.setAppName(bean.name);

        //默认设置
        if (ApkUtils.isAppInstalled(bean.pkg)) {
            progressButton.setCurrentText("立即试玩");
        } else {
            progressButton.setCurrentText("立即下载");
        }

        if (!TextUtils.isEmpty(bean.appIcon)) {
            Glide.with(UtilsConfig.getApplication())
                    .load(bean.appIcon).into(integralView.getIvLogo());
        } else {
            integralView.getIvLogo().setVisibility(View.GONE);
        }

        upClickTime = 0;
        progressButton.setOnClickListener(v -> {
            //方连点处理
            if (System.currentTimeMillis() - upClickTime < 500) {
                return;
            }
            upClickTime = System.currentTimeMillis();

            if (ApkUtils.isAppInstalled(bean.pkg)) {

                if (!TextUtils.isEmpty(bean.deepLink)) {
                    ApkUtils.startAppBySchema(bean.deepLink);
                } else if (!TextUtils.isEmpty(bean.pkg)) {
                    ApkUtils.startAppByPackageName(bean.pkg);
                }

                intervalReport(5);
                if (!isStartApp) {
                    isStartApp = true;
                    startAppTime = System.currentTimeMillis();
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
                NetHintDialog.showDialog(activity, () -> {
                    isAlreadyHintNet = true;
                    downLoadApp();
                });
            } else {
                ToastUtil.show(UtilsConfig.getApplication(), "当前网络不可用！");
            }


        });

        requestInfo.container.removeAllViews();
        requestInfo.container.addView(integralView);

        createDownload();

    }


    /**
     * 创建下载器
     */
    private void createDownload() {
        if (bean == null) {
            return;
        }
        downloadManager = new DownloadManager(UtilsConfig.getApplication(), bean.pkg, bean.downLoadUrl,
                new DownloadListener() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void updateProgress(long currentLength, long totalLength, int progress) {
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

    private static final long AWARD_TIME = 30 * 1000;

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (progressButton != null && downloadComplete) {
            progressButton.setState(ProgressButton.NORMAL);
            progressButton.setCurrentText("立即安装");
        }
        //默认设置
        if (bean != null && ApkUtils.isAppInstalled(bean.pkg) && progressButton != null) {
            progressButton.setCurrentText("立即试玩");
        }
        //不给奖励或者没有调用启动app
        if (!giveAward || !isStartApp) {
            return;
        }
        //启动app之后

        long tryPlayTime = System.currentTimeMillis() - startAppTime;
        if (tryPlayTime > AWARD_TIME) {
            //  调用金币奖励
            sendTheReward();
        } else {
            showTryPlayDialog((int) ((AWARD_TIME - tryPlayTime) / 1000));
        }

    }

    private boolean tryPlayDialogShowed = false;

    /**
     * 获取奖励剩余时间
     *
     * @param residueTryPlayTime 剩余单位，秒
     */
    private void showTryPlayDialog(int residueTryPlayTime) {
        if (tryPlayDialogShowed) {
            return;
        }
        tryPlayDialogShowed = true;
        TryPlayHintDialog.showDialog(activity, Math.abs(residueTryPlayTime), bean.title, () -> {
            if (bean.status >= 4 && ApkUtils.isAppInstalled(bean.pkg)) {
                if (!TextUtils.isEmpty(bean.deepLink)) {
                    ApkUtils.startAppBySchema(activity, bean.deepLink);
                } else if (!TextUtils.isEmpty(bean.pkg)) {
                    ApkUtils.startAppByPackageName(bean.pkg);
                }
            }
            tryPlayDialogShowed = false;
        }, () -> {
            tryPlayDialogShowed = false;
        });
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }


    private void downLoadApp() {
        if (progressButton != null && downLoadProgress == 0) {
            progressButton.setCurrentText("下载中...");
        }
        downloadComplete = false;

        downloadManager.start();

    }

    /**
     * 刷新积分对象
     */
    private void refreshIntegralBean() {
        EasyHttp.get(UrlCreator.INTEGRAL_APP_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<IntegralBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (e.getCode() == 1) {
                            bean = null;
                            addView(activity);
                        }
                    }

                    @Override
                    public void onSuccess(IntegralBean integralBean) {
                        if (integralBean != null && integralBean.appList != null && integralBean.appList.size() > 0) {
                            bean = integralBean.appList.get(0);
                            IntegralDataSupply.getInstance().setIntegralBean(bean);
                        } else {
                            bean = null;
                        }
                        addView(activity);
                    }
                });
    }

    /**
     * 分发奖励
     */
    private void sendTheReward() {
        if (bean == null) {
            return;
        }
        if (adSdkHttp == null) {
            adSdkHttp = new AdSdkHttp();
        }
        isStartApp = false;
        adSdkHttp.giveOutIntervalAward(bean.pkg, new SimpleCallBack<IntegralAwardBean>() {
            @Override
            public void onError(ApiException e) {
                SdkLogUtils.i(SdkLogUtils.TAG, "--onError : " + e.getMessage());
                isStartApp = false;
            }

            @Override
            public void onSuccess(IntegralAwardBean integralAwardBean) {
                ToastUtil.show(UtilsConfig.getApplication(), " 奖励已发送");
                isStartApp = false;
                //刷新积分对象
                refreshIntegralBean();
            }
        });

    }


    /**
     * 积分墙数据上报
     * 上报数据
     */
    private synchronized void intervalReport(final int status) {

        if (bean == null) {
            return;
        }
        if (adSdkHttp == null) {
            adSdkHttp = new AdSdkHttp();
        }
        IntegralDataSupply.getInstance().appActivateReport(bean);

        adSdkHttp.intervalReport(bean.pkg, bean.name, bean.downLoadUrl,
                bean.deepLink, bean.appIcon, status, bean.type, bean.text, bean.desc,
                new SimpleCallBack<IntegralBean>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(IntegralBean integralBean) {
                        bean = integralBean.appList.get(0);
                    }
                });

    }


}
