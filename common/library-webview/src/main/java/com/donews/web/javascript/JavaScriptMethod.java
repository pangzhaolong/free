package com.donews.web.javascript;

import android.content.Context;

import com.donews.utilslibrary.utils.DeviceUtils;

/**
 * @Author: honeylife
 * @CreateDate: 2020/4/9 15:40
 * @Description: Android 调用h5的方法
 */
public class JavaScriptMethod {

    // 头部的返回
    public final static String BACK1 = "back1";
    // 系统的返回
    public final static String BACK2 = "back2";

    /**
     * initHomePage //刷新方法 setHomeDeviceID//传参方法 （和setDeviceID 调用实机 传参一样）
     */

    /**
     * @return 刷新界面 走路页面 首页 每日任务
     */
    public static String initHomePage() {
        return "javascript:initHomePage()";
    }

    /**
     * @param context
     * @return 走路页面 返回h5 调用参数
     */
//    public static String setHomeDeviceID(Context context) {
//        UserInfo userInfo = UserHelp.getUserHelp().getUserInfo();
//        if (userInfo == null) {
//            return "javascript:setHomeDeviceID('" + PhoneInfoUtils_a.getIMEI(context) + "','" +
//                    PhoneInfoUtils_a.getIMEI(context) + "','" +
//                    "" + "','" +
//                    "" + "','" +
//                    AppInfo.getInstance().getChannelName() + "','" +
//                    PhoneInfoUtils_a.getAppVersion(context) + "','" +
//                    DeviceUtils.getShuMeiDeviceId() + "')";
//        }
//        return "javascript:setHomeDeviceID('" + PhoneInfoUtils_a.getIMEI(context) + "','" +
//                userInfo.getUid() + "','" +
//                userInfo.getToken() + "','" +
//                userInfo.getInv_code() + "','" +
//                AppInfo.getInstance().getChannelName() + "','" +
//                PhoneInfoUtils_a.getAppVersion(context) + "','" +
//                DeviceUtils.getShuMeiDeviceId() + "')";
//
//    }

    /**
     * 赚赚上传信息页面
     *
     * @param context
     * @return
     */
//    public static String setDeviceID(Context context) {
//        UserInfo userInfo = UserHelp.getUserHelp().getUserInfo();
//        if (userInfo == null) {
//            return "javascript:setDeviceID('" + PhoneInfoUtils_a.getIMEI(context) + "','" +
//                    PhoneInfoUtils_a.getIMEI(context) + "','" +
//                    "" + "','" +
//                    "" + "','" +
//                    AppInfo.getInstance().getChannelName() + "','" +
//                    PhoneInfoUtils_a.getAppVersion(context) + "')";
//        }
//        return "javascript:setDeviceID('" + PhoneInfoUtils_a.getIMEI(context) + "','" +
//                userInfo.getUid() + "','" +
//                userInfo.getToken() + "','" +
//                userInfo.getInv_code() + "','" +
//                AppInfo.getInstance().getChannelName() + "','" +
//                PhoneInfoUtils_a.getAppVersion(context) + "')";
//
//    }

    /**
     * @param context
     * @return 走路页面 用户退出登录，调用h5的方法
     */
    public static String setHomeDeviceIDOut(Context context) {
        return "javascript:setHomeDeviceID('" + DeviceUtils.getDeviceId() + "','" +
                "0" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "','" + "" + "')";

    }

    /**
     * @param context
     * @param back    back1 title的返回，back2 手机系统的返回
     * @return 返回H5 页面的上一层，调用h5页面的方法
     */
    public static String setBackH5(Context context, String back) {
        return "javascript:returnPage('" + back + "')";

    }

    /**
     * @return 返回H5 ，调用h5页面的可见的方法
     */
    public static String getResume() {
        return "javascript:onResume()";
    }

    /**
     * @return 返回H5 ，调用h5页面的不可见的方法
     */
    public static String getPause() {
        return "javascript:onPause()";
    }

    /**
     * @return 返回H5 ，分享成功之后调用h5页面的方法
     */
    public static String getShareSuccess() {
        return "javascript:successShare()";
    }

    /**
     * @return 返回H5 ，页面要销毁调用h5页面的方法
     */
    public static String getDestoryWebview() {
        return "javascript:destoryWebview()";
    }

    /**
     * @return 赚赚刷新页面
     */
    public static String getInitPage() {
        return "javascript:initPage()";
    }

    /**
     * @return 刷新页面
     */
    public static String getInitHomePage() {
        return "javascript:initHomePage()";
    }

    /**
     * @return h5 调用的方法
     */
    public static String getMethodStr(String onMethod) {
        return String.format("javascript:%s()", onMethod);
    }

    /**
     * @return h5 分享成功之后调用的方法
     */
    public static String getInvitationSuccess() {
        return "javascript:invitesuccess()";
    }

    /**
     * @return h5 提现选中的样式
     */
    public static String getDrawMoney(int type) {
        return "javascript:onDrawMoney('" + type + "')";
    }

    /**
     * @return h5 提现选中的样式
     */
    public static String getShuMeiId() {
        return "javascript:onShuMeiId('" + DeviceUtils.getShuMeiDeviceId() + "')";
    }

    /**
     * @return 激励视频播放成功，回调给H5
     */
    public static String adSuccess() {
        return "javascript:adsuccess()";
    }

    /**
     * @return 激励视频播放出错，回调给H5
     */
    public static String adFailed() {
        return "javascript:adfailed()";
    }

    /**
     * @return 传token，回调给H5
     */
    public static String getToken(String token) {
        return "javascript:getNewToken('" + token + "')";
    }
}
