package com.dn.sdk.sdk.interfaces.listener.preload;

/**
 * 广告错误监听器,汇总所有错误信息,包括加载错误和显示错误
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 16:19
 */
public interface IAdErrorListener {

	void onError(int code, String msg);
}
