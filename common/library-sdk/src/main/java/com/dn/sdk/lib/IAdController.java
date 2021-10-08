package com.dn.sdk.lib;

import android.app.Activity;

import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdPreLoadVideoListener;
import com.dn.sdk.listener.AdSplashListener;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.listener.IAdNewsFeedListener;

/**
 * @author by SnowDragon
 * Date on 2021/1/15
 * Description:
 */
public interface IAdController {
    /**
     * 开屏广告
     *
     * @param activity
     * @param requestInfo
     * @param splashListener
     */
    void loadAdSplash(Activity activity, RequestInfo requestInfo, AdSplashListener splashListener);

    void loadVideo(Activity activity, RequestInfo requestInfo, AdVideoListener videoListener);

    VideoNative preLoadVideo(Activity mActivity, RequestInfo requestInfo, AdPreLoadVideoListener videoListener);


    void loadNewsFeedTemplate(Activity activity, RequestInfo requestInfo,boolean cacheAd, IAdCallBack iAdCallBack);

    void loadNewsFeedCustomRender(Activity activity, RequestInfo requestInfo, int layoutId, IAdNewsFeedListener listener);

    void loadInterstitial(Activity activity, RequestInfo requestInfo, IAdCallBack iAdCallBack);

    void loadBanner(Activity activity, RequestInfo requestInfo, IAdCallBack iAdCallBack);
}

