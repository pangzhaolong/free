package com.dn.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.bean.IntegralOriginalBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SDKType;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.base.UtilsConfig;

import java.util.HashMap;


/**
 * @author by SnowDragon
 * Date on 2021/3/29
 * Description: 监听广告数据，只有 开屏和app 下载类广告
 */
public class IntegralReceiver extends BroadcastReceiver {
    HashMap<String, RequestInfo> requestInfoHashMap = new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String reqId = intent.getStringExtra("reqId");
        /*
         * 1代表ad request，
         * 10代表ad show，
         * 20代表ad click，
         * 30代表ad start download，
         * 40代表download success and start install，
         * 41代表install success，
         * 50代表open app
         *
         * */
        int status = intent.getIntExtra("status", -1);


        String metaId = intent.getStringExtra("metaId");
        String metadata = intent.getStringExtra("metadata");

        // 广告数据不为 null
        if (!TextUtils.isEmpty(metadata)) {
            Log.i("CrashHandlerUtil","---");
            IntegralDataSupply.getInstance().addIntegralData(reqId, metadata);
        }
//        SdkLogUtils.i(SdkLogUtils.TAG, "integralReceiver: " + reqId + " status " + status + " metadata: " + metadata);

        checkReportType(status, reqId);
    }

    private void checkReportType(int status, String reqId) {
        IntegralOriginalBean bean = IntegralDataSupply.getInstance().getIntegralOriginalBean(reqId);
        SdkLogUtils.i(SdkLogUtils.TAG, "integralReceiver: " + reqId + " status " + status + " checkReportType bean : " + bean);
        switch (status) {
            case 1:
                // ad request
                break;
            case 10:
                if (requestInfoHashMap.get(reqId) == null) {
                    requestInfoHashMap.put(reqId, IntegralDataSupply.getInstance().getRequestInfo());
                }
                // ad show
                SdkLogUtils.i(SdkLogUtils.TAG, "---------onShow");
                break;
            case 20:
                if (bean != null && bean.isApp()) {
                    // MWC adClickApp
                    intervalReport(1, reqId, bean);

                    //大数据统计
                    event("adClickApp", reqId, bean.getPackageName(),bean.getName());
                }
                //大数据上报;
                break;
            case 30:
                //adStartDownload
                if (bean != null) {
                    intervalReport(2, reqId, bean);
                    event("adStartDownload", reqId, bean.getPackageName(),bean.getName());
                }
                break;
            case 40:
                // adDownloadComplete
                if (bean != null) {
                    intervalReport(3, reqId, bean);
                    event("adDownloadComplete", reqId, bean.getPackageName(),bean.getName());
                }
                break;
            case 41:
                // adInstallSuccess
                if (bean != null) {
                    intervalReport(4, reqId, bean);
                    event("adInstallSuccess", reqId, bean.getPackageName(),bean.getName());
                }
                break;
            case 50:
                // adOpenApp
                if (bean != null) {
                    intervalReport(5, reqId, bean);
                    event("adOpenApp", reqId, bean.getPackageName(),bean.getName());
                }
                break;
            default:

        }
    }

    /**
     * 上报数据
     */
    private void intervalReport(int status, String reqId, IntegralOriginalBean bean) {

        if (bean == null) {
            return;
        }
        AdSdkHttp adSdkHttp = new AdSdkHttp();

        adSdkHttp.intervalReport(bean.getPackageName(), bean.getName(), bean.getDownloadUrl(),
                bean.getDeepLink(), bean.geIconUrl(), status, bean.type,bean.getTxt(),bean.getDesc());
    }

    /**
     * 大数据上报
     *
     * @param eventName 事件名称
     */
    private void event(String eventName, String reqId, String pkName,String appName) {
        RequestInfo rInfo = requestInfoHashMap.get(reqId);
        if (rInfo == null) {
            return;
        }
        SDKType sdkType = rInfo.getSdkType();
        AdType adType = rInfo.adType;

        SdkLogUtils.i(SdkLogUtils.TAG, "EventName: " + eventName + " sdk: " + sdkType.DESCRIPTION + " adType: "
                + adType.DESCRIPTION + " adId: " + rInfo.id + " pkName: " + pkName + " requestId : " + rInfo.requestId+" appName "+appName);

        AnalysisHelp.onEvent(UtilsConfig.getApplication(),
                eventName, sdkType.DESCRIPTION, adType.DESCRIPTION, rInfo.id, rInfo.requestId, pkName,appName);

    }
}
