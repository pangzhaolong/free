package com.donews.web.ui;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.dn.sdk.listener.interstitial.IAdInterstitialFullScreenListener;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.share.ShareWeixinApp;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.web.R;
import com.donews.web.databinding.WebViewObjActivityBinding;
import com.donews.web.javascript.JavaScriptInterface;
import com.donews.web.javascript.JavaScriptMethod;
import com.donews.web.javascript.OpenWebViewType;
import com.donews.web.manager.ISFinishCallBack;
import com.donews.web.manager.WebModel;
import com.donews.web.manager.WebViewManager;
import com.donews.web.viewmodel.WebViewModel;
import com.donews.yfsdk.loader.AdManager;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
public class WebViewObjActivity extends MvvmBaseLiveDataActivity<WebViewObjActivityBinding, WebViewModel>
        implements ISFinishCallBack {

    @Autowired
    String title;
    @Autowired
    String url = "";
    @Autowired
    Boolean showAd = false;
    private WebModel mWebModel;
    @Autowired
    int mOpenType;
    @Autowired
    int mActionId;

    private WebViewManager webViewManager;
    private JavaScriptInterface javaScriptInterface;

    private boolean mIsPaused = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);
        mWebModel = new WebModel();
        mWebModel.setmOpenType(mOpenType);
        mWebModel.setmActionId(mActionId);
        webViewManager = new WebViewManager.Builder()
                .setView(mDataBinding.webView, mDataBinding.errorView).setContext(this).setOverrideUrlLoad(true).url(
                        url)
                .setProgressBar(mDataBinding.progressBar).setLoadingView(mDataBinding.loadingView)
                .setOverrideUrlLoad(false) //是否新开启一个页面打开链接
                .setFinishCallBack(this)
                .build();

        initView();
        mViewModel.setBaseActivity(this);
        mViewModel.setDataBinding(mDataBinding);
    }

    @Override
    public void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();

        mDataBinding.webView.getSettings().setSupportMultipleWindows(false);
        mDataBinding.progressBar.setIndeterminate(false);
        mDataBinding.progressBar.setIndeterminateDrawable(
                getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
        // progressBar
        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.BLUE), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        mDataBinding.progressBar.setProgressDrawable(d);
        mDataBinding.progressBar.setMinimumHeight(20);

        javaScriptInterface = new JavaScriptInterface(this, mDataBinding.webView);
        ARouteHelper.bind(RouterActivityPath.ClassPath.WEB_VIEW_OBJ_ACTIVITY_JAVASCRIPT, javaScriptInterface);
        javaScriptInterface.setWebModel(mWebModel);
        javaScriptInterface.setWebViewModel(mViewModel);
        mDataBinding.webView.addJavascriptInterface(javaScriptInterface, "android");
        mDataBinding.titleBar.setTitle(title);
//        LogUtil.d("url" + url);
        url = JsonUtils.H5url(url);
//        LogUtil.d("url" + url);
        if (url.contains("report.amap.com")) {
            mDataBinding.webView.loadUrl(url);
//            LogUtil.e("url:" + url);
        } else {
            mDataBinding.webView.loadUrl(url + JsonUtils.getCommonH5Json(url.contains("?") && url.contains("authorization")));
//            LogUtil.e("url:" + url + JsonUtils.getCommonH5Json(url.contains("?") && url.contains("authorization")));
        }
        mDataBinding.titleBar.setBackOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (showAd) {
            new Handler().postDelayed(() -> {
                if (!mIsPaused) {
                    AdManager.INSTANCE.loadAndShowInterstitialFullAd(WebViewObjActivity.this, new IAdInterstitialFullScreenListener() {
                        @Override
                        public void onAdStatus(int code, @Nullable Object any) {

                        }

                        @Override
                        public void onAdLoad() {
                            AnalysisUtils.onEventEx(WebViewObjActivity.this, Dot.YYW_INTERSTITIAL_FULL_LOAD);
                        }

                        @Override
                        public void onAdCached() {

                        }

                        @Override
                        public void onAdError(int errorCode, @NonNull String errprMsg) {
                            AnalysisUtils.onEventEx(WebViewObjActivity.this, Dot.YYW_INTERSTITIAL_FULL_ERR, String.valueOf(errorCode));
                        }

                        @Override
                        public void onAdShow() {
                            AnalysisUtils.onEventEx(WebViewObjActivity.this, Dot.YYW_INTERSTITIAL_FULL_SHOW);
                        }

                        @Override
                        public void onAdClicked() {

                        }

                        @Override
                        public void onAdComplete() {

                        }

                        @Override
                        public void onAdClose() {

                        }

                        @Override
                        public void onSkippedVideo() {

                        }

                        @Override
                        public void onRewardVerify(boolean reward) {

                        }

                        @Override
                        public void onAdShowFail(int errCode, @NonNull String errMsg) {
                            AnalysisUtils.onEventEx(WebViewObjActivity.this, Dot.YYW_INTERSTITIAL_FULL_SHOW_FAIL, String.valueOf(errCode));
                        }

                        @Override
                        public void onAdVideoError(int errCode, @NonNull String errMsg) {

                        }

                        @Override
                        public void onAdStartLoad() {
                            AnalysisUtils.onEventEx(WebViewObjActivity.this, Dot.YYW_INTERSTITIAL_FULL_LOAD);
                        }
                    });
                    AnalysisUtils.onEventEx(WebViewObjActivity.this, Dot.YYW_INTERSTITIAL_FULL);
//                    AdManager.INSTANCE.loadInterstitialAd(WebViewObjActivity.this, null);
                }
            }, 500);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_view_obj_activity;
    }


    @Override
    public void onFinishUrl() {

    }

    @Override
    public void onTitleName(String titleName) {
        if (mDataBinding == null) {
            return;
        }
        if (title == null || TextUtils.isEmpty(title)) {
            mDataBinding.titleBar.setTitle(titleName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("=onResume=");
        mWebModel.setResume(true);
        mDataBinding.webView.loadUrl(JavaScriptMethod.getResume());
        mIsPaused = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("=onRestart=");
        if (mWebModel != null)
            mOpenType = mWebModel.getmOpenType();
        if (ShareWeixinApp.getWeixinApp().isWeixin() &&
                mDataBinding.webView != null && mOpenType == OpenWebViewType.SHARE_SUCCESS) {
            mDataBinding.webView.loadUrl(JavaScriptMethod.getShareSuccess());
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.e("WebObjectActivity=onDestroy=");
        mDataBinding.webView.loadUrl(JavaScriptMethod.getDestoryWebview());
        if (webViewManager != null) webViewManager.destroy(mDataBinding.webView);
        ARouteHelper.unBind(RouterActivityPath.ClassPath.WEB_VIEW_OBJ_ACTIVITY_JAVASCRIPT);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebModel != null && mWebModel.isBackH5()) {
            mDataBinding.webView.post(
                    () -> mDataBinding.webView.loadUrl(JavaScriptMethod.setBackH5(WebViewObjActivity.this,
                            JavaScriptMethod.BACK2)));
            mWebModel.setBackH5(false);
        } else {
            if (mDataBinding.webView.canGoBack()) {
                mDataBinding.webView.goBack();
            } else {
                finish();
            }

        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataBinding.webView.loadUrl(JavaScriptMethod.getPause());
        mWebModel.setResume(false);
        mIsPaused = true;
    }
}
