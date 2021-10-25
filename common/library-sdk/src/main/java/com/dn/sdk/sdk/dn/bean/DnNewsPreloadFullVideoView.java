package com.dn.sdk.sdk.dn.bean;

import com.dn.sdk.sdk.interfaces.view.PreloadFullVideoView;
import com.donews.b.main.DoNewsAdNative;

/**
 * 多牛聚合全屏视频封装
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 17:06
 */
public class DnNewsPreloadFullVideoView extends PreloadFullVideoView {

	private DoNewsAdNative mDoNewsAdNative;

	public DnNewsPreloadFullVideoView(DoNewsAdNative doNewsAdNative) {
		mDoNewsAdNative = doNewsAdNative;
	}

	@Override
	public void realShow() {
		if (mDoNewsAdNative != null) {
			mDoNewsAdNative.showFullScreenVideo();
		}
	}
}
