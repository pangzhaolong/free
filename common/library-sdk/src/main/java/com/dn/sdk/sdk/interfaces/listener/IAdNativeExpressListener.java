package com.dn.sdk.sdk.interfaces.listener;

import android.view.View;


/**
 * 模板信息流渲染广告监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 15:56
 */
public interface IAdNativeExpressListener extends IAdNativeListener {

	/**
	 * 模板渲染失败
	 *
	 * @param view
	 */
	void onRenderFail(View view, String msg, int code);

	/**
	 * 模板渲染成功
	 *
	 * @param width  返回view的宽 ，开发者可以根据返回的宽高进行比例适配
	 * @param height 返回view的高 ，开发者可以根据返回的宽高进行比例适配
	 */
	void onRenderSuccess(float width, float height);
}
