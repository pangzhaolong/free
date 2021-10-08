package com.dn.sdk.lib.donews;

import android.app.Activity;

import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.count.CountTrackImpl;
import com.dn.sdk.listener.AdSplashListener;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;


/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
class SplashAdLoadManager {
    private static final String TAG = "SplashAdLoadManager";

    public void loadAd(Activity activity, RequestInfo requestInfo, AdSplashListener splashListener) {

        SdkLogUtils.i(SdkLogUtils.TAG, " id； " + requestInfo.id);

        DoNewsAD doNewsAD = new DoNewsAD.Builder()
                .setPositionid(requestInfo.id)
                .setView(requestInfo.container)
                .build();

        requestInfo.adType = AdType.SPLASH;
        DoNewsAdNative doNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
        CountTrackImpl track = new CountTrackImpl(requestInfo);

        doNewsAdNative.onCreateAdSplash(activity, doNewsAD, new DoNewsAdNative.SplashListener() {
            @Override
            public void onNoAD(String s) {
                SdkLogUtils.i(SdkLogUtils.TAG, " : " + s);
                track.onLoadError();

                if (splashListener != null) {
                    splashListener.onNoAD(s);
                }
            }

            @Override
            public void onClicked() {
                track.onClick();

                if (splashListener != null) {
                    splashListener.onClicked();
                }
            }

            @Override
            public void onShow() {
                SdkLogUtils.e(TAG, "");
                SdkLogUtils.i(SdkLogUtils.TAG, "");
                track.onShow();

                if (splashListener != null) {
                    splashListener.onShow();
                }
            }

            @Override
            public void onPresent() {
                SdkLogUtils.i(SdkLogUtils.TAG, "");
                track.onADExposure();

                if (splashListener != null) {
                    splashListener.onPresent();
                }
            }

            @Override
            public void onADDismissed() {
                SdkLogUtils.i(SdkLogUtils.TAG, "");
                track.onAdClose();

                if (splashListener != null) {
                    splashListener.onADDismissed();
                }
            }

            @Override
            public void extendExtra(String s) {

                if (splashListener != null) {
                    splashListener.extendExtra(s);
                }
            }
        });
    }


}
