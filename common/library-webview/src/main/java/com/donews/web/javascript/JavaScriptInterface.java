package com.donews.web.javascript;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.PermissionUtils;
import com.dn.drouter.ARouteHelper;
import com.donews.common.contract.AdType;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.services.config.ServicesConfig;
import com.donews.share.ShareBasePopupWindow;
import com.donews.share.ShareConstant;
import com.donews.share.ShareItem;
import com.donews.share.ShareManager;
import com.donews.share.ShareType;
import com.donews.share.ShareWeixinApp;
import com.donews.share.WXShareExecutor;
import com.donews.utilslibrary.analysis.AnalysisHelp;
import com.donews.utilslibrary.analysis.AnalysisParam;
import com.donews.utilslibrary.utils.LogUtil;
import com.donews.web.BuildConfig;
import com.donews.web.manager.WebModel;
import com.donews.web.viewmodel.WebViewModel;
import com.donews.web.widget.X5WebView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: honeylife
 * @CreateDate: 2020/6/1 10:12
 * @Description:
 */
public class JavaScriptInterface extends CommonInterface {

    private final static String TAG = "CommonJSInterface";

    private FragmentActivity mContext;
    private X5WebView mWebView;

    private WebModel mWebModel;
    private WebViewModel webViewModel;

    public JavaScriptInterface(FragmentActivity context, X5WebView webView) {
        super(context);
        this.mContext = context;
        this.mWebView = webView;
    }

    public void setWebModel(WebModel webModel) {
        this.mWebModel = webModel;
    }

    public void setWebViewModel(WebViewModel webViewModel) {
        this.webViewModel = webViewModel;
    }


    @JavascriptInterface
    public boolean CheckInstall(String packagename) {
//            boolean installed = false;
        //检查该包名是否已经安装
        boolean installed = isAvilible(mContext, packagename);
        return installed;
    }

