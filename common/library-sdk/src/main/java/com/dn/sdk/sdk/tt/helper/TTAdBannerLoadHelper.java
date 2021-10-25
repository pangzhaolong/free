package com.dn.sdk.sdk.tt.helper;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.TTAdSize;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTSettingConfigCallback;
import com.dn.admediation.csj.bean.DnTTBannerViewAd;
import com.dn.admediation.csj.listener.DnTTAdBannerListener;
import com.dn.admediation.csj.listener.DnTTAdBannerLoadCallBack;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdBannerListener;
import com.donews.common.utils.DensityUtils;

/**
 * banner广告加载实现类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 16:31
 */
public class TTAdBannerLoadHelper {
	private Activity mActivity;
	private RequestInfo mRequestInfo;
	private IAdBannerListener mListener;
	private DnTTBannerViewAd mDnTTBannerViewAd;

	private final TTSettingConfigCallback mSettingConfigCallback = new TTSettingConfigCallback() {
		@Override
		public void configLoad() {
			load();
		}
	};

	private final DnTTAdBannerListener mBannerListener = new DnTTAdBannerListener() {
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

		@Override
		public void onAdClosed() {
			if (mListener != null) {
				mListener.onAdClosed();
			}
		}

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
	};

	public void loadBannerAd(Activity activity, RequestInfo requestInfo, IAdBannerListener listener) {
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
		mDnTTBannerViewAd = new DnTTBannerViewAd(mActivity, mRequestInfo.getAdId());
		mDnTTBannerViewAd.setRefreshTime(30);
		mDnTTBannerViewAd.setAllowShowCloseBtn(true);
		mDnTTBannerViewAd.setTTAdBannerListener(mBannerListener);

		if (mActivity instanceof AppCompatActivity) {
			((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						TTMediationAdSdk.unregisterConfigCallback(mSettingConfigCallback);
						if (mDnTTBannerViewAd != null) {
							mDnTTBannerViewAd.destroy();
						}
						((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
						mActivity = null;
					}
				}
			});
		}


		if (mRequestInfo.getWidth() == 0) {
			DisplayMetrics outMetrics = new DisplayMetrics();
			mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
			mRequestInfo.setWidth((int) DensityUtils.px2dp((float) outMetrics.widthPixels));
		}

		//step4:创建广告请求参数AdSlot,具体参数含义参考文档
		AdSlot adSlot = new AdSlot.Builder()
				// banner暂时只支持模版类型，必须手动设置为AdSlot.TYPE_EXPRESS_AD
				.setAdStyleType(AdSlot.TYPE_EXPRESS_AD)
				// 使用TTAdSize.BANNER_CUSTOME可以调用setImageAdSize设置大小
				.setBannerSize(TTAdSize.BANNER_CUSTOME)
				.setImageAdSize(mRequestInfo.getWidth(), mRequestInfo.getHeight())
				.build();
		//step5:请求广告，对请求回调的广告作渲染处理
		mDnTTBannerViewAd.loadAd(adSlot, new DnTTAdBannerLoadCallBack() {
			@Override
			public void onAdFailedToLoad(AdError adError) {
				// 获取本次waterfall加载中，加载失败的adn错误信息。
				mRequestInfo.getContainer().removeAllViews();
				if (mListener != null) {
					mListener.onLoadFail(adError.code, adError.toString());
					mListener.onError(adError.code, adError.toString());
				}
			}

			@Override
			public void onAdLoaded() {
				mRequestInfo.getContainer().removeAllViews();
				if (mDnTTBannerViewAd != null) {
					//横幅广告容器的尺寸必须至少与横幅广告一样大。如果您的容器留有内边距，实际上将会减小容器大小。如果容器无法容纳横幅广告，则横幅广告不会展示
					View view = mDnTTBannerViewAd.getBannerView();
					if (view != null) {
						mRequestInfo.getContainer().addView(view);
					}
				}
				if (mListener != null) {
					mListener.onLoad();
				}
			}
		});
	}
}
