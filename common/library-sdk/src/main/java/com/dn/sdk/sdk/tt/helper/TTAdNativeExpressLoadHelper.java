package com.dn.sdk.sdk.tt.helper;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.AdSlot;
import com.bytedance.msdk.api.AdmobNativeAdOptions;
import com.bytedance.msdk.api.TTAdSize;
import com.bytedance.msdk.api.TTDislikeCallback;
import com.bytedance.msdk.api.TTMediationAdSdk;
import com.bytedance.msdk.api.TTSettingConfigCallback;
import com.bytedance.msdk.api.TTVideoOption;
import com.dn.admediation.csj.bean.DnTTUnifiedNativeAd;
import com.dn.admediation.csj.listener.DnTTNativeAd;
import com.dn.admediation.csj.listener.DnTTNativeAdLoadCallback;
import com.dn.admediation.csj.listener.DnTTNativeExpressAdListener;
import com.dn.admediation.csj.listener.DnTTVideoListener;
import com.dn.sdk.R;
import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener;
import com.dn.sdk.sdk.tt.utils.UIUtils;
import com.dn.sdk.sdk.tt.utils.VideoOptionUtil;
import com.donews.common.utils.DensityUtils;
import com.donews.utilslibrary.base.UtilsConfig;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息流模板广告加载
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 10:09
 */
public class TTAdNativeExpressLoadHelper {

	private Context mContext;
	private RequestInfo mRequestInfo;
	private IAdNativeExpressListener mListener;

	private DnTTUnifiedNativeAd mDnTTUnifiedNativeAd;

	private final TTSettingConfigCallback mConfigCallback = new TTSettingConfigCallback() {
		@Override
		public void configLoad() {
			load();
		}
	};

	public void loadAd(Context context, RequestInfo requestInfo, IAdNativeExpressListener listener) {
		this.mContext = context;
		this.mRequestInfo = requestInfo;
		this.mListener = listener;
		if (TTMediationAdSdk.configLoadSuccess()) {
			load();
		} else {
			TTMediationAdSdk.registerConfigCallback(mConfigCallback);
		}
	}

