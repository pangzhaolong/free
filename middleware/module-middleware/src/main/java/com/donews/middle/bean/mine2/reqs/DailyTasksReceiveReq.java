package com.donews.middle.bean.mine2.reqs;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

/**
 * @author lcl
 * Date on 2022/5/10
 * Description:
 * 领取任务的请求参数
 */
public class DailyTasksReceiveReq extends BaseCustomViewModel {
    /**
     * 任务id
     */
    public long id;
    /**
     * 任务类型
     * none: 领取时可用，领取全部
     * turntable: 转盘
     * collect: 集卡
     * lottery: 抽奖
     * share: 分享
     * sign: 签到
     * video: 视频
     * taskvideo: 每日任务视频
     * giftbox: 宝箱
     */
    public String type;

}

