package com.dn.sdk.sdk.interfaces.listener.preload;


import com.dn.sdk.sdk.interfaces.view.PreloadVideoView;

/**
 * 预加载View
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 17:49
 */
public interface IAdPreloadVideoViewListener {

	/**
	 * 回调返回预加载View
	 *
	 * @param videoView 预加载View
	 */
	void OnLoadVideoView(PreloadVideoView videoView);
}
