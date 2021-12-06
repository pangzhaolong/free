package com.donews.common.bean;

import androidx.annotation.Keep;

/**
 * @author by SnowDragon
 * Date on 2021/4/6
 * Description:
 */
@Keep
public class AppGlobalConfigBean{
    public boolean crashIsOpen = false;
    public boolean crashLifecycleHandler = false;

    public boolean keepAliveOpen = false;

    public int keepAliveMinVersion = 24;

    public long keepAliveFirstDelayOpen = 15000;

    public long myKeepAliveFirstDelayOpen = 8000;

    public int refreshInterval = 10;


    public long notifyDelayShowTime = 5000;
    /** notify频度使用，暂时在这配置一个数值吧 */
    public long notifyShowTime = 5000;

    /** 起始实际，即10-14点 */
    public int notifyTimeStart1 = 12;

    /** 起始实际，即16-20点 */
    public int notifyTimeStart2 = 18;


    public String notifyActionAlias = "com.alibaba.sdk.android.login.ui.WebViewActivity";
    public String notifyAlias = "com.sina.weibo.WebWeiboActivity";

    //默认0是hide，1是finish，2是jump
    public int notifyActionType =0;
    public String notifySchemeOpen ="jdd://com.cdyfnts.lottery.jdd.plus";
    //0是small,1是big
    public int notifyShowSizeType = 1;

    public boolean notifyAlwaysShow = false;

    public int notifyShowMaxCount = -1;


    public String notifyLauncherImg;

    public String notifyBarImg;

}
