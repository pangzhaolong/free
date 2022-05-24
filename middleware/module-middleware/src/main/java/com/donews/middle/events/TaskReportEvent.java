package com.donews.middle.events;

/**
 * @author lcl
 * Date on 2022/5/23
 * Description:
 *  任务上报事件
 */
public class TaskReportEvent {
    /**
     * 上报的类型。任务类型，参考:
     * {@link com.donews.middle.bean.mine2.emuns.Mine2TaskType}
     */
    public String eventType;

    public TaskReportEvent(String eventType) {
        this.eventType = eventType;
    }
}
