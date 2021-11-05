package com.donews.web.ui;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.share.ShareWeixinApp;
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
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
public class WebViewObjActivity extends MvvmBaseLiveDataActivity<WebViewObjActivityBinding, WebViewModel>
        implements ISFinishCallBack {

    @Autowired
    String title;
    @Autowired
    String url = "";
    private WebModel mWebModel;
    @Autowired
    int mOpenType;
    @Autowired
    int mActionId;

    private WebViewManager webViewManager;
    private JavaScriptInterface javaScriptInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);
        mWebModel = new WebModel();
        mWebModel.setmOpenType(mOpenType);
        mWebModel.setmActionId(mActionId);
        webViewManager = new WebViewManager.Builder()
                .setView(mDataBinding.webView, mDataBinding.errorView).setContext(this).setOverrideUrlLoad(false).url(
                        url)
                .setProgressBar(mDataBinding.progressBar).setLoadingView(mDataBinding.loadingView)
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

        LogUtil.d("url" + url);
        url = JsonUtils.H5url(url);
        LogUtil.d("url" + url);
        mDataBinding.webView.loadUrl(url + JsonUtils.getCommonH5Json());
        mDataBinding.titleBar.setBackOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
    }
}
