package com.dn.sdk.sdk.interfaces.view;

/**
 * @author XuShuai
 * @version v1.0
 * @date 2021/9/26 17:31
 */
public abstract class PreloadVideoView {

	private boolean mLoadSuccess = false;
	private boolean mNeedShow = false;

	public abstract void realShow();

	public void show() {
		if (isLoadSuccess()) {
			realShow();
		} else {
			mNeedShow = true;
		}
	}

	public boolean isLoadSuccess() {
		return mLoadSuccess;
	}

	public void setLoadSuccess(boolean loadSuccess) {
		mLoadSuccess = loadSuccess;
	}

	public boolean isNeedShow() {
		return mNeedShow;
	}
}
