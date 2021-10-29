package com.dn.sdk.sdk.tt.helper;

import android.app.Activity;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo;
import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTNetworkRequestInfo;
import com.bytedance.msdk.api.TTSettingConfigCallback;
import com.bytedance.msdk.api.TTVideoOption;
import com.dn.admediation.csj.bean.DnTTSplashAd;
import com.dn.admediation.csj.listener.DnTTSplashAdListener;
import com.dn.admediation.csj.listener.DnTTSplashAdLoadCallback;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdSplashListener;
import com.dn.sdk.sdk.tt.utils.VideoOptionUtil;
import com.donews.utilslibrary.utils.DensityUtils;

/**
 * 开屏广告加载辅助类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 16:09
 */
public class TTAdSplashLoadHelper {

	private Activity mActivity;
	private RequestInfo mRequestInfo;
	private IAdSplashListener mListener;

	private DnTTSplashAd mDnTTSplashAd;

	private final DnTTSplashAdListener mSplashAdListener = new DnTTSplashAdListener() {
		@Override
		public void onAdClicked() {
			if (mListener != null) {
				mListener.onAdClicked();
			}
		}

		@Override
		public void onAdShow() {
			if (mListener != null) {
				mListener.onAdShow();
			}
		}


		@Override
		public void onAdSkip() {
			if (mListener != null) {
				mListener.onAdSkip();
			}
		}

		@Override
		public void onAdDismiss() {
			if (mListener != null) {
				mListener.onAdDismiss();
			}
		}
	};

	private final TTSettingConfigCallback mSettingConfigCallback = new TTSettingConfigCallback() {
		@Override
		public void configLoad() {
			load();
		}
	};

	public void loadSplashAd(Activity activity, RequestInfo requestInfo, IAdSplashListener listener) {
		this.mActivity = activity;
		this.mRequestInfo = requestInfo;
		this.mListener = listener;

		DisplayMetrics outMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		if (requestInfo.getWidth() == 0) {
			requestInfo.setWidth((int) DensityUtils.px2dp(activity, outMetrics.widthPixels));
		}
		if (requestInfo.getHeight() == 0) {
			requestInfo.setHeight((int) DensityUtils.px2dp(activity, outMetrics.heightPixels));
		}
		if (TTMediationAdSdk.configLoadSuccess()) {
			load();
		} else {
			TTMediationAdSdk.registerConfigCallback(mSettingConfigCallback);
		}
	}


	private void load() {
		mDnTTSplashAd = new DnTTSplashAd(mActivity, mRequestInfo.getAdId());
		mDnTTSplashAd.setTTAdSplashListener(mSplashAdListener);


		//gdt开屏预加载设置选项 （可选配置）
		TTVideoOption videoOption = VideoOptionUtil.getSplashProLoadTTVideoOption();

		//step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
		AdSlot adSlot = new AdSlot.Builder()
				.setTTVideoOption(videoOption)
				.setImageAdSize(mRequestInfo.getWidth(), mRequestInfo.getHeight())
				.build();

		TTNetworkRequestInfo ttNetworkRequestInfo = new PangleNetworkRequestInfo(
				mRequestInfo.getAppId(), mRequestInfo.getMinimumCodeId());

		if (mActivity instanceof AppCompatActivity) {
			((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						TTMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback);
						if (mDnTTSplashAd != null) {
							mDnTTSplashAd.destroy();
						}
						((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
						mActivity = null;
					}
				}
			});
		}
		mDnTTSplashAd.loadAd(adSlot, ttNetworkRequestInfo, new DnTTSplashAdLoadCallback() {
			@Override
			public void onSplashAdLoadFail(AdError adError) {
				if (mListener != null) {
					mListener.onLoadFail(adError.code, adError.toString());
					mListener.onError(adError.code, adError.toString());
				}
			}

			@Override
			public void onSplashAdLoadSuccess() {
				mDnTTSplashAd.showAd(mRequestInfo.getContainer());
				if (mListener != null) {
					mListener.onLoad();
				}

			}

			@Override
			public void onAdLoadTimeout() {
				if (mListener != null) {
					mListener.onLoadTimeout();
					mListener.onError(AdError.ERROR_CODE_LOAD_AD_TIMEOUT, "广告加载超时");
				}
			}
		}, 3000);
	}
}
