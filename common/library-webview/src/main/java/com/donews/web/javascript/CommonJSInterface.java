package com.donews.web.javascript;


/**
 * @Author: honeylife
 * @CreateDate: 2020/4/14 10:12
 * @Description:
 */
public class CommonJSInterface {
//
//    private BaseActivity mContext;
//    protected CommonJSInterface(BaseActivity context){
//        this.mContext=context;
//    }
//    /**
//     * @param base64Data base64的数据
//     * @param page       页面
//     * @param type       分享类型 1 微信好友，2微信朋友圈
//     */
//    @JavascriptInterface
//    public void invitationFriends(String base64Data, String page, int type) {
//        LogUtil.e("TAG", "==A==" + base64Data);
//        Log.e("TAG","===========A========"+base64Data.length());
//        try {
//            if (TextUtils.isEmpty(base64Data)) return;
//            WXShareExecutor wxShareExecutor = new WXShareExecutor(mContext);
//            int cmd = 0;
//            ShareItem shareItem = new ShareItem();
//            cmd = type == ShareConstant.WX ?
//                    ShareManager.SHARE_COMMAND_WX :
//                    ShareManager.SHARE_COMMAND_WX_FRIEND;
//            shareItem.setImageUrl(base64Data);
//            Log.e("TAG","==B==="+shareItem.getImageUrl());
//            Log.e("TAG","===========B========"+shareItem.getImageUrl().length());
//            wxShareExecutor.onShareImageBase64(cmd, shareItem);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//
//    }
}
