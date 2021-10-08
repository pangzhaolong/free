package com.donews.share;


import com.donews.base.base.ContextHolder;
import com.donews.share.wxapi.WXCustomEntryActivity;
import com.donews.utilslibrary.utils.BaseToast;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @Author: honeylife
 * @CreateDate: 2020/4/14 10:05
 * @Description: 微信分享的
 */
public class WXHolderHelp {

    private static IWXAPI api;

    //绑定微信
    public static final int STATE_BINDING = 3;
    // 微信登录
    public static final int STATE_LOGIN= 4;

    private static IWXAPI getApi() {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(ContextHolder.getInstance().getApplication(), KeyConstant.getWxApi(),
                    true);
            api.registerApp(KeyConstant.getWxApi());
        }
        return api;
    }

    public static boolean isWXAppInstalled() {
        return getApi().isWXAppInstalled();
    }

    public static void login(ISWXSuccessCallBack successCallBack) {
        if (!getApi().isWXAppInstalled()) {
            BaseToast.makeToast(ContextHolder.getInstance().getApplication()).setToastText("微信没有安装！").showToast();
            return;
        }

        BaseToast.makeToast(ContextHolder.getInstance().getApplication()).setToastText("启动微信中").setToastLongDuration().showToast();
        WXCustomEntryActivity.state = WXHolderHelp.STATE_LOGIN;
        WXCustomEntryActivity.mSuccessCallBack = successCallBack;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        getApi().sendReq(req);
    }


    public static void onBind(int type, ISWXSuccessCallBack successCallBack) {
        if (!getApi().isWXAppInstalled()) {
            BaseToast.makeToast(ContextHolder.getInstance().getApplication()).setToastText("微信没有安装！").showToast();
            return;
        }
        BaseToast.makeToast(ContextHolder.getInstance().getApplication()).setToastText("启动微信中").setToastLongDuration().showToast();
        WXCustomEntryActivity.state = type;
        WXCustomEntryActivity.mSuccessCallBack = successCallBack;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        getApi().sendReq(req);
    }

    public static void share(int cmd, ShareItem shareItem, WXMediaMessage msg) {
        WXCustomEntryActivity.cmd = cmd;
        WXCustomEntryActivity.shareItem = shareItem;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = cmd == ShareManager.SHARE_COMMAND_WX_FRIEND ? SendMessageToWX.Req
                .WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        WXHolderHelp.getApi().sendReq(req);
    }

    public static void shareText(int cmd, ShareItem shareItem, WXMediaMessage msg) {
        WXCustomEntryActivity.cmd = cmd;
        WXCustomEntryActivity.shareItem = shareItem;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = cmd == ShareManager.SHARE_COMMAND_WX_FRIEND ? SendMessageToWX.Req
                .WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        WXHolderHelp.getApi().sendReq(req);
    }
    public static void shareImage(int cmd, WXMediaMessage msg) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = cmd == ShareManager.SHARE_COMMAND_WX_FRIEND ? SendMessageToWX.Req
                .WXSceneTimeline
                : SendMessageToWX.Req.WXSceneSession;
        WXHolderHelp.getApi().sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System
                .currentTimeMillis();
    }
}

