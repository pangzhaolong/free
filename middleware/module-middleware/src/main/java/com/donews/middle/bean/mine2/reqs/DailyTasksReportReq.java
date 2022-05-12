package com.donews.middle.bean.mine2.reqs;

/**
 * @author lcl
 * Date on 2022/5/10
 * Description:
 * 领取任务的请求参数
 */
public class DailyTasksReportReq extends DailyTasksReceiveReq {
    /**
     * 当前的时间戳(单位秒)
     */
    public String timestamp = System.currentTimeMillis() / 1000 + "";

}

