package com.dn.sdk.sdk.interfaces.listener.preload;

import android.view.View;

import java.util.List;

/**
 * 信息流广告
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 15:45
 */
public interface IAdNativeLoadListener {

	/**
	 * 加载信息流广告成功
	 *
	 * @param views 信息流View
	 */
	void onLoad(List<View> views);


	/**
	 * 加载广告失败
	 *
	 * @param code  失败code
	 * @param error 错误信息
	 */
	void onLoadFail(int code, String error);


}
