package com.donews.web.manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.donews.web.ui.WebViewObjActivity;
import com.donews.web.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @CreateDate: 2020/4/18
 * @Author: honeylife
 * @Description:
 * @Version:
 */
public class CustomWebViewClient extends WebViewClient {

    private final static String TAG = "CustomWebViewClient";

    // 断网的情况
    private static final String INTERNET_DISCONNECTED = "net::ERR_INTERNET_DISCONNECTED";
    private static final String INTERNET_PROXY = "net::ERR_PROXY_CONNECTION_FAILED";
    private Context mActivity;
    private X5WebView mWebView;
    private View mErrorView;
    // true 表示不重定向
    private boolean isOverrideUrlLoad = true;
    private ISWebCallBack isWebCallBack;

    public CustomWebViewClient(Context context, X5WebView webView, View mErrorView, boolean isOverrideUrlLoad, ISWebCallBack isWebCallBack) {
        this.mActivity = context;
        this.mWebView = webView;
        this.mErrorView = mErrorView;
        this.isOverrideUrlLoad = isOverrideUrlLoad;
        this.isWebCallBack = isWebCallBack;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (isOverrideUrlLoad) {
            Log.e("TAG", "==url====" + url);
            Intent intent = new Intent(mActivity, WebViewObjActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("titleName", "");
            mActivity.startActivity(intent);
            return true;
        } else
            return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e("TAG", "==B==" + description);
        if (INTERNET_DISCONNECTED.equals(description) || INTERNET_PROXY.equals(description)) {
            showError();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.e("TAG", "==A==" + error.getDescription());
        if (INTERNET_DISCONNECTED.contentEquals(error.getDescription()) || INTERNET_PROXY.contentEquals(error.getDescription())) {
            Log.e("TAG", "==AAAAA==" + error.getDescription());
            showError();
        }

    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//        Uri uri = request.getUrl();
//        WebResourceResponse webResourceResponse = WebResourceResponseUtils.interceptRequest(uri);
//        if (webResourceResponse != null) {
//            return webResourceResponse;
//        }
//        Log.e("TAG", "=111=uri=" + uri);
//        return super.shouldInterceptRequest(view, request);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        Uri uri = Uri.parse(url);
//        WebResourceResponse webResourceResponse = WebResourceResponseUtils.interceptRequest(uri);
//        if (webResourceResponse != null) {
//            return webResourceResponse;
//        }
//        Log.e("TAG", "==uri=" + uri + "=url=" + url);
//        return super.shouldInterceptRequest(view, url);
//    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        isWebCallBack.onFinishUrl();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        isWebCallBack.isFlag(false);
    }


    /**
     * 错误页面展示
     */
    private void showError() {
        isWebCallBack.isFlag(true);
        mWebView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    /**
     * 错误页面展示
     */
    public void hideError() {
        if (mWebView == null || mErrorView == null) return;
        mWebView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);

    }
}
