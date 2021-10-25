package com.dn.sdk.sdk.interfaces.listener;

/**
 * 视频信息流监听器
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 10:51
 */
public interface IAdNativeVideoListener {
	public void onVideoStart();

	public void onVideoPause();

	public void onVideoResume();

	public void onVideoCompleted();

	public void onVideoError(int code, String error);
}
