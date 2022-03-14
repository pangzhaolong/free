package com.donews.web.javascript;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.donews.web.widget.X5WebView;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/4 11:35<br>
 * 版本：V1.0<br>
 */
public class Js3JavaInterface extends CommonJSInterface {
    private Context context;
    private X5WebView x5WebView;

    public Js3JavaInterface(Context context, X5WebView x5WebView) {
        this.context = context;
        this.x5WebView = x5WebView;
    }

    @JavascriptInterface
    public void showInv_code(int type) {
//        try {
//            Log.d(TAG, "showInv_code: type:" + type);
//            if (getActivity() == null) return;
//            getActivity().runOnUiThread(() -> mZhuanPresenter.gotoTask(type));
//            Log.d(TAG, "showInv_code: js调用Android");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @JavascriptInterface
    public void showglod(int taskId) {
//        try {
//            Log.d(TAG, "showglod: type:" + taskId);
//            mZhuanPresenter.getDraw(String.format("%s", taskId));
//            Log.d(TAG, "showglod: js调用Android");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @JavascriptInterface
    public void qiandaodouble(int num) {
//        try {
//            Log.d(TAG, "qiandaodouble: num:" + num);
//            mZhuanPresenter.requestVideoAd(ZhuanPresenter.AD_TYPE1, AdPositionManage.videoId);
//            Log.d(TAG, "showglod: js调用Android");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @JavascriptInterface
    public void initSuccess() {

    }

    @JavascriptInterface
    public void pullDown(String eventName) {
//        Log.d(TAG, "eventReport: eventName：" + eventName);
//        if (eventName.equals("show")) {
//            callInitPage();
//
//        }
    }

    /**
     * 友盟上报事件
     *
     * @param eventName
     */
    @JavascriptInterface
    public void eventReport(String eventName) {
//        LogUtil.d(TAG, "eventReport: eventName：" + eventName);
//        MobclickAgent.onEvent(getActivity(), eventName);
    }

    /**
     * h5 上报大数据
     *
     * @param eventName
     */
    @JavascriptInterface
    public void eventReportData(String eventName) {
//        LogUtil.d(TAG, "eventReportData: eventName：" + eventName);
//        AnalysisUtils.onEvent(getActivity(), eventName);
    }
}
