package com.donews.web.manager;

import android.view.ViewGroup;

import com.donews.web.widget.X5WebView;


/**
 * @Author: honeylife
 * @CreateDate: 2020/6/17 19:20
 * @Description:
 */
public class WebViewClear {
    //销毁资源
    public static void destroy(X5WebView webview) {
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
