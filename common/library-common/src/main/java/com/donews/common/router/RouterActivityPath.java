package com.donews.common.router;

/**
 * 应用模块: 组件化路由
 * <p>
 * 类描述: 用于在组件化开发中,利用ARouter 进行Activity跳转的统一路径注册, 注册时请写好注释、标注界面功能业务
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期： 2020-02-25
 */
public class RouterActivityPath {
    /**
     * main组件
     */
    public static class Main {
        private static final String MAIN = "/main";

        /**
         * 主页面
         */
        public static final String PAGER_MAIN = MAIN + "/Main";
    }

    public static class User {
        private static final String USER = "/login";

        /**
         * 登录界面
         */
        public static final String PAGER_LOGIN = USER + "/Login";

        /**
         * 关注页面
         */
        public static final String PAGER_ATTENTION = USER + "/attention";
        /**
         * 手机号登录
         */
        public static final String PAGER_MOBILE = USER + "/mobile";

        /**
         * 绑定手机
         */
        public static final String PAGER_BIND_PHONE = USER + "/bindphone";
    }


    public static class Web {
        private static final String USER = "/web";

        /**
         * webview页面
         */
        public static final String PAGER_WEB_ACTIVITY = USER + "/webActivity";
    }

    public static class ClassPath {
        public static final String WEB_VIEW_OBJ_ACTIVITY_JAVASCRIPT = "com.donews.web.javascript.JavaScriptInterface";
        public static final String MINE_ACTIVITY_JAVASCRIPT = "com.donews.main.ui.MainActivity";
        public static final String METHOD_FRAGMENT_SET_POSITION = "com.donews.main.ui.MainActivity.setCurrentItemPosition";

    }
}
