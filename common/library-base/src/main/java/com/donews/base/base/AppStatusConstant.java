package com.donews.base.base;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2021/1/6 10:41<br>
 * 版本：V1.0<br>
 */
public class AppStatusConstant {
    public static final int STATUS_FORCE_KILLED = -1; //应用放在后台被强杀了
    public static final int STATUS_NORMAL = 2;  //APP正常态//intent到MainActivity 区分跳转目的
    public static final String KEY_HOME_ACTION = "key_home_action";//返回到主页面
    public static final int ACTION_BACK_TO_HOME = 0; //默认值
    public static final int ACTION_RESTART_APP = 1;//被强杀
}
