package com.dn.sdk.sdk.interfaces.listener;


import com.dn.sdk.sdk.interfaces.listener.preload.IAdErrorListener;
import com.dn.sdk.sdk.interfaces.listener.preload.IAdLoadListener;

/**
 * 开屏广告监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:50
 */
public interface IAdSplashListener extends IAdLoadListener, IAdErrorListener {
	void onAdShow();

	void onAdClicked();

	void onAdShowFail(int code, String error);

	void onAdSkip();

	void onAdDismiss();

	void onPresent();

	void extendExtra(String var1);
}
