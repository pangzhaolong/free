package com.dn.sdk.sdk.interfaces.listener;


import com.dn.sdk.sdk.interfaces.listener.preload.IAdErrorListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdLoadListener;

/**
 * banner广告监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:55
 */
public interface IAdBannerListener extends IAdLoadListener, IAdErrorListener {

	void onAdShow();

	void onAdClosed();

	void onAdClicked();

	void onAdShowFail(int code, String msg);

	/** 广告被曝光 */
	void onAdExposure();

	/**
	 * 当广告打开浮层时调用，如打开内置浏览器、内容展示浮层，一般发生在点击之后
	 * 常常在onAdLeftApplication之前调用，不能保证每次都回调 admob sdk和优量汇sdk支持，穿山甲SDK、unity SDK,baidu SDK,mintegral SDK暂时不支持
	 */
	void onAdOpened();

	/**
	 * 此方法会在用户点击打开其他应用（例如 Google Play）时
	 * 于 onAdOpened() 之后调用，从而在后台运行当前应用，不能保证每次都回调  admob sdk、优量汇sdk、mintegral SDK支持，穿山甲SDK、unity SDK,baidu SDK,暂时不支持
	 */
	void onAdLeftApplication();
}
