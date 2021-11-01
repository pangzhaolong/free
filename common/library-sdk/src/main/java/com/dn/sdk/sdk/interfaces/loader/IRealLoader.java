package com.dn.sdk.sdk.interfaces.loader;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

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


/**
 * 广告加载类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:42
 */
public interface IRealLoader {

    /**
     * 当前loader使用的SDK Type
     *
     * @return {@link SDKType}
     */
    SDKType getSdkType();

    /**
     * 开屏广告 (启动广告)
     */
    void loadSplashAd(Activity activity, RequestInfo requestInfo, @Nullable IAdSplashListener listener);

    /**
     * Banner广告
     */
    void loadBannerAd(Activity activity, RequestInfo requestInfo, IAdBannerListener listener);

    /**
     * 插屏广告(弹窗形式广告)
     */
    void loadInterstitialAd(Activity activity, RequestInfo requestInfo, IAdInterstitialListener listener);

    /**
     * 激励视频广告
     */
    void loadRewardVideoAd(Activity activity, RequestInfo requestInfo, IAdRewardVideoListener listener);

    /**
     * 预加载激励视频广告
     */
    void preloadRewardViewAd(Activity activity, RequestInfo requestInfo,
            IAdPreloadVideoViewListener viewListener, IAdRewardVideoListener listener);

    /**
     * 全屏视频
     */
    void loadFullVideoAd(Activity activity, RequestInfo requestInfo, IAdFullVideoListener listener);

    /**
     * 预加载全屏视频
     */
    void preloadFullVideoAd(Activity activity, RequestInfo requestInfo,
            IAdPreloadVideoViewListener viewListener, IAdFullVideoListener listener);

    /**
     * 信息流广告 (自渲染，需要自己渲染处理显示)
     */
    void loadFeedNativeAd(Context context, RequestInfo requestInfo, IAdNativeListener listener);

    /**
     * 模板信息流 (框架渲染，无需自己处理显示)
     */
    void loadFeedNativeExpressAd(Context context, RequestInfo requestInfo, IAdNativeExpressListener listener);
}
