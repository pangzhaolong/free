package com.donews.web.viewmodel;

import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.drouter.ARouteHelper;
import com.donews.base.model.BaseModel;
import com.donews.base.model.IModelListener;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.contract.AdType;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.config.ServicesConfig;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.utilslibrary.utils.Validator;
import com.donews.web.R;
import com.donews.web.databinding.WebViewObjActivityBinding;
import com.donews.web.javascript.JavaScriptMethod;
import com.donews.web.manager.WebModel;
import com.donews.web.model.WebModels;
import com.donews.web.widget.X5WebView;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/11 16:20<br>
 * 版本：V1.0<br>
 */
public class WebViewModel extends BaseLiveDataViewModel< WebModels> implements IModelListener {
    private WebModel webModel;
    private X5WebView webView;
    private FragmentActivity baseActivity;

    private WebViewObjActivityBinding viewDataBinding;

    public void setDataBinding(WebViewObjActivityBinding viewDataBinding) {
        this.viewDataBinding = viewDataBinding;
    }


    public void setModel(WebModel webModel, X5WebView webView) {
        this.webModel = webModel;
        this.webView = webView;

    }

    public void setBaseActivity(FragmentActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public void gotoTask(int id, int action, String location) {
        switch (action) {
            case WebActionType.no:
                break;
            case WebActionType.video:// 1;
                ARouteHelper.routeAccessServiceForResult(ServicesConfig.Dialog.DIALOG_SERVICE,
                        "onRequestAdVideo", new Object[]{baseActivity, AdType.TASK_DRAW_GOLD, 0, id, AnalysisParam.LOOK_JOB_LOOK});
                break;
            case WebActionType.html: // 2;
                ARouter.getInstance().build(RouterActivityPath.Web.PAGER_WEB_ACTIVITY)
                        .withString("title", "")
                        .withString("url", location)
                        .navigation();
                break;
            case WebActionType.share: // 3;
                break;
            case WebActionType.sdkVideo: // 4;
                break;
            case WebActionType.sdkList: // 5;
                break;
            case WebActionType.sdkInfo: // 6;
                break;
            case WebActionType.bindWx: // 7;
                break;
            case WebActionType.money: // 8;
                break;
            case WebActionType.dou: // 9;
                break;
            case WebActionType.bindMobile: // 10;
                break;
            case WebActionType.taoBao: // 11;
                break;
            case WebActionType.goShop: // 12;
                break;
            case WebActionType.selectShop: // 13;
                break;
            case WebActionType.sign: // 14;
                break;
            case WebActionType.shareShop: // 15;
                break;
            case WebActionType.red: // 16;
                break;
            case WebActionType.welfare: //17
                ARouteHelper.invoke(RouterActivityPath.ClassPath.MINE_ACTIVITY_JAVASCRIPT
                        , "onWelfareItemView");
                break;
            case WebActionType.guess:
                ARouteHelper.invoke(RouterActivityPath.ClassPath.MINE_ACTIVITY_JAVASCRIPT
                        , "onGuessItemView");
                break;
            case WebActionType.nativeStart: //跳转原生页面
                ARouter.getInstance().build(location)
                        .navigation();
                break;

            case WebActionType.nativeAction: //跳转 业务逻辑处理
                ARouteHelper.build(location).invoke();
                ;
                break;
        }


    }

    /**
     * @param color 颜色 例如 #333333
     * @param title title 名称
     */
    public void onSetTitleBg(final String color, final String title) {

        if (viewDataBinding == null) {
            return;
        }

        if (!TextUtils.isEmpty(title)) {
            viewDataBinding.titleBar.setTitle(title);
        }
        if (!Validator.isColor(color)) {
            return;
        }
        LogUtil.d("color" + color + "title" + title);
        viewDataBinding.titleBar.setTitleBarBackgroundColor(color);
        viewDataBinding.titleBar.setTitleTextColor("#FFFFFF");
        viewDataBinding.titleBar.setBackImageView(R.drawable.left_back_logo_write);
    }

    /**“
     * 隐藏返回按钮
     */

    public void hideLeftBackImage(){
        if (viewDataBinding == null) {
            return;
        }
        viewDataBinding.titleBar.hideBackButton();
    }
    /**“
     * 展示返回按钮
     */

    public void showLeftBackImage(){
        if (viewDataBinding == null) {
            return;
        }
        viewDataBinding.titleBar.showBackButton();
    }


    // 更新任务状态  反射，勿删
    public void onUpdateTask(int id) {
        mModel.onUpdateTask(id);
    }

    // 领取奖励  反射，勿删
    public void onScoreAdd(int id) {
        mModel.onScoreAdd(id);
    }

    public void onRefreshPageView() {
        if (webView != null) {
            webView.loadUrl(JavaScriptMethod.getInitHomePage());
        }
    }

    @Override
    public void onLoadFinish(BaseModel model, Object data) {

    }

    @Override
    public void onLoadFail(BaseModel model, String prompt) {

    }

    @Override
    public void onComplete() {
        onRefreshPageView();
    }

    public void onReloadUrl() {
        if (webView != null) {
            LogUtil.d(webView.getUrl());
            webView.loadUrl(JavaScriptMethod.getToken(AppInfo.getToken()));
        }
    }

    @Override
    public WebModels createModel() {
        return new WebModels();
    }
}