	private void load() {
		mDnTTUnifiedNativeAd = new DnTTUnifiedNativeAd(mContext, mRequestInfo.getAdId());

		if (mContext instanceof AppCompatActivity) {
			((AppCompatActivity) mContext).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						TTMediationAdSdk.unregisterConfigCallback(mConfigCallback);
						if (mDnTTUnifiedNativeAd != null) {
							mDnTTUnifiedNativeAd.destroy();
						}
						((AppCompatActivity) mContext).getLifecycle().removeObserver(this);
						mContext = null;
					}
				}
			});
		}

		//视频声音控制设置
		TTVideoOption videoOption = VideoOptionUtil.getTTVideoOption();

		AdmobNativeAdOptions admobNativeAdOptions = new AdmobNativeAdOptions();
		admobNativeAdOptions.setAdChoicesPlacement(AdmobNativeAdOptions.ADCHOICES_TOP_RIGHT)//设置广告小标默认情况下，广告选择叠加层会显示在右上角。
				.setRequestMultipleImages(true)//素材中可能包含多张图片，如果此值设置为true， 则表示需要展示多张图片，此值设置为 false（默认），仅提供第一张图片。
				.setReturnUrlsForImageAssets(true);//设置为true，SDK会仅提供Uri字段的值，允许自行决定是否下载实际图片，同时不会提供Drawable字段的值

		// 针对Gdt Native自渲染广告，可以自定义gdt logo的布局参数。该参数可选,非必须。
		FrameLayout.LayoutParams gdtNativeAdLogoParams =
				new FrameLayout.LayoutParams(
						UIUtils.dip2px(UtilsConfig.getApplication(), 40),
						UIUtils.dip2px(UtilsConfig.getApplication(), 13),
						Gravity.RIGHT | Gravity.TOP); // 例如，放在右上角

		if (mRequestInfo.getWidth() == 0) {
			int widthDp = (int) DensityUtils.px2dp(DeviceUtils.getWidthPixels((Activity) mContext));
			mRequestInfo.setWidth(widthDp);
		}
		if (mRequestInfo.getNativeAdCount() == 0) {
			mRequestInfo.setNativeAdCount(1);
		}

		/**
		 * 创建feed广告请求类型参数AdSlot,具体参数含义参考文档
		 * 备注
		 * 1: 如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
		 * 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
		 */
		AdSlot adSlot = new AdSlot.Builder()
				.setTTVideoOption(videoOption)//视频声音相关的配置
				.setAdmobNativeAdOptions(admobNativeAdOptions)
				//必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot.TYPE_NATIVE_AD：原生广告
				.setAdStyleType(AdSlot.TYPE_EXPRESS_AD)
				// 备注
				// 1:如果是信息流自渲染广告，设置广告图片期望的图片宽高 ，不能为0
				// 2:如果是信息流模板广告，宽度设置为希望的宽度，高度设置为0(0为高度选择自适应参数)
				// 必选参数 单位dp ，详情见上面备注解释
				.setImageAdSize(mRequestInfo.getWidth(), mRequestInfo.getHeight())
				.setAdCount(mRequestInfo.getNativeAdCount()) //请求广告数量为1到3条
				.setGdtNativeAdLogoParams(gdtNativeAdLogoParams) // 设置gdt logo布局参数。
				.build();

		//请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
		/**
		 * 注：每次加载信息流广告的时候需要新建一个TTUnifiedNativeAd，否则可能会出现广告填充问题
		 * (例如：mTTAdNative = new TTUnifiedNativeAd(this, mAdUnitId);）
		 */
		mDnTTUnifiedNativeAd.loadAd(adSlot, new DnTTNativeAdLoadCallback() {
			@Override
			public void onAdLoaded(List<DnTTNativeAd> ads) {
				List<View> views = new ArrayList<>();
				for (DnTTNativeAd ad : ads) {
					if (ad != null && ad.isExpressAd()) {
						views.add(getExpressAdView(null, null, ad));
					}
				}
				if (mListener != null) {
					mListener.onLoad(views);
				}
			}

			@Override
			public void onAdLoadedFial(AdError adError) {
				if (mListener != null) {
					mListener.onLoadFail(adError.code, adError.toString());
					mListener.onError(adError.code, adError.toString());
				}
			}
		});


	}


	private View getExpressAdView(View convertView, ViewGroup parent, @NonNull final DnTTNativeAd ad) {
		final ExpressAdViewHolder adViewHolder;
		try {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.sdk_listitem_ad_native_express, parent, false);
				adViewHolder = new ExpressAdViewHolder();
				adViewHolder.mAdContainerView = convertView.findViewById(R.id.iv_listitem_express);
				convertView.setTag(adViewHolder);
			} else {
				adViewHolder = (ExpressAdViewHolder) convertView.getTag();
			}

			if (ad.hasDislike()) {
				Activity activity = null;
				if (mContext instanceof Activity) {
					activity = (Activity) mContext;
				}
				if (activity != null) {
					ad.setDislikeCallback(activity, new TTDislikeCallback() {
						@Override
						public void onSelected(int i, String s) {
							if (mListener != null) {
								mListener.onSelected(i, s);
							}
						}

						@Override
						public void onCancel() {
							if (mListener != null) {
								mListener.onCancel();
							}
						}

						@Override
						public void onRefuse() {
							if (mListener != null) {
								mListener.onRefuse();
							}
						}

						@Override
						public void onShow() {
							if (mListener != null) {
								mListener.onShow();
							}
						}
					});
				}
			}

			ad.setTTVideoListener(new DnTTVideoListener() {
				@Override
				public void onVideoStart() {
					if (mListener != null) {
						mListener.onVideoStart();
					}
				}

				@Override
				public void onVideoPause() {
					if (mListener != null) {
						mListener.onVideoPause();
					}
				}

				@Override
				public void onVideoResume() {
					if (mListener != null) {
						mListener.onVideoResume();
					}
				}

				@Override
				public void onVideoCompleted() {
					if (mListener != null) {
						mListener.onVideoCompleted();
					}
				}

				@Override
				public void onVideoError(AdError adError) {
					if (mListener != null) {
						mListener.onVideoError(adError.code, adError.toString());
						mListener.onError(adError.code, adError.toString());
					}
				}
			});

			ad.setTTNativeExpressAdListener(new DnTTNativeExpressAdListener() {
				@Override
				public void onRenderFail(View view, String s, int i) {
					if (mListener != null) {
						mListener.onRenderFail(view, s, i);
						mListener.onError(AdError.RENDER_FAIL_UNKNOWN, s);
					}
				}

				@Override
				public void onRenderSuccess(float width, float height) {
					if (adViewHolder.mAdContainerView != null) {
						//获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
						int sWidth;
						int sHeight;
						final View video = ad.getExpressView(); // 获取广告view
						//如果返回的size是TTAdSize.FULL_WIDTH和 height == TTAdSize.AUTO_HEIGHT
						//广告view的布局直接设置为如下值
						if (width == TTAdSize.FULL_WIDTH && height == TTAdSize.AUTO_HEIGHT) {
							sWidth = FrameLayout.LayoutParams.MATCH_PARENT;
							sHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
						} else {
							//传回来的值，开发者可自行调整View大小
							sWidth = UIUtils.getScreenWidth(mContext);
							sHeight = (int) ((sWidth * height) / width);
						}
						if (video != null) {
							//如果存在父布局，需要先从父布局中移除
							UIUtils.removeFromParent(video);
							FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(sWidth, sHeight);
							adViewHolder.mAdContainerView.removeAllViews();
							adViewHolder.mAdContainerView.addView(video, layoutParams);
						}
					}
					if (mListener != null) {
						mListener.onRenderSuccess(width, height);
					}
				}

				@Override
				public void onAdClick() {
					if (mListener != null) {
						mListener.onAdClick();
					}
				}

				@Override
				public void onAdShow() {
					if (mListener != null) {
						mListener.onAdShow();
					}
				}
			});

			ad.render();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	private static class ExpressAdViewHolder {
		FrameLayout mAdContainerView;
	}
}
