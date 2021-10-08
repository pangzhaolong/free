package com.donews.web.ui;

import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.utilslibrary.utils.JsonUtils;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.web.R;
import com.donews.web.databinding.WebViewFragmentBinding;
import com.donews.web.javascript.JavaScriptInterface;
import com.donews.web.javascript.JavaScriptMethod;
import com.donews.web.manager.ISFinishCallBack;
import com.donews.web.manager.WebModel;
import com.donews.web.manager.WebViewManager;
import com.donews.web.viewmodel.WebViewModel;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/4 10:38<br>
 * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.Web.PAGER_FRAGMENT)
public class WebViewFragment extends MvvmLazyLiveDataFragment<WebViewFragmentBinding, WebViewModel> implements ISFinishCallBack {

    private WebViewManager webViewManager;

    @Autowired
    String url = "";
    @Autowired
    int mOpenType;
    @Autowired
    int mActionId;
    private WebModel mWebModel;

    @Override
    public int getLayoutId() {
        return R.layout.web_view_fragment;
    }



    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        if (mDataBinding == null) {
            return;
        }
        ARouter.getInstance().inject(this);
        webViewManager = new WebViewManager.Builder()
                .setView(mDataBinding.webViewFrag, mDataBinding.errorView).setContext(getActivity()).setOverrideUrlLoad(false).url(url)
                .setLoadingView(mDataBinding.loadingLayoutView)
                .setFinishCallBack(this)
                .build();
        mWebModel = new WebModel();
        mWebModel.setmOpenType(mOpenType);
        mWebModel.setmActionId(mActionId);
        mViewModel.setModel(mWebModel, mDataBinding.webViewFrag);
        mViewModel.setBaseActivity(getBaseActivity());
        JavaScriptInterface javaScriptInterface = new JavaScriptInterface(getBaseActivity(), mDataBinding.webViewFrag);
        javaScriptInterface.setWebModel(mWebModel);
        javaScriptInterface.setWebViewModel(mViewModel);
        mDataBinding.webViewFrag.addJavascriptInterface(javaScriptInterface, "android");
        LogUtil.d("url" + url);
        url = JsonUtils.H5url(url);
        LogUtil.d("url" + url);
        mDataBinding.webViewFrag.loadUrl(url + JsonUtils.getCommonH5Json());
        ARouteHelper.bind(RouterFragmentPath.ClassPath.WEB_VIEW_MODEL, mViewModel);

    }

    @Override
    public void onFinishUrl() {

    }

    @Override
    public void onTitleName(String titleName) {

    }

    @Override
    public void onDestroy() {
        Log.e("TAG", "=====WebViewFrag=====onDestroy===");
        if (mDataBinding != null && mDataBinding.webViewFrag != null) {
            mDataBinding.webViewFrag.loadUrl(JavaScriptMethod.getDestoryWebview());
        }
        if (webViewManager != null) {
            webViewManager.destroy(mDataBinding.webViewFrag);
        }
        ARouteHelper.unBind(RouterFragmentPath.ClassPath.WEB_VIEW_MODEL);
        super.onDestroy();
    }
}
