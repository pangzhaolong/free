package com.dn.sdk.lib.load;

import android.app.Activity;

import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.lib.SdkManager;
import com.dn.sdk.listener.AdSplashListener;

import java.util.LinkedList;

/**
 * @author by SnowDragon
 * Date on 2021/1/19
 * Description:
 */
public class LoadSplash {
    LinkedList<AdConfigBean.AdID> adIdS;

    private final Activity activity;
    private final RequestInfo requestInfo;
    private final AdSplashListener adListener;

    public LoadSplash(Activity activity, RequestInfo requestInfo, AdSplashListener adListener) {
        this.activity = activity;
        this.requestInfo = requestInfo;
        this.adListener = adListener;
        requestInfo.adType = AdType.SPLASH;
    }

    public void loadAd() {
        adIdS = AdConfigSupply.getInstance().getAdIdList(requestInfo.adType);
        loadSplash();
    }

    private void loadSplash() {
        if (adIdS.isEmpty()) {
            if (adListener != null) {
                adListener.onNoAD("加载失败");
            }
            return;
        }
        AdConfigBean.AdID adId = adIdS.poll();
        AdConfigSupply.getInstance().wrapperRequestInfo(adId, requestInfo);

        SdkManager.getInstance().getAdController(requestInfo.getSdkType())
                .loadAdSplash(activity, requestInfo, new AdSplashListener() {
                    @Override
                    public void onNoAD(String s) {
                        requestInfo.usePassId = false;
                        loadSplash();
                    }

                    @Override
                    public void onClicked() {
                        if (adListener != null) {
                            adListener.onClicked();
                        }
                    }

                    @Override
                    public void onShow() {
                        if (adListener != null) {
                            adListener.onShow();
                        }

                    }

                    @Override
                    public void onPresent() {
                        if (adListener != null) {
                            adListener.onPresent();
                        }
                    }

                    @Override
                    public void onADDismissed() {
                        if (adListener != null) {
                            adListener.onADDismissed();
                        }
                    }

                    @Override
                    public void extendExtra(String s) {

                    }
                });

    }
}
