package com.donews.utilslibrary.datacenter;

import android.app.Application;

import com.cdyfnts.datacenter.api.YFDot;
import com.cdyfnts.datacenter.callback.YFInitCallBack;
import com.cdyfnts.datacenter.entity.AdActionBean;


public class YfDcHelper {
    public static void init(Application context, String channelName, boolean isDebug) {
        YFDot.init(context, channelName, isDebug, new YFInitCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        });
    }

    public static boolean isInit() {
        return YFDot.isInit();
    }

    public static void refreshSuuid(String suuid) {
        YFDot.refreshSuuid(suuid);
    }

    public static void onDeviceEvent() {
        YFDot.onDeviceEvent();
    }

    public static void onAdActionEvent(AdActionBean bean) {
        YFDot.onAdActionEvent(bean);
    }
}
