package com.dn.sdk.sdk.tt.bean;

import android.app.Activity;

import com.dn.admediation.csj.bean.DnTTRewardAd;
import com.dn.admediation.csj.listener.DnTTRewardedAdListener;
import com.dn.sdk.sdk.interfaces.view.PreloadRewardVideoView;

/**
 * GroMore 预加载类
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 17:12
 */
public class DnTTPreloadRewardVideoView extends PreloadRewardVideoView {

	private Activity mActivity;
	private DnTTRewardAd mDnTTRewardAd;
	private DnTTRewardedAdListener mAdListener;

	public DnTTPreloadRewardVideoView(Activity activity, DnTTRewardAd dnTTRewardAd, DnTTRewardedAdListener listener) {
		mActivity = activity;
		mDnTTRewardAd = dnTTRewardAd;
		mAdListener = listener;
	}


	@Override
	public void realShow() {
		if (mDnTTRewardAd.isReady()) {
			mDnTTRewardAd.showRewardAd(mActivity, mAdListener);
		}
	}
}
