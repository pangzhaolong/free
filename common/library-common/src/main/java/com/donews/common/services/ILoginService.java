package com.donews.common.services;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 应用模块:
 * <p>
 * 类描述: app登陆相关信息
 * <p>
 *
 * 作者： created by honeylife<br>
 * 日期： 2020-01-27
 */
public interface ILoginService extends IProvider {

    String LOGIN_SERVICE_NAME = "login_service";

    /**
     * 保存登录状态
     *
     * @param status 登录状态
     * @return 返回当前登录状态
     */
    boolean saveStatus(boolean status);

    /**
     * 是否登录
     * @return 是否登录
     * */
    boolean isLogin();

    /**
     * 获取Token
     * @return token
     * */
    String getToken();

    /**
     * 获取uuid
     * @return uuid
     * */
    String getUUID();

    /**
     * 刷新用户信息
     * */
    void refreshUserDetailInfo();

    /**
     * 登录
     * */
    void login();

    /**
     * 是否以管理员身份登录
     * */
    void login(boolean isMainAccountLogin);

    /**
     * 退出登录
     * */
    void logout();



    /**
     * 设置token
     * @param token
     * @return
     */
    String setToken(String token);

    /**
     * 取消登录
     * */
    void onLoginCancel();

    /**
     * token过期处理
     * */
    void onTokenExpire();

}
