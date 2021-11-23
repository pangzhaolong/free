package com.dn.sdk.sdk.dn.helper;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.dn.sdk.sdk.bean.RequestInfo;
import com.dn.sdk.sdk.interfaces.listener.IAdFullVideoListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdPreloadVideoViewListener;
import com.dn.sdk.sdk.dn.bean.DnNewsPreloadFullVideoView;
import com.donews.b.main.DoNewsAdNative;
import com.donews.b.main.info.DoNewsAD;
import com.donews.b.start.DoNewsAdManagerHolder;

/**
 * 多牛聚合 全屏视频加载辅助类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 17:03
 */
public class DnAdFullVideoLoadHelper {

	private Activity mActivity;
	private RequestInfo mRequestInfo;
	private IAdFullVideoListener mListener;
	private IAdPreloadVideoViewListener mPreloadVideoViewListener;
	private DoNewsAdNative mDoNewsAdNative;


	public void loadAd(Activity activity, RequestInfo requestInfo, IAdFullVideoListener listener) {
		this.mActivity = activity;
		this.mRequestInfo = requestInfo;
		this.mListener = listener;
		load();
	}


	public void preloadAd(Activity activity, RequestInfo requestInfo, IAdPreloadVideoViewListener viewListener, IAdFullVideoListener listener) {
		this.mActivity = activity;
		this.mRequestInfo = requestInfo;
		this.mPreloadVideoViewListener = viewListener;
		this.mListener = listener;
		preload();
	}

	private void load() {
		if (mActivity instanceof AppCompatActivity) {
			((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						mDoNewsAdNative.destroy();
						((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
						mActivity = null;
					}
				}
			});
		}

		if (mRequestInfo.getOrientation() == 0) {
			mRequestInfo.setOrientation(1);
		}
		DoNewsAD doNewsAD = new DoNewsAD.Builder()
				.setPositionid(mRequestInfo.getAdId())
//				.setExpressViewWidth(mRequestInfo.getWidth())
//				.setExpressViewHeight(mRequestInfo.getHeight())
				.setOrientation(mRequestInfo.getOrientation())
				.build();
		mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();
		mDoNewsAdNative.preLoadFullScreenVideoAd(mActivity, doNewsAD, new DoNewsAdNative.FullSreenVideoListener() {
			@Override
			public void onAdStatus(int i, Object o) {

			}

			@Override
			public void onAdShow() {
				if (mListener != null) {
					mListener.onFullVideoAdShow();
				}
			}

			@Override
			public void onAdClick() {
				if (mListener != null) {
					mListener.onFullVideoClick();
				}
			}

			@Override
			public void onAdClose() {
				if (mListener != null) {
					mListener.onFullVideoClosed();
				}
			}

			@Override
			public void onVideoLoad() {
				if (mListener != null) {
					mListener.onLoad();
				}
			}

			@Override
			public void onVideoCached() {
				if (mListener != null) {
					mListener.onLoadCached();
				}
				mDoNewsAdNative.showFullScreenVideo();
			}

			@Override
			public void onSkippedVideo() {
				if (mListener != null) {
					mListener.onSkippedFullVideo();
				}
			}

			@Override
			public void onVideoComplete() {
				if (mListener != null) {
					mListener.onFullVideoComplete();
				}
			}

			@Override
			public void onError(int i, String s) {
				if (mListener != null) {
					mListener.onError(i, s);
				}
			}
		});
	}

	private void preload() {
		if (mActivity instanceof AppCompatActivity) {
			((AppCompatActivity) mActivity).getLifecycle().addObserver(new LifecycleEventObserver() {
				@Override
				public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
					if (event == Lifecycle.Event.ON_DESTROY) {
						mDoNewsAdNative.destroy();
						((AppCompatActivity) mActivity).getLifecycle().removeObserver(this);
						mActivity = null;
					}
				}
			});
		}

		DoNewsAD doNewsAD = new DoNewsAD.Builder()
				.setPositionid(mRequestInfo.getAdId())
				.setOrientation(mRequestInfo.getOrientation())
				.build();
		mDoNewsAdNative = DoNewsAdManagerHolder.get().createDoNewsAdNative();

		DnNewsPreloadFullVideoView preloadFullVideoView = new DnNewsPreloadFullVideoView(mDoNewsAdNative);

		mDoNewsAdNative.preLoadFullScreenVideoAd(mActivity, doNewsAD, new DoNewsAdNative.FullSreenVideoListener() {
			@Override
			public void onAdStatus(int i, Object o) {

			}

			@Override
			public void onAdShow() {
				if (mListener != null) {
					mListener.onFullVideoAdShow();
				}
			}

			@Override
			public void onAdClick() {
				if (mListener != null) {
					mListener.onFullVideoClick();
				}
			}

			@Override
			public void onAdClose() {
				if (mListener != null) {
					mListener.onFullVideoClosed();
				}
			}

			@Override
			public void onVideoLoad() {
				if (mListener != null) {
					mListener.onLoad();
				}
			}

			@Override
			public void onVideoCached() {
				if (mListener != null) {
					mListener.onLoadCached();
				}
				preloadFullVideoView.setLoadSuccess(true);
				if (preloadFullVideoView.isNeedShow()) {
					preloadFullVideoView.show();
				}
			}

			@Override
			public void onSkippedVideo() {
				if (mListener != null) {
					mListener.onSkippedFullVideo();
				}
			}

			@Override
			public void onVideoComplete() {
				if (mListener != null) {
					mListener.onFullVideoComplete();
				}
			}

			@Override
			public void onError(int i, String s) {
				if (mListener != null) {
					mListener.onError(i, s);
				}
			}
		});

		if (mPreloadVideoViewListener != null) {
			mPreloadVideoViewListener.OnLoadVideoView(preloadFullVideoView);
		}
	}

}
