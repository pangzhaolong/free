package com.dn.sdk.lib.integral;

import android.text.TextUtils;

import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.api.UrlCreator;
import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.receiver.PackageReceiver;
import com.dn.sdk.utils.ApkUtils;
import com.donews.common.download.DownloadListener;
import com.donews.common.download.DownloadManager;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.NetworkUtils;

/**
 * @author by SnowDragon
 * Date on 2021/3/31
 * Description:
 */
public class IntegralEvent {
    public void onClick() {
        IntegralBean.DataBean bean = IntegralDataSupply.getInstance().getServerIntegralBean();
        if (bean != null) {
            executeClick(bean);
        } else {
            getIntegralBean();
        }

    }

    /**
     * 获取服务端积分列表
     */
    private void getIntegralBean() {
        EasyHttp.get(UrlCreator.INTEGRAL_APP_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<IntegralBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(IntegralBean integralBean) {
                        if (integralBean != null && integralBean.appList != null && integralBean.appList.size() > 0) {
                            executeClick(integralBean.appList.get(0));

                        }

                    }
                });
    }

    private void executeClick(IntegralBean.DataBean bean) {
        if (bean.status >= 4 && ApkUtils.isAppInstalled(bean.pkg)) {
            if (!TextUtils.isEmpty(bean.deepLink)) {
                ApkUtils.startAppBySchema(bean.deepLink);
            } else if (!TextUtils.isEmpty(bean.pkg)) {
                ApkUtils.startAppByPackageName(bean.pkg);
            }
            intervalReport(bean, 5);
            return;
        }
        //wifi连接
        if (NetworkUtils.isWifiConnected()) {
            downLoadApp(bean);
        }
        PackageReceiver.installHashMap.put(PackageReceiver.RECEIVER_AD_CLICK,
                new PackageReceiver.InstallListener() {
                    @Override
                    public void installComplete(String packageName) {
                        if (bean != null && packageName.equalsIgnoreCase(bean.pkg)) {
                            bean.status = 4;
                            intervalReport(bean, 4);
                        }

                    }

                    @Override
                    public void activateComplete(String packageName) {
                        if (bean != null && packageName.equalsIgnoreCase(bean.pkg)) {
                            intervalReport(bean, 5);
                        }

                    }
                });
    }

    private void downLoadApp(IntegralBean.DataBean bean) {
        DownloadManager downloadManager = new DownloadManager(UtilsConfig.getApplication(), bean.pkg, bean.downLoadUrl, new DownloadListener() {
            @Override
            public void updateProgress(int progress) {

            }

            @Override
            public void downloadComplete(String pkName, String path) {
                PackageReceiver.installPackageHashMap.put(pkName, path);
                intervalReport(bean, 3);
            }

            @Override
            public void downloadError(String error) {

            }
        });

        downloadManager.start();
    }

    /**
     * 积分墙数据上报
     * 上报数据
     */
    private synchronized void intervalReport(IntegralBean.DataBean bean, final int status) {
        if (bean == null) {
            return;
        }
        AdSdkHttp adSdkHttp = new AdSdkHttp();
        IntegralDataSupply.getInstance().appActivateReport("mistakeClickAppActivate", bean);

        adSdkHttp.intervalReport(bean.pkg, bean.name, bean.downLoadUrl,
                bean.deepLink, bean.appIcon, status, bean.type, bean.text, bean.desc);

    }
}
