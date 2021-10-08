package com.donews.common.services.config;

/**
 * 应用模块:
 * <p>
 * 类描述: 各个组件需要暴露给外部的service名称 配置
 * <p>
 * <p>
 * 作者： created by honeylife<br>
 * 日期： 2020-02-29
 */
public class ServicesConfig {
    private static final String SERVICE = "/service";

    /**
     * 用户模块
     */
    public static class User {
        /**
         * 用户登录状态
         */
        public static final String LONGING_SERVICE = SERVICE + "/login";
        /**
         * 登录成功之后
         */
        public static final String LOGIN_SUCCESS = SERVICE + "/loginSuccess";
    }

    /**
     * 弹窗
     */
    public static class Dialog {
        /**
         * 弹窗服务
         */
        public static final String DIALOG_SERVICE = "/dialog/dialogPage";


        /**
         * 猜字，猜成语，模块
         */
        public static final String DIALOG_SERVICE_GUESS_EVENT = "com.donews.dialog.provider.DialogProvider.guessEvent";

        public static final String DIALOG_SERVICE_GUESS_LOOK_VIDEO = "com.donews.dialog.provider.DialogProvider.guessLookVideo";
        public static final String DIALOG_SERVICE_GUESS_NO_ENERGY = "com.donews.dialog.provider.DialogProvider.guessNoEnergy";
        public static final String DIALOG_SERVICE_GUESS_ANSWER_HINT = "com.donews.dialog.provider.DialogProvider.answerHint";
        public static final String DIALOG_SERVICE_RED_PACKET = "com.donews.dialog.provider.DialogProvider.redPacket";

    }


    /**
     * 设置
     */
    public static class Mina {
        /**
         * 设置服务
         */
        public static final String STTING_SERVIE = "/mina/stting";
    }
}
