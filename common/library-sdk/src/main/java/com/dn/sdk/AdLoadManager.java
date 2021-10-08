package com.dn.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import com.dn.drouter.ARouteHelper;
import com.dn.drouter.annotation.DNMethodRoute;
import com.dn.sdk.api.AdConfigSupply;
import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.bean.AdConfigBean;
import com.dn.sdk.bean.RequestInfo;
import com.dn.sdk.cache.ACache;
import com.dn.sdk.constant.AdType;
import com.dn.sdk.dialog.LoadingDialog;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.lib.donews.ActivityLifecycleListener;
import com.dn.sdk.lib.donews.NewsFeedAdLoadManager;
import com.dn.sdk.lib.donews.RewardAdLoadManager;
import com.dn.sdk.lib.integral.IntegralAd;
import com.dn.sdk.lib.integral.IntegralEvent;
import com.dn.sdk.lib.load.LoadBanner;
import com.dn.sdk.lib.load.LoadCusRender;
import com.dn.sdk.lib.load.LoadFeedTempLate;
import com.dn.sdk.lib.load.LoadInterstitial;
import com.dn.sdk.lib.load.LoadRewardVideo;
import com.dn.sdk.lib.load.LoadSplash;
import com.dn.sdk.lib.load.PreLoadRewardVideo;
import com.dn.sdk.listener.AdPreLoadVideoListener;
import com.dn.sdk.listener.AdSplashListener;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.listener.IAdCallBack;
import com.dn.sdk.listener.IAdNewsFeedListener;
import com.dn.sdk.manager.IntegralDataSupply;
import com.dn.sdk.receiver.IntegralReceiver;
import com.dn.sdk.receiver.PackageReceiver;
import com.dn.sdk.utils.SdkLogUtils;
import com.dn.sdk.widget.AdView;
import com.donews.ad.sdk.JNILibs;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.start.DoNewsAdManagerHolder;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterFragmentPath;
import com.tencent.mmkv.MMKV;

