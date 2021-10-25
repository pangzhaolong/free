package com.dn.sdk.sdk.tt.bean;

import android.app.Activity;

import com.dn.admediation.csj.bean.DnTTFullVideoAd;
import com.dn.admediation.csj.listener.DnTTFullVideoAdListener;
import com.dn.sdk.sdk.interfaces.view.PreloadFullVideoView;


/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 18:09
 */
public class DnTTPreloadFullVideoView extends PreloadFullVideoView {

	private Activity mActivity;
	private DnTTFullVideoAd mDnTTRewardAd;
	private DnTTFullVideoAdListener mAdListener;

	public DnTTPreloadFullVideoView(Activity activity, DnTTFullVideoAd dnTTRewardAd, DnTTFullVideoAdListener adListener) {
		mActivity = activity;
		mDnTTRewardAd = dnTTRewardAd;
		mAdListener = adListener;
	}

	@Override
	public void realShow() {
		mDnTTRewardAd.showFullAd(mActivity, mAdListener);
	}
}
