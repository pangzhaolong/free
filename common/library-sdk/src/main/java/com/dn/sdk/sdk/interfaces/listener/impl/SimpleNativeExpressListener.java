package com.dn.sdk.sdk.interfaces.listener.impl;

import android.view.View;


import com.dn.sdk.sdk.interfaces.listener.IAdNativeExpressListener;

import java.util.List;

/**
 * 模本信息流广告监听
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/27 12:58
 */
public class SimpleNativeExpressListener implements IAdNativeExpressListener {
	@Override
	public void onSelected(int i, String s) {

	}

	@Override
	public void onCancel() {

	}

	@Override
	public void onRefuse() {

	}

	@Override
	public void onShow() {

	}

	@Override
	public void onRenderFail(View view, String msg, int code) {

	}

	@Override
	public void onRenderSuccess(float width, float height) {

	}

	@Override
	public void onAdClick() {

	}

	@Override
	public void onAdShow() {

	}

	@Override
	public void onAdClose() {

	}

	@Override
	public void onVideoStart() {

	}

	@Override
	public void onVideoPause() {

	}

	@Override
	public void onVideoResume() {

	}

	@Override
	public void onVideoCompleted() {

	}

	@Override
	public void onVideoError(int code, String error) {

	}

	@Override
	public void onLoad(List<View> views) {

	}

	@Override
	public void onLoadFail(int code, String error) {

	}

	@Override
	public void onError(int code, String msg) {

	}
}
