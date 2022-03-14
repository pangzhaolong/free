package com.donews.share;

/**
 * @Author: honeylife
 * @CreateDate: 2020/4/16 14:03
 * @Description:
 */
public class ShareWeixinApp {
    private static ShareWeixinApp weixinApp;
    // 是否从微信进入APP
    private boolean isWeixin;

    private ShareWeixinApp() {
    }

    public static ShareWeixinApp getWeixinApp() {
        if (weixinApp == null) {
            weixinApp = new ShareWeixinApp();
        }
        return weixinApp;
    }

    public boolean isWeixin() {
        return isWeixin;
    }

    public void setWeixin(boolean weixin) {
        isWeixin = weixin;
    }
}
