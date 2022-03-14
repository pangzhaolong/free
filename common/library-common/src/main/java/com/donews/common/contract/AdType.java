package com.donews.common.contract;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/10 15:27<br>
 * 版本：V1.0<br>
 */
public class AdType {
    /**
     *
     */

    /**
     * luckGold: 幸运金币
     * home: 首页充电领取奖励
     * homeOne: 首页模拟充电第一阶段
     * homeTwo: 首页模拟充电第二阶段
     * gameGold: 充电游戏
     * taskDraw: 任务领取
     * battery: 收取电池
     */
    /**
     * private GameGoldBean gameGold;
     * private HomeBean home;
     * private HomeOneBean homeOne;
     * private HomeTwoBean homeTwo;
     * private LuckGoldBean luckGold;
     * private TaskDrawBean taskDraw;
     * private WelfareBean welfare;
     */
    public final static int LUCK_GOLD = 1;
    public final static int HOME_RECHARGE = 2;
    public final static int HOME_ONE_RECHARGE = 3;
    public final static int HOME_TWO_RECHARGE = 4;
    public final static int GAME_GOLD = 5;
    public final static int TASK_DRAW = 6; // 任务列表
    public final static int WELFARE = 7;
    public final static int TASK_DRAW_GOLD = 9; //任务列表看激励视频
    public final static int NEWCONER_WELFARE = 8;
    public final static int WEB_VIDEO = 10; //webview 看激励视频

    public final static int RED_PACKET = 15; //弹窗，弹出红包


    public final static int PAGE_ONE = 1;  // 领取金币页面
    public final static int PAGE_TWO = 2;  // 激励视频
    public final static int PAGE_THREE = 3; // 得到金币页面
    public final static int PAGE_FOUR = 4; // 判断信息流模板是否能点击
    public final static int PAGE_FIVE = 5; // 关闭之后是否播放激励视频
    public final static int PAGE_SIX = 6; // 积分墙点击
}
