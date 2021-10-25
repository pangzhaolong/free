package com.dn.sdk.sdk.interfaces.listener.show;

/**
 * 全屏广告展示监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 15:04
 */
public interface IAdFullVideoShowListener {

	void onFullVideoAdShow();

	/**
	 * 广告的下载bar点击回调 admob、unity类型广告无此回调
	 */
	void onFullVideoClick();

	void onFullVideoClosed();

	void onFullVideoComplete();

	/**
	 * 1、视频播放失败的回调，admob、baidu、gdt、mintegral无此回调
	 */
	void onFullVideoError();

	/**
	 * show失败回调。如果show时发现无可用广告（比如广告过期或者isReady=false），会触发该回调。
	 * 开发者应该在该回调里进行重新请求。
	 */
	void onFullVideoAdShowFail(int code, String message);

	/**
	 * 跳过视频播放
	 * GroMore 的admob、baidu、gdt、ks、mintegral、sigmob无此回调
	 */
	void onSkippedFullVideo();
}
