package com.dn.sdk.sdk.tt.helper;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTSettingConfigCallback;
import com.bytedance.msdk.api.TTVideoOption;
import com.dn.admediation.csj.bean.DnTTInterstitialAd;
import com.dn.admediation.csj.listener.DnTTInterstitialAdListener;
import com.dn.admediation.csj.listener.DnTTInterstitialAdLoadCallback;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdInterstitialListener;
import com.dn.sdk.sdk.tt.utils.VideoOptionUtil;

/**
 * 插屏广告加载辅助类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 16:47
 */
public class TTAdInterstitialLoadHelper {

    private Activity mActivity;
    private RequestInfo mRequestInfo;
    private IAdInterstitialListener mListener;
    private DnTTInterstitialAd mDnInterstitialAd;

    private final DnTTInterstitialAdListener mAdListener = new DnTTInterstitialAdListener() {

        @Override
        public void onInterstitialShow() {
            if (mListener != null) {
                mListener.onAdShow();
            }
        }

//		@Override
//		public void onInterstitialShowFail(AdError adError) {
//			if (mListener != null) {
//				mListener.onAdShowFail(adError.code, adError.toString());
//				mListener.onError(adError.code,adError.toString());
//			}
//		}

        @Override
        public void onInterstitialAdClick() {
            if (mListener != null) {
                mListener.onAdClicked();
            }
        }

        @Override
        public void onInterstitialClosed() {
            if (mListener != null) {
                mListener.onAdClosed();
            }
        }

        @Override
        public void onAdOpened() {
            if (mListener != null) {
                mListener.onAdOpened();
            }
        }

        @Override
        public void onAdLeftApplication() {
            if (mListener != null) {
                mListener.onAdLeftApplication();
            }
        }
    };

    private final TTSettingConfigCallback mSettingConfigCallback = new TTSettingConfigCallback() {
        @Override
        public void configLoad() {
            load();
        }
    };

    public void loadAd(Activity activity, RequestInfo requestInfo, IAdInterstitialListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;
        if (TTMediationAdSdk.configLoadSuccess()) {
            load();
        } else {
            TTMediationAdSdk.registerConfigCallback(mSettingConfigCallback);
        }
    }


    private void load() {
        mDnInterstitialAd = new DnTTInterstitialAd(mActivity, mRequestInfo.getAdId());
        mDnInterstitialAd.setTTAdInterstitialListener(mAdListener);

        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        TTMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback);
                        if (mDnInterstitialAd != null) {
                            mDnInterstitialAd.destroy();
                        }
                        ((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
                        mActivity = null;
                    }
                }
            });
        }


        TTVideoOption videoOption = VideoOptionUtil.getTTVideoOption(mRequestInfo.isOpenSound());

        //创建插屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                // 注意：插屏暂时支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD)
                .setTTVideoOption(videoOption)
                .setImageAdSize(mRequestInfo.getWidth(), mRequestInfo.getHeight())
                .build();
        //请求广告，调用插屏广告异步请求接口
        mDnInterstitialAd.loadAd(adSlot, new DnTTInterstitialAdLoadCallback() {
            @Override
            public void onInterstitialLoadFail(AdError adError) {
                if (mListener != null) {
                    mListener.onLoadFail(adError.code, adError.toString());
                    mListener.onError(adError.code, adError.toString());
                }
            }

            @Override
            public void onInterstitialLoad() {
                if (mListener != null) {
                    mListener.onLoad();
                }
                if (mDnInterstitialAd.isReady()) {
                    mDnInterstitialAd.showAd(mActivity);
                }
            }
        });

    }

}
