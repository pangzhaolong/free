package com.dn.sdk.sdk.tt.helper;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTAdConstant;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTSettingConfigCallback;
import com.bytedance.msdk.api.TTVideoOption;
import com.dn.admediation.csj.bean.DnTTFullVideoAd;
import com.dn.admediation.csj.listener.DnTTFullVideoAdListener;
import com.dn.admediation.csj.listener.DnTTFullVideoAdLoadCallback;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener;
import com.dn.sdk.sdk.interfaces.view.PreloadFullVideoView;
import com.dn.sdk.sdk.tt.bean.DnTTPreloadFullVideoView;
import com.dn.sdk.sdk.tt.utils.VideoOptionUtil;

/**
 * 全屏视频辅助加载类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 17:58
 */
public class TTAdFullVideoLoadHelper {

    private Activity mActivity;
    private RequestInfo mRequestInfo;
    private IAdFullVideoListener mListener;
    private IAdPreloadVideoViewListener mPreloadVideoViewListener;
    private DnTTFullVideoAd mDnTTFullVideoAd;


    private final DnTTFullVideoAdListener mAdListener = new DnTTFullVideoAdListener() {


        @Override
        public void onFullVideoAdShow() {
            if (mListener != null) {
                mListener.onFullVideoAdShow();
            }
        }

//		@Override
//		public void onFullVideoAdShowFail(AdError adError) {
//			if (mListener != null) {
//				mListener.onFullVideoAdShowFail(adError.code, adError.toString());
//				mListener.onError(adError.code, adError.toString());
//			}
//		}

        @Override
        public void onFullVideoAdClick() {
            if (mListener != null) {
                mListener.onFullVideoClick();
            }
        }

        @Override
        public void onFullVideoAdClosed() {
            if (mListener != null) {
                mListener.onFullVideoClosed();
            }
        }

        @Override
        public void onVideoComplete() {
            if (mListener != null) {
                mListener.onFullVideoComplete();
            }
        }

        @Override
        public void onVideoError() {
            if (mListener != null) {
                mListener.onFullVideoError();
                mListener.onError(AdError.ERROR_CODE_UNKNOWN_ERROR, "Video 播放错误");
            }
        }

        @Override
        public void onSkippedVideo() {
            if (mListener != null) {
                mListener.onSkippedFullVideo();
            }
        }
    };

    private final TTSettingConfigCallback mLoadCallback = new TTSettingConfigCallback() {
        @Override
        public void configLoad() {
            load();
        }
    };

    private final TTSettingConfigCallback mPreloadCallback = new TTSettingConfigCallback() {
        @Override
        public void configLoad() {
            preload();
        }
    };

    public void loadAd(Activity activity, RequestInfo requestInfo, IAdFullVideoListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mListener = listener;
        if (TTMediationAdSdk.configLoadSuccess()) {
            load();
        } else {
            TTMediationAdSdk.registerConfigCallback(mLoadCallback);
        }
    }


    public void preloadAd(Activity activity, RequestInfo requestInfo, IAdPreloadVideoViewListener viewListener,
            IAdFullVideoListener listener) {
        this.mActivity = activity;
        this.mRequestInfo = requestInfo;
        this.mPreloadVideoViewListener = viewListener;
        this.mListener = listener;
        if (TTMediationAdSdk.configLoadSuccess()) {
            preload();
        } else {
            TTMediationAdSdk.registerConfigCallback(mPreloadCallback);
        }
    }

    private void load() {
        mDnTTFullVideoAd = new DnTTFullVideoAd(mActivity, mRequestInfo.getAdId());

        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        TTMediationAdSdk.unregisterConfigCallback(mLoadCallback);
                        mDnTTFullVideoAd.destroy();
                        ((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
                        mActivity = null;
                    }
                }
            });
        }


        TTVideoOption videoOption = VideoOptionUtil.getTTVideoOption();

        //创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD)
                .setTTVideoOption(videoOption)
                .setOrientation(TTAdConstant.VERTICAL)
                .setUserID(mRequestInfo.getUseId())
                .setCustomData(mRequestInfo.getCustomData())
                .build();

        mDnTTFullVideoAd.loadFullAd(adSlot, new DnTTFullVideoAdLoadCallback() {
            @Override
            public void onFullVideoLoadFail(AdError adError) {
                if (mListener != null) {
                    mListener.onLoadFail(adError.code, adError.toString());
                    mListener.onError(adError.code, adError.toString());
                }
            }

            @Override
            public void onFullVideoAdLoad() {
                if (mListener != null) {
                    mListener.onLoad();
                }
                mDnTTFullVideoAd.showFullAd(mActivity, mAdListener);
            }

            @Override
            public void onFullVideoCached() {
                if (mListener != null) {
                    mListener.onLoadCached();
                }
            }
        });
    }

    private void preload() {
        mDnTTFullVideoAd = new DnTTFullVideoAd(mActivity, mRequestInfo.getAdId());

        PreloadFullVideoView fullVideoView = new DnTTPreloadFullVideoView(mActivity, mDnTTFullVideoAd, mAdListener);

        if (mActivity instanceof AppCompatActivity) {
            ((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        TTMediationAdSdk.unregisterConfigCallback(mLoadCallback);
                        if (mDnTTFullVideoAd != null) {
                            mDnTTFullVideoAd.destroy();
                        }
                        ((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
                        mActivity = null;
                    }
                }
            });
        }


        TTVideoOption videoOption = VideoOptionUtil.getTTVideoOption();

        //创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setAdStyleType(AdSlot.TYPE_EXPRESS_AD)
                .setTTVideoOption(videoOption)
                .setOrientation(TTAdConstant.VERTICAL)
                .setUserID(mRequestInfo.getUseId())
                .setCustomData(mRequestInfo.getCustomData())
                .build();

        mDnTTFullVideoAd.loadFullAd(adSlot, new DnTTFullVideoAdLoadCallback() {
            @Override
            public void onFullVideoLoadFail(AdError adError) {
                if (mListener != null) {
                    mListener.onLoadFail(adError.code, adError.toString());
                    mListener.onError(adError.code, adError.toString());
                }
            }

            @Override
            public void onFullVideoAdLoad() {
                if (mListener != null) {
                    mListener.onLoad();
                }
                mDnTTFullVideoAd.showFullAd(mActivity, mAdListener);
            }

            @Override
            public void onFullVideoCached() {
                if (mListener != null) {
                    mListener.onLoadCached();
                }
            }
        });

        if (mPreloadVideoViewListener != null) {
            mPreloadVideoViewListener.OnLoadVideoView(fullVideoView);
        }
    }
}
