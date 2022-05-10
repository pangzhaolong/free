package com.donews.middle.bean.mine2.emuns;

/**
 * @author lcl
 * Date on 2022/5/7
 * Description:
 */
public enum Mine2TaskType {
    Null("null"), //不存在此类型(没有这个支持的类型)
    none("none"), //领取时可用，领取全部
    turntable("turntable"), // 转盘
    collect("collect"), // 集卡
    lottery("lottery"), // 抽奖
    share("share"), // 分享
    sign("sign"), // 签到
    video("video"), // 视频(活动列表视频)
    taskvideo("taskvideo"), // 任务视频(每日任务)
    giftbox("giftbox"); // 宝箱

    public String type;
    public Runnable clickTask;

    Mine2TaskType(String type) {
        this.type = type;
    }

    /**
     * 根据类型查找类型
     *
     * @param type
     * @return null：表示不存在类型,
     */
    public static Mine2TaskType query(String type) {
        for (Mine2TaskType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return Null;
    }

}
