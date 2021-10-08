package com.dn.sdk.count;

import com.dn.sdk.AdLoadManager;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SDKType;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.network.down.MD5Util;
import com.donews.utilslibrary.analysis.AnalysisHelp;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public class CountTrackImpl implements ITrack {
    private static final String TAG = CountTrackImpl.class.getSimpleName();

    /**
     * 广告类型
     */
    private final AdType adType;
    private final SDKType sdkType;
    private final RequestInfo rInfo;


    public CountTrackImpl(RequestInfo rInfo) {
        this.sdkType = rInfo.getSdkType();
        this.adType = rInfo.adType;

        String key = AdLoadManager.getInstance().getApp().getPackageName() + System.currentTimeMillis();
        rInfo.requestId = MD5Util.getFileMD5(key.getBytes());

        this.rInfo = rInfo;

        event(rInfo.preLoad ? Event.AD_LOADING : Event.AD_ACTIVITY);

        if (adType == AdType.SPLASH || adType == AdType.REWARD_VIDEO) {
            IntegralDataSupply.getInstance().setRequestInfo(rInfo);
        }
    }

    @Override
    public void onClick() {
        event(Event.AD_CLICK);
    }

    @Override
    public void onShow() {
        SdkLogUtils.i(SdkLogUtils.TAG, "--onShow");
        event(Event.AD_SHOW);
    }

    @Override
    public void onAdClose() {
        event(Event.AD_CLOSE);
    }

    @Override
    public void onLoadError() {

    }

    @Override
    public void onADExposure() {

    }

    @Override
    public void onRewardVerify(boolean b) {

    }

    @Override
    public void onVideoComplete() {
        event(Event.AD_COMPLETE);
    }

    @Override
    public void downloadFinished() {
        event(Event.AD_DOWNLOAD_FINISHED);
    }


    private void event(String eventName) {
        SdkLogUtils.i(SdkLogUtils.TAG, "EventName: " + eventName + " sdk: " + sdkType.DESCRIPTION + " adType: "
                + adType.DESCRIPTION + " adId: " + rInfo.id);

        AnalysisHelp.onEvent(AdLoadManager.getInstance().getApp(),
                eventName, sdkType.DESCRIPTION, adType.DESCRIPTION, rInfo.id, rInfo.requestId);

    }


}
