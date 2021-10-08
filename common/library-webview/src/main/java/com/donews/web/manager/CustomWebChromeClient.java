package com.donews.web.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * @Author: honeylife
 * @CreateDate: 2020/5/11 15:52
 * @Description:
 */
public class CustomWebChromeClient extends WebChromeClient {

    private final static String TAG = "CustomWebChromeClient";
    private Activity mActivity;

    private boolean isFlag;

    private ISWebCallBack isWebCallBack;

    private View mLoadingView;
    private ProgressBar mProgressBar;

    public CustomWebChromeClient(Activity mActivity, View mLoadingView, ProgressBar mProgressBar, ISWebCallBack isWebCallBack) {
        this.mActivity = mActivity;
        this.isWebCallBack = isWebCallBack;
        this.mLoadingView = mLoadingView;
        this.mProgressBar = mProgressBar;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.d(TAG, "onProgressChanged: newProgress:" + newProgress);
        if (mProgressBar != null) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE)
                    mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }
        if (newProgress == 100) {
            mLoadingView.setVisibility(View.GONE);
        }

        if (newProgress == 100 && !isFlag) {
            if (isWebCallBack == null) return;
            isWebCallBack.onProgress();

        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (isWebCallBack == null) return;
        isWebCallBack.onTitleName(title);
    }
}
