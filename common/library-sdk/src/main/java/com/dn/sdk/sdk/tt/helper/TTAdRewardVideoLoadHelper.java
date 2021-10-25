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
import com.bytedance.msdk.api.reward.RewardItem;
import com.dn.admediation.csj.bean.DnTTRewardAd;
import com.dn.admediation.csj.listener.DnTTRewardedAdListener;
import com.dn.admediation.csj.listener.DnTTRewardedAdLoadCallback;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdRewardVideoListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener;
import com.dn.sdk.sdk.tt.bean.DnTTPreloadRewardVideoView;

/**
 * 激励视频加载辅助类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 16:58
 */
public class TTAdRewardVideoLoadHelper {

	private Activity mActivity;
	private RequestInfo mRequestInfo;
	private IAdRewardVideoListener mListener;
	private IAdPreloadVideoViewListener mPreloadVideoViewListener;
	private DnTTRewardAd mDnTTRewardAd;

	private final DnTTRewardedAdListener mAdListener = new DnTTRewardedAdListener() {
		@Override
		public void onRewardedAdShow() {
			if (mListener != null) {
				mListener.onRewardAdShow();
			}
		}

//		@Override
//		public void onRewardedAdShowFail(AdError adError) {
//			if (mListener != null) {
//				mListener.onRewardVideoAdShowFail(adError.code, adError.toString());
//				mListener.onError(adError.code, adError.toString());
//			}
//			load();
//		}

		@Override
		public void onRewardClick() {
			if (mListener != null) {
				mListener.onRewardBarClick();
			}
		}

		@Override
		public void onRewardedAdClosed() {
			if (mListener != null) {
				mListener.onRewardedClosed();
			}
		}

		@Override
		public void onVideoComplete() {
			if (mListener != null) {
				mListener.onRewardVideoComplete();
			}
		}

		@Override
		public void onVideoError() {
			if (mListener != null) {
				mListener.onRewardVideoError();
				mListener.onError(AdError.ERROR_CODE_UNKNOWN_ERROR, "Video 播放错误");
			}
		}

		@Override
		public void onRewardVerify(RewardItem rewardItem) {
			if (mListener != null) {
				mListener.onRewardVerify(rewardItem.rewardVerify());
			}
		}

		@Override
		public void onSkippedVideo() {
			if (mListener != null) {
				mListener.onSkippedRewardVideo();
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

	public void loadAd(Activity activity, RequestInfo requestInfo, IAdRewardVideoListener listener) {
		this.mActivity = activity;
		this.mRequestInfo = requestInfo;
		this.mListener = listener;
		if (TTMediationAdSdk.configLoadSuccess()) {
			load();
		} else {
			TTMediationAdSdk.registerConfigCallback(mLoadCallback);
		}
	}


	public void preloadAd(Activity activity, RequestInfo requestInfo, IAdPreloadVideoViewListener viewListener, IAdRewardVideoListener listener) {
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
		mDnTTRewardAd = new DnTTRewardAd(mActivity, mRequestInfo.getAdId());

		if (mActivity instanceof AppCompatActivity) {
			((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						TTMediationAdSdk.unregisterConfigCallback(mLoadCallback);
						mDnTTRewardAd.destroy();
						((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
						mActivity = null;
					}
				}
			});
		}

		TTVideoOption videoOption = new TTVideoOption.Builder()
				.setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
				.setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
				.build();

//		Map<String, String> customData = new HashMap<>();
//		customData.put(AdSlot.CUSTOM_DATA_KEY_PANGLE, "pangle media_extra");
//		customData.put(AdSlot.CUSTOM_DATA_KEY_GDT, "gdt custom data");
//		customData.put(AdSlot.CUSTOM_DATA_KEY_KS, "ks custom data");
		// 其他需要透传给adn的数据。

		//创建广告请求参数AdSlot,具体参数含义参考文档

		int orientation = TTAdConstant.VERTICAL;
		if (mRequestInfo.getOrientation() == 2) {
			orientation = TTAdConstant.HORIZONTAL;
		}

		AdSlot.Builder adSlotBuilder = new AdSlot.Builder()
				.setTTVideoOption(videoOption)
				.setRewardName(mRequestInfo.getRewardName()) //奖励的名称
				.setRewardAmount(mRequestInfo.getRewardAmount())  //奖励的数量
				.setUserID(mRequestInfo.getUseId())//用户id,必传参数
				.setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 确保该聚合广告位下所有gdt代码位都是同一种类型（模版或非模版）。
				.setCustomData(mRequestInfo.getCustomData()) // 激励视频开启服务端验证时，透传给adn的自定义数据。
				.setOrientation(orientation);//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL

		mDnTTRewardAd.loadRewardAd(adSlotBuilder.build(), new DnTTRewardedAdLoadCallback() {
			@Override
			public void onRewardVideoLoadFail(AdError adError) {
				if (mListener != null) {
					mListener.onLoadFail(adError.code, adError.toString());
					mListener.onError(adError.code, adError.toString());
				}
			}

			@Override
			public void onRewardVideoAdLoad() {
				if (mListener != null) {
					mListener.onLoad();
				}
				mDnTTRewardAd.showRewardAd(mActivity, mAdListener);
			}

			@Override
			public void onRewardVideoCached() {
				if (mListener != null) {
					mListener.onLoadCached();
				}
			}
		});
	}

	private void preload() {
		mDnTTRewardAd = new DnTTRewardAd(mActivity, mRequestInfo.getAdId());

		DnTTPreloadRewardVideoView rewardVideoView = new DnTTPreloadRewardVideoView(mActivity, mDnTTRewardAd, mAdListener);


		if (mActivity instanceof AppCompatActivity) {
			((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						TTMediationAdSdk.unregisterConfigCallback(mPreloadCallback);
						if(mDnTTRewardAd!=null) {
							mDnTTRewardAd.destroy();
						}
						((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
						mActivity = null;
					}
				}
			});
		}

		TTVideoOption videoOption = new TTVideoOption.Builder()
				.setMuted(true)//对所有SDK的激励广告生效，除需要在平台配置的SDK，如穿山甲SDK
				.setAdmobAppVolume(0f)//配合Admob的声音大小设置[0-1]
				.build();

//		Map<String, String> customData = new HashMap<>();
//		customData.put(AdSlot.CUSTOM_DATA_KEY_PANGLE, "pangle media_extra");
//		customData.put(AdSlot.CUSTOM_DATA_KEY_GDT, "gdt custom data");
//		customData.put(AdSlot.CUSTOM_DATA_KEY_KS, "ks custom data");
		// 其他需要透传给adn的数据。


		//创建广告请求参数AdSlot,具体参数含义参考文档
		int orientation = TTAdConstant.VERTICAL;
		if (mRequestInfo.getOrientation() == 2) {
			orientation = TTAdConstant.HORIZONTAL;
		}

		AdSlot.Builder adSlotBuilder = new AdSlot.Builder()
				.setTTVideoOption(videoOption)
				.setRewardName(mRequestInfo.getRewardName()) //奖励的名称
				.setRewardAmount(mRequestInfo.getRewardAmount())  //奖励的数量
				.setUserID(mRequestInfo.getUseId())//用户id,必传参数
				.setAdStyleType(AdSlot.TYPE_EXPRESS_AD) // 确保该聚合广告位下所有gdt代码位都是同一种类型（模版或非模版）。
				.setCustomData(mRequestInfo.getCustomData()) // 激励视频开启服务端验证时，透传给adn的自定义数据。
				.setOrientation(orientation);//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL

		mDnTTRewardAd.loadRewardAd(adSlotBuilder.build(), new DnTTRewardedAdLoadCallback() {
			@Override
			public void onRewardVideoLoadFail(AdError adError) {
				if (mListener != null) {
					mListener.onLoadFail(adError.code, adError.toString());
					mListener.onError(adError.code, adError.toString());
				}
			}

			@Override
			public void onRewardVideoAdLoad() {
				if (mListener != null) {
					mListener.onLoad();
				}
				rewardVideoView.setLoadSuccess(true);
				if (rewardVideoView.isNeedShow()) {
					rewardVideoView.show();
				}
			}

			@Override
			public void onRewardVideoCached() {
				if (mListener != null) {
					mListener.onLoadCached();
				}
			}
		});
		if (mPreloadVideoViewListener != null) {
			mPreloadVideoViewListener.OnLoadVideoView(rewardVideoView);
		}
	}
}
