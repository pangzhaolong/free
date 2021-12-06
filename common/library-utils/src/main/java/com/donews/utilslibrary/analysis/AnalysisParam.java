package com.donews.utilslibrary.analysis;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/22 17:47<br>
 * 版本：V1.0<br>
 */
public final class AnalysisParam {

    public static String STARTUP = "startup"; //App启动
    public static String SHUTDOWN = "shutdown"; //App退出
    public static String LOGIN = "login"; //App账号登录
    public static String LOGOUT = "logout"; //App退出账号登录
    public static String ERROR = "error"; //各种原因崩溃记录
    public static String OS_UPGRADE = "os_upgrade"; //升级操作系统
    public static String APP_UPGRADE = "app_upgrade"; //升级app版本

    public static String CLICK_GOLD = "click_gold"; //点击首页幸运金币
    public static String LOOK_GOLD = "look_gold"; //点击幸运金币跳转到幸运金币展示信息流领取页面
    public static String CLICK_BATTERYGOLD = "click_batterygold"; //点击首页充电奖励按钮（不含跳出激励视频频次限制）
    public static String LOOK_BATTERYGOLD = "look_batterygold"; //首页充电奖励跳转展示信息流广告领取页面
    public static String LOOK_SIMULATIONBATTERYGOLD_1 = "look_simulationbatterygold_1"; //首页模拟充电第一阶段奖励金币展示信息流领取页面弹出
    public static String LOOK_SIMULATIONBATTERYGOLD_2 = "look_simulationbatterygold_2"; //首页模拟充电第二阶段奖励金币展示信息流领取页面弹出
    public static String TO_BATTERYGAME = "to_batterygame"; //打开充电游戏页面
    public static String LOOK_BATTERYGAMEGOLD = "look_batterygamegold"; //点击幸运金币跳转到幸运金币展示信息流领取页面

    public static String TO_BENEFIT_BOTTOM_NAV = "to_benefit_bottom_nav"; //点击底部导航栏进入福利页面
    public static String TO_BENEFIT_CASH = "to_benefit_cash"; //点击提现页面完成福利任务提示进入福利页面
    public static String TO_BENEFIT_JOB = "to_benefit_job"; //
    public static String LOOK_BENEFIT = "look_benefit"; //点击领取福利跳转到激励视频
    public static String CLICK_BENEFIT_HEART = "click_benefit_heart"; //点击收下红心（统计增加）
    public static String LOOK_JOB_LOOK = "look_job_look"; //创意视频完整播放


    /** Swei桌面通知新增的事件 */
    public static final String DESKTOP_DISPLAY = "desktopdisplay";
    public static final String DESKTOP_DISPLAY_CLICK = "desktopclicking";
    public static final String NOTICE_BAR_DISPLAY = "noticebardisplay";
    public static final String NOTICE_BAR_CLICK = "noticebarClick";

}
