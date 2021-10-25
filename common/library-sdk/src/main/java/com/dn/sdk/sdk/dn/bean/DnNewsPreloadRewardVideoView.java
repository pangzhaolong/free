package com.dn.sdk.sdk.dn.bean;

import com.dn.sdk.sdk.interfaces.view.PreloadRewardVideoView;
import com.donews.b.main.DoNewsAdNative;

/**
 * 多牛聚合sdk 预加载返回激励视频对象封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 16:59
 */
public class DnNewsPreloadRewardVideoView extends PreloadRewardVideoView {

	private DoNewsAdNative mDoNewsAdNative;

	public DnNewsPreloadRewardVideoView(DoNewsAdNative doNewsAdNative) {
		mDoNewsAdNative = doNewsAdNative;
	}

	@Override
	public void realShow() {
		mDoNewsAdNative.showRewardAd();
	}
}
