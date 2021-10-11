package com.donews.common.router;

/**
 * 应用模块: 组件化路由
 * <p>
 * 类描述: 用于组件化开发中,ARouter Fragment路径统一注册, 注册的路径请写好注释、标注业务功能
 * <p>
 *
 * 作者： created by honeylife<br>
 * 日期： 2020-02-25
 */
public class RouterFragmentPath
{
    
    /** 首页组件 */
    public static class Home
    {
        private static final String HOME = "/home";
        
        /** 首页 */
        public static final String PAGER_HOME = HOME + "/Home";
        
    }

    /** sdkTest */
    public static class TestSdk {
        private static final String TEST = "/test";

        /** 首页 */
        public static final String PAGER_TEST_SDK = TEST + "/testSdk";

        public static final String PAGER_TEST_AD_SDK = TEST + "/adSdk";


    }

    public static class User
    {
        private static final String USER = "/user";
        
        /** 个人中心 */
        public static final String PAGER_USER = USER + "/User";

        /** 用户设置 */
        public static final String PAGER_USER_SETTING = USER + "/Setting";
    }


    public static class Web {
        private static final String WEB = "/web";

        /**
         * 个人中心
         */
        public static final String PAGER_FRAGMENT = WEB + "/webFragment";
    }

    public static class ClassPath {
        public static final String ACTION_VIEW_MODEL = "com.donews.action.viewmodel.ActionViewModel";
        public static final String HOME_VIEW_MODEL = "com.donews.home.viewModel.HomeViewModel";
        public static final String WEB_VIEW_MODEL = "com.donews.web.viewmodel.WebViewModel";

    }
    public static class MethodPath {
        public static final String AD_LOAD_MANAGER_REFRESH_AD_CONFIG = "com.dn.sdk.AdLoadManager.refreshAdConfig";
    }
}