    @JavascriptInterface
    public void AwallOpen(String pkgName) {
        //启动该应用
        try {
            Log.d(TAG, "AwallOpen: 打开app:" + pkgName);
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(pkgName);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 激励视频
     */
    @JavascriptInterface
    public void onShowVideoAd() {
        if (mContext == null) {
            return;
        }
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ARouteHelper.routeAccessServiceForResult(ServicesConfig.Dialog.DIALOG_SERVICE,
                        "onRequestAdVideo", new Object[]{mContext, AdType.WEB_VIDEO, 0, 0, ""});
            }
        });
    }


    /**
     * 关闭当前的页面
     */
    @JavascriptInterface
    public void onClose() {
        mContext.finish();
    }

    private boolean isAvilible(Activity context, String packageName) {
        if (!PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE)) {
            return false;
        }
        final PackageManager packageManager;//获取packagemanager
        packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * @param base64Data base64的数据
     * @param page       页面
     * @param type       分享类型 1 微信好友，2微信朋友圈
     */
    @JavascriptInterface
    public void invitationFriends(String base64Data, String page, int type) {
        LogUtil.e("==A==" + base64Data);
        Log.e("TAG", "===========A========" + base64Data.length());
        try {
            if (TextUtils.isEmpty(base64Data)) return;
            WXShareExecutor wxShareExecutor = new WXShareExecutor(mContext);
            int cmd = 0;
            ShareItem shareItem = new ShareItem();
            cmd = type == ShareConstant.WX ?
                    ShareManager.SHARE_COMMAND_WX :
                    ShareManager.SHARE_COMMAND_WX_FRIEND;
            shareItem.setImageUrl(base64Data);
            Log.e("TAG", "==B===" + shareItem.getImageUrl());
            Log.e("TAG", "===========B========" + shareItem.getImageUrl().length());
            wxShareExecutor.onShareImageBase64(cmd, shareItem);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 分享web链接方法
     */
    @JavascriptInterface
    public void onShare(String json) {
        LogUtil.i("=====" + json);
        if (json == null) return;
        Gson gson = new Gson();
        final ShareItem shareItem = gson.fromJson(json, ShareItem.class);
        shareItem.setActionId(mWebModel.getmActionId());
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ShareBasePopupWindow shareBasePopupWindow = new ShareBasePopupWindow(mContext);
                shareBasePopupWindow.setShareItem(shareItem);
//                shareBasePopupWindow.setX5WebView(mWebView);
                shareBasePopupWindow.show();
            }
        });

    }

    /**
     * 分享朋友圈，好友，等饭Web
     *
     * @param json
     */
    @JavascriptInterface
    public void onShareJson(String json) {
        LogUtil.i("=====" + json);
        if (json == null) return;
        Gson gson = new Gson();
        final ShareItem shareItem = gson.fromJson(json, ShareItem.class);
        shareItem.setActionId(mWebModel.getmActionId());
        int cmd = shareItem.getCmd();
        ShareManager shareManager = new ShareManager();
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cmd >= 0) {
                    switch (shareItem.getShareFromType()) {
                        case ShareType.INVITATION_FRIEND:
//                            if (!TextUtils.isEmpty(shareItem.getExtraKey()) && !TextUtils.isEmpty(shareItem.getExtraValue()))
//                                shareItem.setInvitationUrl(shareItem.getWebUrl(), shareItem.getActionId(), shareItem.getExtraKey(), shareItem.getExtraValue());
//                            else
//                                shareItem.setInvitationUrl(shareItem.getWebUrl(), shareItem.getActionId());
//                            break;
                        default:
                            break;
                    }
                    shareManager.share(cmd, shareItem, mContext);
                    ShareWeixinApp.getWeixinApp().setWeixin(true);
                }
            }
        });
    }

    /**
     * @param color 背景色 例如 #ffffff
     * @param title title名字
     */
    @JavascriptInterface
    public void onSetTitleBg(final String color, final String title) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webViewModel.onSetTitleBg(color, title);

            }
        });
    }

    // 分享成功是否需要回调
    @JavascriptInterface
    public void onShareSuccess(final int shareSuccess) {
        if (mWebModel == null) return;
        mWebModel.setmOpenType(shareSuccess);
    }

    // 需要返回h5的上一层页面
    @JavascriptInterface
    public void onBackH5() {
        if (mWebModel == null) return;
        Log.e("TAG", "===B===" + mWebModel.isBackH5());
        mWebModel.setBackH5(true);
        Log.e("TAG", "===C===" + mWebModel.isBackH5());
    }

    @JavascriptInterface
    public void closeTitle() {
        Log.d(TAG, "closeTitle: ");
//        mContext.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mContext.closeTitle();
//            }
//        });

//            closeTitle();
//            wxShare(fileUrl);
    }

    @JavascriptInterface
    public void openTitle() {
        Log.d(TAG, "openTitle: ");
//        mContext.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mContext.openTitle();
//            }
//        });


    }

    // 隐藏返回图标
    @JavascriptInterface
    public void onHideBackView() {
        Log.d(TAG, "openTitle: ");
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webViewModel.hideLeftBackImage();
            }
        });


    }

    // 显示返回图标
    @JavascriptInterface
    public void onShowBackView() {
        Log.d(TAG, "openTitle: ");
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webViewModel.showLeftBackImage();
            }
        });


    }

    /**
     * @param text        头部右侧名称
     * @param onMethodStr 头部右侧 h5处理方法名
     * @param color       头部右侧 字体颜色
     */
    @JavascriptInterface
    public void onRightTextView(String text, String color, String onMethodStr) {
        if (mContext == null || TextUtils.isEmpty(text)) return;
//        mContext.runOnUiThread(() -> {
//            mContext.setTitleRightText(text, TextUtils.isEmpty(color) ?
//                    Color.parseColor("#393939") :
//                    Color.parseColor(color));
//            mContext.setRightTextClick(v -> mWebView.loadUrl(JavaScriptMethod.getMethodStr(onMethodStr)));
//        });
    }

    /**
     * 活动中需要摇晃
     *
     * @param isShake true 表示可摇晃，false 表示不可摇晃
     */
    @JavascriptInterface
    public void onSharkItOff(boolean isShake) {
        if (mWebModel == null) return;
        Log.e("TAG", "==isShake=====" + isShake);
        mWebModel.setIsmShake(isShake);
        onShake();
    }

    /**
     * 测试环境的时候，摇晃手机，替换域名
     */
    public void onShake() {
        if (mWebModel == null) return;
        if (mWebModel.isIsmShake()) {
//            SensorManagerHelper sensorHelper = new SensorManagerHelper(mContext);
//            sensorHelper.setOnShakeListener(new SensorManagerHelper.OnShakeListener() {
//                @Override
//                public void onShake() {
//                    Log.i("TAG", "========onShake==手机摇晃了===onShakeMobile()===");
//                    sensorHelper.stop();
//                    mWebView.loadUrl("javascript:onShakeMobile()");
//                }
//            });
        }
    }


    public void requestVideoAd(final String positionId) {
        requestVideoAd(positionId, 0);
    }

    public void requestVideoAd(final String positionId, final int type) {
        if (mContext.isDestroyed() || !mWebModel.isResume()) return;
        if (!mWebModel.isVideo()) return;
        mWebModel.setVideo(false);
    }


    /**
     * 视频播放成功的回调
     */
    public void adsuccess() {
        Log.e("TAG", "========adsuccess===========");
        try {
            if (mWebView == null) {
                mContext.finish();
                return;
            }
            mWebView.loadUrl("javascript:adsuccess()");

        } catch (Throwable e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 视频播放失败的回调
     */
    public void adfailed() {
        try {
            if (mWebView == null) {
                mContext.finish();
                return;
            }
            mWebView.loadUrl("javascript:adfailed()");

        } catch (Throwable e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 领取奖励的方法
     *
     * @param json
     */
    @JavascriptInterface
    public void onReceiveReward(String json) {
        LogUtil.d(json);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("json" + json);
                ARouteHelper.routeAccessServiceForResult(ServicesConfig.Dialog.DIALOG_SERVICE,
                        "getPage", new Object[]{AdType.GAME_GOLD, json, mContext, AnalysisParam.LOOK_BATTERYGAMEGOLD});
            }
        });
    }

    /**
     * H5 做任务的方法
     *
     * @param id     任务的id
     * @param action 活动处理的事件
     */
    @JavascriptInterface
    public void showInvCode(int id, int action, String location) {
        try {
            LogUtil.d("showInv_code: type:" + id + "action" + action + "location" + location);
            if (mContext == null) return;
            mContext.runOnUiThread(() -> webViewModel.gotoTask(id, action, location));
            LogUtil.d("showInv_code: js调用Android");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * H5 活动领取金币的接口
     *
     * @param json
     */
    @JavascriptInterface
    public void showGold(String json) {
        try {
            LogUtil.d("showglod: type:" + json);
            if (mContext == null) return;
            mContext.runOnUiThread(() -> ARouteHelper.routeAccessServiceForResult(ServicesConfig.Dialog.DIALOG_SERVICE,
                    "getPage", new Object[]{AdType.TASK_DRAW, json, mContext, ""}));
            Log.d(TAG, "showglod: js调用Android");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //提现跳转页面
    @JavascriptInterface
    public void onStartPageActivity(int type) {
        Log.e("TAG", "===type=======" + type);
        mContext.runOnUiThread(() -> {
            switch (type) {
                case 1: // 跳转首页
                    ARouteHelper.invoke(RouterActivityPath.ClassPath.MINE_ACTIVITY_JAVASCRIPT
                            , "onHomeItemView");
                    break;
                case 2: // 跳转福利
                    ARouteHelper.invoke(RouterActivityPath.ClassPath.MINE_ACTIVITY_JAVASCRIPT
                            , "onWelfareItemView");
                    break;

            }
            mContext.finish();
        });
    }


    // 绑定微信
    @JavascriptInterface
    public void onBindWeiXin() {
        LogUtil.d("==onBindWeiXin=");
        mContext.runOnUiThread(() -> ARouteHelper.routeAccessServiceForResult(ServicesConfig.User.LONGING_SERVICE,
                "weChatBind", null));

    }

    /**
     * 上报事件
     *
     * @param eventName
     */
    @JavascriptInterface
    public void eventReport(String eventName) {
        LogUtil.d("eventReport: eventName：" + eventName);
        AnalysisHelp.onEvent(mContext, eventName);
    }

    public void onReloadUrl() {
        if (webViewModel != null) {
            webViewModel.onReloadUrl();
        }
    }
}
