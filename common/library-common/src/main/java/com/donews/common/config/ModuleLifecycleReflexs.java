package com.donews.common.config;

/**
 * 应用模块: common
 * <p>
 * 类描述: 组件生命周期初始化类的类目管理者,在这里注册需要初始化的组件,通过反射动态调用各个组件的初始化方法
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期： 2020-02-25
 */
public class ModuleLifecycleReflexs {
    /**
     * 基础库
     */
    private static final String BaseInit = "com.donews.common.CommonModuleInit";

    /**
     * main组件库
     */
    private static final String MainInit =
            "com.donews.main.application.MainModuleInit";

    /**
     * 用户组件初始化
     */
    private static final String UserInit = "com.donews.user.UserModuleInit";

    /**
     * home 缓存初始化
     */

    private static final String HomeInit = "com.donews.home.application.HomeModuleInit";

    /**
     * 首页 缓存初始化
     */

    private static final String FrontInit = "com.donews.home.application.FrontModuleInit";

    /**
     * 广告组件库
     */
    private static final String SDKInit = "com.donews.common.ad.business.application.AdSdkModuleInit";

    public static String[] initModuleNames = {MainInit, BaseInit, HomeInit, SDKInit, FrontInit};
}
