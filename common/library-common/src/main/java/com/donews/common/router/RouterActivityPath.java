package com.donews.common.router;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.launcher.ARouter;

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

    public static class Mine {

        /**
         * 分享H5连接的分享弹窗(所有都是默认的落地页)
         *
         * @param activity 依赖的上下文
         */
        public static void goShareWxChatDialogDefaultH5(FragmentActivity activity) {
            DialogFragment df = (DialogFragment) ARouter.getInstance()
                    .build(RouterActivityPath.Mine.DIALOG_SHARE_TO_DIALOG_FRAGMENT)
                    .navigation();
            df.show(activity.getSupportFragmentManager(), "share");
        }

        private static final String MINE = "/mine";

        /**
         * 提现记录
         */
        public static final String PAGER_ACTIVITY_WITHDRAWAL_RECORD = MINE + "/WithdrawalRecordActivity";

        /**
         * 提现中心
         */
        public static final String PAGER_ACTIVITY_WITHDRAWAL = MINE + "/WithdrawalCenterActivity";
        /**
         * 设置页面
         */
        public static final String PAGER_ACTIVITY_SETTING = MINE + "/SettingActivity";

        /**
         * 分享到哪里的选择弹窗
         */
        public static final String DIALOG_SHARE_TO_DIALOG_FRAGMENT = MINE + "/ShareToDialogFragment";

        /**
         * 注销账户原因弹窗
         */
        public static final String DIALOG_USER_CANCELLATION_WHY_DIALOG_FRAGMENT = MINE +
                "/UserCancellationWhyDialogFragment";

        /**
         * 账户注销页面
         */
        public static final String PAGER_MINEUSER_CANCELLATION_ACTIVITY = MINE + "/MineUserCancellationActivity";

        /**
         * 参与记录
         */
        public static final String PAGER_PARTICIPATE_RECORD = MINE + "/participateRecord";

        /**
         * 中奖记录
         */
        public static final String PAGER_WINNING_RECORD = MINE + "/mineWinningRecord";

        /**
         * 个人中心的开奖码页面
         */
        public static final String PAGER_MINE_WINNING_CODE_ACTIVITY = MINE + "/MineWinningCodeActivity";

        /**
         * 关于我们
         */
        public static final String PAGER_MINE_ABOUT_ACTIVITY = MINE + "/AboutActivity";


        /**
         * 往期开奖
         */
        public static final String PAGE_MINE_REWARD_HISTORY = MINE + "/RewardHistoryActivity";
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
        public static final String METHOD_FRAGMENT_SET_POSITION = "com.donews.main.ui.MainActivity" +
                ".setCurrentItemPosition";

    }


    public static class GoodsDetail {
        public static final String DETAIL = "/detail";
        //商品详情页
        public static final String GOODS_DETAIL = DETAIL + "/goodsDetail";

        //商品详情相关的Provider
        public static final String GOODS_DETAIL_PROVIDER = DETAIL + "/goodsDetailProvider";
    }

    public static class RealTime {
        public static final String REALTIME = "/reltime";
        public static final String REALTIME_DETAIL = REALTIME + "/realtimeDetail";
    }

    public static class CrazyList {
        public static final String CRAZY_LIST = "/crazylist";
        public static final String CRAZY_LIST_DETAIL = CRAZY_LIST + "/CrazyList";
    }

    public static class Rp {
        public static final String RP = "/rp";
        public static final String PAGE_RP = RP + "/rp";
    }
}
