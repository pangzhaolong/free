package com.dn.sdk.sdk.interfaces.listener;

/**
 * 原生信息流不喜欢事件监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 10:45
 */
public interface IAdNativeDislikeListener {

	public void onSelected(int i, String s);

	public void onCancel();

	public void onRefuse();

	public void onShow();
}
