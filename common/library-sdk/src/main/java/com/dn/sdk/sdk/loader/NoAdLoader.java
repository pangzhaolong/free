package com.dn.sdk.sdk.loader;

import android.app.Activity;
import android.content.Context;

import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.bean.SDKType;
import com.dn.sdk.sdk.interfaces.listener.IAdBannerListener;
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener;
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeListener;
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener;
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener;
import com.dn.sdk.sdk.interfaces.loader.ILoader;


/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 11:03
 */
public class NoAdLoader implements ILoader {
    @Override
    public SDKType getSdkType() {
        return SDKType.NO_AD;
    }

    @Override
    public void loadSplashAd(Activity activity, RequestInfo requestInfo, IAdSplashListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void loadBannerAd(Activity activity, RequestInfo requestInfo, IAdBannerListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void loadInterstitialAd(Activity activity, RequestInfo requestInfo, IAdInterstitialListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void loadRewardVideoAd(Activity activity, RequestInfo requestInfo, IAdRewardVideoListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void preloadRewardViewAd(Activity activity, RequestInfo requestInfo,
            IAdPreloadVideoViewListener viewListener, IAdRewardVideoListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void loadFullVideoAd(Activity activity, RequestInfo requestInfo, IAdFullVideoListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void preloadFullVideoAd(Activity activity, RequestInfo requestInfo, IAdPreloadVideoViewListener viewListener,
            IAdFullVideoListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void loadFeedNativeAd(Context context, RequestInfo requestInfo, IAdNativeListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }

    @Override
    public void loadFeedNativeExpressAd(Context context, RequestInfo requestInfo, IAdNativeExpressListener listener) {
        if (listener != null) {
            listener.onLoadFail(-1, "已关闭广告");
            listener.onError(-1, "已关闭广告");
        }
    }
}
