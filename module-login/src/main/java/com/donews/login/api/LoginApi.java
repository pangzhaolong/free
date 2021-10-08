package com.donews.login.api;

import com.donews.login.BuildConfig;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/13 17:28<br>
 * 版本：V1.0<br>
 */
public class LoginApi {
    private static final String BASE_URL = BuildConfig.LOGIN_URL + "app/v2/";
    // code 获取验证码
    public static final String CODE = BASE_URL + "code";
    // 手机号登录与微信登录
    public static final String LOGIN = BASE_URL + "login";
    //获取用户TAG值,新老用户
    public static final String USER_TAG = BuildConfig.TASK_URL + "user/more";
    // 手机号与微信 绑定
    public static final String BIND = BASE_URL + "bind";

    //JWT Token续期，需要在每次升级系统或每次冷启动前调用， 避免jwt中的信息与实际信息不一致。
    // 本接口会返回新的JWT， 后续请求务必请将老JWT替换为新JWT。
    public static final String REFRESH = BASE_URL + "refresh";

    // info 更新个人信息 本接口会返回新的token，后续请求务必请将老JWT替换为新token。
    // post 请求
    public static final String INFO = BASE_URL + "info";
    // 针对已登录用户，取消绑定手机或微信登录方式 本接口会返回新的token，
    // 后续请求务必请将老JWT替换为新token。
    public static final String UNBIND = BASE_URL + "unbind";
    // 获取任意用户信息，填0获取本人信息
    public static final String INFO_USER_ID = BASE_URL + "info/%s";

}