import java.util.LinkedList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public class AdLoadManager {
    private static final AdLoadManager AD_LOAD_MANAGER = new AdLoadManager();
    static volatile boolean hasInit = false;

    public static AdLoadManager getInstance() {
        return AD_LOAD_MANAGER;
    }

    private static Handler mHandle = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (getInstance().getApp() != null) {
                if (msg.what == 1) {
                    String msgText = (String) msg.obj;
                    ToastUtil.show(getInstance().getApp(), msgText);
                }
            }
        }
    };

    /**
     * 发送handle消息
     *
     * @param text String
     */
    private static void sendHandle(String text) {
        Message msg = mHandle.obtainMessage();
        msg.obj = text;
        msg.what = 1;
        mHandle.sendMessage(msg);
    }

    /**
     * @param context   上下文环境
     * @param openDebug false是广告正式环境，对接时候请使用正式环境 设置成false。
     */
    private Context mContext;


    public void init(Application context, boolean openDebug) {
        this.mContext = context;
        MMKV.initialize(context);
        if (hasInit) {

        } else {
            // DnLogSwitchUtils.setEnableLog(BuildConfig.DEBUG);
            DoNewsAdManagerHolder.init(context);
            hasInit = true;


            //应用内广告配置
            getAdConfig();

            //注册activity监听
            context.registerActivityLifecycleCallbacks(new ActivityLifecycleListener());

            ARouteHelper.bind(this);

            // 注册积分墙数据监听
            registerIntegralReceiver(context);

            //apk安装监听
            registerPackageReceiver(context);
        }


    }

    /**
     * 监听积分墙广告数据
     *
     * @param application
     */
    public void registerIntegralReceiver(Application application) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("rpg.rouge");
        application.registerReceiver(new IntegralReceiver(), intentFilter);
    }

    /**
     * apk安装监听
     *
     * @param application 全局
     */
    private void registerPackageReceiver(Application application) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        application.registerReceiver(new PackageReceiver(), intentFilter);

    }


    //获取服务的广告id配置
    private void getAdConfig() {
        AdSdkHttp adSdkHttp = new AdSdkHttp();
        adSdkHttp.getAdConfig();

        adSdkHttp.getIntegralList();
    }

    public Context getApp() {
        return mContext;
    }

    /**
     * 加载开屏广告
     *
     * @param activity
     * @param requestInfo
     * @param splashListener
     */
    public void loadSplash(Activity activity, RequestInfo requestInfo, AdSplashListener splashListener) {
        SdkLogUtils.i(SdkLogUtils.TAG, "************ loadSplash **************");
        new LoadSplash(activity, requestInfo, splashListener).loadAd();
    }


    /**
     * 加载激励视频
     *
     * @param activity      fragmentActivity
     * @param requestInfo   广告信息
     * @param videoListener 回调监听
     */
    public void loadRewardVideo(FragmentActivity activity, RequestInfo requestInfo, AdVideoListener videoListener) {
        loadRewardVideo(activity, true, true, requestInfo, videoListener);
    }

    LinkedList<AdConfigBean.AdID> adIDLinkedList = new LinkedList<>();

    public void loadRewardVideo(FragmentActivity activity, boolean isShowLoading,
                                boolean showToastHint, RequestInfo requestInfo, AdVideoListener videoListener) {
        long intervalTime = (System.currentTimeMillis() - AdConfigSupply.getInstance().getVideoUpPlayTime()) / 1000;

        if (intervalTime < AdConfigSupply.getInstance().getVideoPlayInterval()) {
            String str = String.format("请求频繁，请%s秒之后再试", AdConfigSupply.getInstance().getVideoPlayInterval() - intervalTime);
            //
            if (showToastHint) {
                sendHandle(str);
            }
            if (videoListener != null) {
                videoListener.videoCoolDownIng();
            }
            return;
        }

        if (!checkRequestInfo(activity, requestInfo)) {
            return;
        }

        //服务端配置不播放激励视频
        if (!AdConfigSupply.getInstance().isPlayVideo()) {
            if (videoListener != null) {
                videoListener.onAdClose();
            }
            return;
        }
        SdkLogUtils.i(SdkLogUtils.TAG, "************loadVideo**************");
        SdkLogUtils.i(SdkLogUtils.TAG, "is open cache: " + AdConfigSupply.getInstance().usePreLoadPlayVideo());


        VideoNative videoNative = ACache.getInstance().getVideoNative();

        if (AdConfigSupply.getInstance().usePreLoadPlayVideo() && videoNative != null && videoNative.isReady()) {
            SdkLogUtils.i(SdkLogUtils.TAG, " video from cache");
            fromCachePlayerVideo(videoNative, activity, videoListener);
        } else {
            SdkLogUtils.i(SdkLogUtils.TAG, "********cache is invalid ,online play");
            new LoadRewardVideo(activity, isShowLoading, requestInfo, videoListener).loadAd();
        }

    }

    private void fromCachePlayerVideo(VideoNative videoNative, Activity activity, AdVideoListener videoListener) {
        videoNative.register(new AdVideoListener() {
            @Override
            public void onAdShow() {
                SdkLogUtils.i(SdkLogUtils.TAG, "");
                if (videoListener != null) {
                    videoListener.onAdShow();
                }
            }

            @Override
            public void onAdClose() {
                SdkLogUtils.i(SdkLogUtils.TAG, "");
                if (videoListener != null) {
                    videoListener.onAdClose();
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (videoListener != null) {
                    videoListener.onError(errorCode, errorMsg);
                }
                SdkLogUtils.i(SdkLogUtils.TAG, "-- errcode: " + errorCode + "   errorMsg: " + errorMsg);
            }
        });
        videoNative.showRewardVideoAd(activity);
    }


    /**
     * 预加载激励视频
     *
     * @param activity      activity
     * @param requestInfo   广告信息
     * @param videoListener 回调接口
     * @return
     */
    public VideoNative preLoadRewardVideo(FragmentActivity activity, RequestInfo requestInfo, AdPreLoadVideoListener videoListener) {

        SdkLogUtils.i(SdkLogUtils.TAG, "************ preLoadRewardVideo **************");
        VideoNative videoNative = new VideoNative();

        if (!checkRequestInfo(activity, requestInfo)) {
            return null;
        }

        new PreLoadRewardVideo(activity, videoNative, requestInfo, videoListener).loadAd();

        return videoNative;

    }

    public void cacheRewardVideo(FragmentActivity activity, RequestInfo requestInfo, AdPreLoadVideoListener videoListener) {
        SdkLogUtils.i(SdkLogUtils.TAG, "************ cacheRewardVideo **************");
        if (AdConfigSupply.getInstance().usePreLoadPlayVideo()) {

            VideoNative videoNative = new VideoNative();
            if (!checkRequestInfo(activity, requestInfo)) {
                return;
            }

            new PreLoadRewardVideo(activity, videoNative, requestInfo, videoListener).loadAd();
        }
    }


    /**
     * 预加载全屏视频
     *
     * @param activity      FragmentActivity
     * @param requestInfo   广告信息
     * @param videoListener 监听
     * @return
     */
    public DoNewsAdNative preLoadFullScreenVideo(FragmentActivity activity, RequestInfo requestInfo, AdVideoListener videoListener) {
        if (!checkRequestInfo(activity, requestInfo)) {
            return null;
        }


        RewardAdLoadManager loadManager = new RewardAdLoadManager();
        DoNewsAdNative doNewsAdNative = loadManager.preLoadFullScreenVideo(activity, requestInfo, videoListener);
        if (doNewsAdNative == null) {
            SdkLogUtils.i(SdkLogUtils.TAG, "preFullScreenVideo is null");
        }
        return doNewsAdNative;
    }

    /**
     * 加载全屏视频
     *
     * @param activity      FragmentActivity
     * @param requestInfo   广告信息
     * @param videoListener 监听
     * @return
     */
    public DoNewsAdNative loadFullScreenVideo(FragmentActivity activity, RequestInfo requestInfo, AdVideoListener videoListener) {

        if (!AdConfigSupply.getInstance().isPlayVideo()) {
            if (videoListener != null) {
                videoListener.onAdClose();
            }
            return null;
        }

        if (!checkRequestInfo(activity, requestInfo)) {
            return null;
        }

        requestInfo.adType = AdType.FULL_SCREEN_VIDEO;

        LoadingDialog loadingDialog = new LoadingDialog();
        //默认是6秒
        loadingDialog.setLoadingTime(6)
                .setBackgroundDim(false);
        loadingDialog.show(activity.getSupportFragmentManager(), null);

        RewardAdLoadManager loadManager = new RewardAdLoadManager();

        return loadManager.loadFullScreenVideo(activity, requestInfo, new AdVideoListener() {
            @Override
            public void onAdShow() {
                loadingDialog.dismissCusDialog();

                if (videoListener != null) {
                    videoListener.onAdShow();
                }
            }

            @Override
            public void onAdClose() {

                if (videoListener != null) {
                    videoListener.onAdClose();
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                loadingDialog.dismissCusDialog();

                if (videoListener != null) {
                    videoListener.onError(errorCode, errorMsg);
                }
            }

            @Override
            public void videoCoolDownIng() {

            }
        });

    }


    /**
     * 加载插屏广告
     *
     * @param activity
     * @param requestInfo
     */
    public void loadInterstitial(Activity activity, RequestInfo requestInfo) {
        loadInterstitial(activity, requestInfo, null);
    }

    public void loadInterstitial(Activity activity, RequestInfo requestInfo, IAdCallBack callBack) {
        SdkLogUtils.i(SdkLogUtils.TAG, "************load--Interstitial**************");
        if (!checkRequestInfo(activity, requestInfo)) {
            return;
        }

        new LoadInterstitial(activity, requestInfo, callBack).loadInterstitial();
    }

    public void loadBanner(Activity activity, RequestInfo requestInfo, IAdCallBack callBack) {
        if (!checkRequestInfo(activity, requestInfo)) {
            return;
        }

        new LoadBanner(activity, requestInfo, callBack).loadBanner();

    }


    /**
     * 检查请求信息
     *
     * @param activity    activity
     * @param requestInfo 广告信息
     */
    private boolean checkRequestInfo(Activity activity, RequestInfo requestInfo) {

        if (requestInfo == null) {
            SdkLogUtils.e(SdkLogUtils.TAG, "requestInfo is not empty");
            return false;
        }
        if (activity == null) {
            SdkLogUtils.e(SdkLogUtils.TAG, "activity is not empty");
            return false;
        }

        return true;
    }

    /**
     * 加载信息流模板广告
     *
     * @param activity    activity
     * @param requestInfo 请求信息
     * @param callBack    回调
     */
    public void loadNewsFeedTemplate(Activity activity, RequestInfo requestInfo, IAdCallBack callBack) {
        if (!checkRequestInfo(activity, requestInfo)) {
            return;
        }

        if (requestInfo.container == null) {
            return;
        }
        SdkLogUtils.i(SdkLogUtils.TAG, "************ load  loadNewsFeedTemplate ************** ");
        SdkLogUtils.i(SdkLogUtils.TAG, "is open cache: " + AdConfigSupply.getInstance().usePreLoadPlayVideo());


        View adView = ACache.getInstance().getFeedTemplate();

        if (AdConfigSupply.getInstance().usePreLoadPlayVideo() && adView != null) {
            SdkLogUtils.i(SdkLogUtils.TAG, "FeedTemplate from cache " + ACache.getInstance().getFeedTempCacheSize());

            requestInfo.container.removeAllViews();
            requestInfo.container.addView(adView);

        } else {
            new LoadFeedTempLate(activity, requestInfo, callBack).loadFeedTemplate();
        }
        //当开启缓存，缓存列表中没有数据时，缓存下一条
        if (AdConfigSupply.getInstance().usePreLoadPlayVideo() && ACache.getInstance().getFeedTempCacheSize() == 0) {
            cacheFeedTempLate(activity, requestInfo);
        }
    }

    public void cacheFeedTempLate(Activity activity, RequestInfo requestInfo) {
        SdkLogUtils.i(SdkLogUtils.TAG, "************ cacheFeedTempLate ************** ");
        if (AdConfigSupply.getInstance().usePreLoadPlayVideo()) {
            if (!checkRequestInfo(activity, requestInfo)) {
                return;
            }

            new LoadFeedTempLate(activity, requestInfo, null, true).loadFeedTemplate();
        }
    }


    /**
     * 加载信息流模板广告
     * 注意 layoutId，组件的id，要与 item_news_feed_default 保持一致，布局可以任意摆放
     *
     * @param activity   activity
     * @param positionId 广告id
     * @param adNum      广告数量
     * @param callBack   回调
     */

    public void loadNewsFeedCustomerRender(Activity activity, String positionId, int adNum, IAdNewsFeedListener callBack) {
        loadNewsFeedCustomerRender(activity, positionId, adNum, 0, callBack);
    }

    /**
     * 加载信息流模板广告
     * * 注意 layoutId，组件的id，要与 item_news_feed_default 保持一致，布局可以任意摆放
     *
     * @param activity
     * @param positionId 广告id
     * @param adNum      广告数量
     * @param layoutId   布局id
     * @param callBack   回调
     */
    public void loadNewsFeedCustomerRender(Activity activity, String positionId, int adNum, int layoutId, IAdNewsFeedListener callBack) {
        if (activity == null) {
            throw new RuntimeException("activity is not empty");
        }

        RequestInfo requestInfo = new RequestInfo(positionId);
        requestInfo.adNum = adNum;
        requestInfo.adType = AdType.NEWS_FEED_CUSTOM_RENDER;

        new LoadCusRender(activity, requestInfo, layoutId, callBack).loadCusRender();
    }


    /**
     * 预加载加载信息流模板广告，放入缓存中
     *
     * @param activity   activity
     * @param positionId 广告id
     * @param adNum      广告数量
     */
    public void preLoadNewsFeedCustomerRender(Activity activity, String positionId, int adNum) {
        preLoadNewsFeedCustomerRender(activity, 0, positionId, adNum);
    }

    public void preLoadNewsFeedCustomerRender(Activity activity, int layoutId, String positionId, int adNum) {
        if (activity == null) {
            if (activity == null) {
                throw new RuntimeException("activity is not empty");
            }
        }

        RequestInfo requestInfo = new RequestInfo(positionId);
        requestInfo.adNum = adNum;
        requestInfo.adType = AdType.NEWS_FEED_CUSTOM_RENDER;

        NewsFeedAdLoadManager loadManager = new NewsFeedAdLoadManager();

        loadManager.loadNewsFeedCustomRender(activity, requestInfo, layoutId, new IAdNewsFeedListener() {
            @Override
            public void success(List<AdView> viewList) {
                ACache.getInstance().saveCustomRender(viewList);
            }

            @Override
            public void onError(String errorMsg) {

            }
        });
    }

    /**
     * @param view 误点击视图
     * @param x    误点击X
     * @param y    误点击Y
     */
    public void sendViewClick(ViewGroup view, int x, int y) {
        JNILibs.send(view, x, y);
    }

    /**
     * @return true 有积分墙数据
     */
    public boolean hasIntegral() {
        return IntegralDataSupply.getInstance().getServerIntegralBean() != null;
    }

    /**
     * 积分墙点击
     */
    public void sendIntegralClick() {
        IntegralEvent eventManager = new IntegralEvent();
        eventManager.onClick();

    }


    /**
     * 刷新广告ID配置
     */
    @DNMethodRoute(RouterFragmentPath.MethodPath.AD_LOAD_MANAGER_REFRESH_AD_CONFIG)
    public void refreshAdConfig() {
        getAdConfig();
    }

    /**
     * 获取积分墙广告
     */

    public void loadIntegralAd(FragmentActivity activity, Lifecycle lifecycle, boolean giveAward, RequestInfo requestInfo) {
        new IntegralAd().loadAd(activity, lifecycle, giveAward, requestInfo);
    }
}
