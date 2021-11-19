package com.donews.common.router;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 应用模块: 组件化路由
 * <p>
 * 类描述: 用于组件化开发中,ARouter Fragment路径统一注册, 注册的路径请写好注释、标注业务功能
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期： 2020-02-25
 */
public class RouterFragmentPath {

    /**
     * 9.9包邮的组件
     */
    public static class Mail {
        private static final String Mail = "/mail";

        /**
         * 包邮的页面组件Fragment
         */
        public static final String PAGE_MAIL_PACKAGE = Mail + "/mailPackageFragment";

    }

    /**
     * 首页组件
     */
    public static class Home {
        private static final String HOME = "/home";

        /**
         * 首页
         */
        public static final String PAGER_HOME = HOME + "/Home";

    }

    /**
     * 首页组件
     **/
    public static class Front {
        private static final String FRONT = "/front";

        /**
         * 首页
         */
        public static final String PAGER_FRONT = FRONT + "/Front";

    }

    public static class Blank {
        private static final String BLANK = "/blank";

        /**
         * 首页
         */
        public static final String PAGER_BLANK = BLANK + "/Blank";

    }

    public static class Face {
        private static final String FACE = "/face";

        /**
         * 首页
         */
        public static final String PAGER_FACE = FACE + "/Face";

    }

    /**
     * 秒杀组件
     */
    public static class Spike {
        private static final String SPIKE = "/spike";

        /**
         * 秒杀
         */
        public static final String PAGER_SPIKE = SPIKE + "/Spike";

    }

    /**
     * 晒单页组件
     */
    public static class Unboxing {
        private static final String UNBOXING = "/unboxing";

        public static final String PAGER_UNBOXING_FRAGMENT = UNBOXING + "/unboxing";

        public static Fragment getUnboxingFragment() {
            return (Fragment) ARouter.getInstance().build(PAGER_UNBOXING_FRAGMENT).navigation();
        }
    }


    /** 抽奖组件 */
    public static class Lottery
    {
        private static final String LOTTERY = "/lottery";

        /** 抽奖 */
        public static final String PAGER_LOTTERY = LOTTERY + "/lottery";

    }


    /**
     * sdkTest
     */
    public static class TestSdk {
        private static final String TEST = "/test";

        /**
         * 首页
         */
        public static final String PAGER_TEST_SDK = TEST + "/testSdk";

        public static final String PAGER_TEST_AD_SDK = TEST + "/adSdk";


    }

    public static class User {
        private static final String USER = "/user";

        /**
         * 个人中心
         */
        public static final String PAGER_USER = USER + "/UserInfo";

        /**
         * 用户设置
         */
        public static final String PAGER_USER_SETTING = USER + "/MineSetting";

        /**
         * 开奖详情、开奖页 的Fragment
         */
        public static final String PAGER_USER_OPEN_WINNING = USER + "/MineOpenWinningFragment";

        /**
         * 获取开奖的Fragment
         *
         * @param period     期数。如果为 0 :表示自动计算期数
         * @param isMainLoad 是否为首页加载，T:是，F:否
         * @param isShowBack 是否显示返回按钮，T:显示按钮，F:不显示
         * @param isShowMore 是否显示往期的按钮，T:显示按钮，F:不显示
         * @return 开奖的Fragment
         */
        public static Fragment getMineOpenWinFragment(
                int period, boolean isMainLoad,boolean isShowBack, boolean isShowMore) {
            return (Fragment) ARouter.getInstance()
                    .build(RouterFragmentPath.User.PAGER_USER_OPEN_WINNING)
                    .withInt("period", period)
                    .withBoolean("isMainLoad", isMainLoad)
                    .withBoolean("isShowBack", isShowBack)
                    .withBoolean("isShowMore", isShowMore)
                    .navigation();
        }
    }

    public static class Login {
        private static final String JDD_LOGIN = "/jdd_login";
        /**
         * 绑定手机弹窗
         */
        public static final String PAGER_BIND_PHONE_DIALOG_FRAGMENT = JDD_LOGIN + "/BindPhoneDialogFragment";
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
