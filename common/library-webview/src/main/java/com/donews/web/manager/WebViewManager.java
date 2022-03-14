package com.donews.web.manager;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.donews.web.widget.X5WebView;
import com.tencent.smtt.sdk.WebSettings;

/**
 * @Author: honeylife
 * @CreateDate: 2020/5/11 14:21
 * @Description: webView 的管理类
 */
public class WebViewManager implements ISWebCallBack {

    private Activity mActivity;
    private X5WebView mWebView;
    private View mErrorView;
    private boolean isOverrideUrlLoad;
    private View mLoadingView;
    private ProgressBar mProgressBar;
    private String url;

    private CustomWebViewClient webViewClient;
    private CustomWebChromeClient chromeClient;
    private ISFinishCallBack finishCallBack;

    private WebViewManager(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mWebView = builder.mWebView;
        this.mErrorView = builder.mErrorView;
        this.isOverrideUrlLoad = builder.isOverrideUrlLoad;
        this.mLoadingView = builder.mLoadingView;
        this.mProgressBar = builder.mProgressBar;
        this.url = builder.url;
        this.finishCallBack = builder.finishCallBack;
        init();
    }

    private void init() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportZoom(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSaveFormData(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setDefaultTextEncodingName("utf-8");
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(false);
        webSetting.setTextZoom(100);
        webSetting.setAppCacheEnabled(true);
        webSetting.setSavePassword(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setBlockNetworkImage(false);
        //设置 缓存模式
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        // 设置允许JS弹窗
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT <= 19) {
            mWebView.clearCache(false);
        }
        /**
         *
         * android 5.0兼容 https
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(2);
        }

        webViewClient = new CustomWebViewClient(mActivity, mWebView, mErrorView, isOverrideUrlLoad, this);
        mWebView.setWebViewClient(webViewClient);
        chromeClient = new CustomWebChromeClient(mActivity, mLoadingView, mProgressBar, this);
        mWebView.setWebChromeClient(chromeClient);

    }

    @Override
    public void onFinishUrl() {
        if (finishCallBack == null) return;
        finishCallBack.onFinishUrl();
    }

    @Override
    public void onProgress() {
        if (webViewClient == null) return;
        webViewClient.hideError();
//        mWebView.loadUrl(JavaScriptMethod.setDeviceID(mActivity));
    }

    @Override
    public void onTitleName(String titleName) {
        if (finishCallBack == null) return;
        finishCallBack.onTitleName(titleName);
    }

    @Override
    public void isFlag(boolean isFlag) {
        chromeClient.setFlag(isFlag);
    }

    public static final class Builder {
        private Activity mActivity;
        private X5WebView mWebView;
        private View mErrorView;
        private boolean isOverrideUrlLoad;
        private String url;
        private View mLoadingView;
        private ProgressBar mProgressBar;
        private ISFinishCallBack finishCallBack;

        public Builder setContext(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public Builder setView(X5WebView mWebView, View mErrorView) {
            this.mWebView = mWebView;
            this.mErrorView = mErrorView;
            return this;
        }

        public Builder setOverrideUrlLoad(boolean isOverrideUrlLoad) {
            this.isOverrideUrlLoad = isOverrideUrlLoad;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder setProgressBar(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
            return this;
        }

        public Builder setLoadingView(View mLoadingView) {
            this.mLoadingView = mLoadingView;
            return this;
        }

        public Builder setFinishCallBack(ISFinishCallBack finishCallBack) {
            this.finishCallBack = finishCallBack;
            return this;
        }

        public WebViewManager build() {
            return new WebViewManager(this);
        }
    }

    //销毁资源
    public void destroy(X5WebView webview) {
        if (webview == null) return;
        webview.stopLoading(); //停止加载
        ((ViewGroup) webview.getParent()).removeView(webview); //把webview从视图中移除
        webview.removeAllViews(); //移除webview上子view
        webview.clearCache(true); //清除缓存
        webview.clearHistory(); //清除历史
        webview.destroy(); //销毁webview自身
        //Process.killProcess(Process.myPid()); //杀死WebView所在的进程
    }
}
