package com.dn.sdk.sdk.interfaces.listener.show;

/**
 * 激励视频展示监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 14:33
 */
public interface IAdRewardVideoShowListener {

	void onRewardAdShow();

	/**
	 * 广告的下载bar点击回调 admob、unity类型广告无此回调
	 */
	void onRewardBarClick();

	void onRewardedClosed();

	void onRewardVideoComplete();

	/**
	 * 1、视频播放失败的回调，admob、baidu、gdt、mintegral无此回调
	 */
	void onRewardVideoError();

	/**
	 * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
	 * 开发者应该在该回调里进行重新请求。
	 */
	void onRewardVideoAdShowFail(int code, String message);

	/**
	 * 激励视频播放完毕，验证是否有效发放奖励的回调
	 */
	void onRewardVerify(boolean result);

	/**
	 * 跳过视频播放
	 * GroMore 的admob、baidu、gdt、ks、mintegral、sigmob无此回调
	 */
	void onSkippedRewardVideo();
}
