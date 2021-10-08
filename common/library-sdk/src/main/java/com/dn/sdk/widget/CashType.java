package com.dn.sdk.widget;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2021/3/30 16:39<br>
 * 版本：V1.0<br>
 */
public enum CashType {
    TYPE_101(101, "请绑定微信", ""),
    TYPE_102(102, "提现申请已提交，\n审核预计3~5个工作日", "我知道了"),
    TYPE_103(103, "系统当日提现已满,\n请明日再试", "我知道了"),
    TYPE_104(104, "当前金币不足，请参与更多\n的活动赚取金币", "去赚钱"),
    TYPE_105(105, "不满足新用户条件", "我知道了"),
    TYPE_106(106, "单个用户每日仅能提现一次", "我知道了"),
    TYPE_107(107, "请完成专享任务", "我知道了"),
    TYPE_108(108, "请完成签到专享", "我知道了"),
    TYPE_109(109, "请完成福利活动", "我知道了"),
    TYPE_110(110, "请完成新手签到提现", "我知道了"),
    TYPE_111(111, "系统当日提现已满,\n请明日再试", "我知道了"),
    TYPE_112(112, "系统当日提现已满,\n请明日再试", "我知道了"),
    TYPE_113(113, "系统当日提现已满,\n请明日再试", "我知道了"),
    TYPE_0(0, "恭喜您提现成功", "我知道了");
    public int CODE;
    public String MSG;
    public String okName;

    CashType(int code, String msg, String ok) {
        CODE = code;
        MSG = msg;
        okName = ok;
    }
}
