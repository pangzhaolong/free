package com.donews.middle.bean.mine2.resp;

import com.donews.common.contract.BaseCustomViewModel;

import java.util.List;

/**
 * @author lcl
 * Date on 2022/5/7
 * Description:
 */
public class DailyTaskResp extends BaseCustomViewModel {
    public List<DailyTaskItemResp> list;

    /**
     * 每个任务的实体
     */
    public static class DailyTaskItemResp extends BaseCustomViewModel {
        /**
         * id
         */
        public long id;
        /**
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
        /**
         * 活动的Icon
         */
        public String icon;
        /**
         * 任务名称
         */
        public String title;
        /**
         * 已完成数量
         */
        public long done;
        /**
         * 任务总数
         */
        public long total;
        /**
         * 完成状态
         * 0=未完成  1=完成可领取  2=已领取
         */
        public long status;
        /**
         * 描述信息
         */
        public String desc;
    }
}

