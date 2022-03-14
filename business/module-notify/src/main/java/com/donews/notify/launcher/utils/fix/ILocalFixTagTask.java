package com.donews.notify.launcher.utils.fix;

/**
 * @author lcl
 * Date on 2022/1/6
 * Description:
 * 本地标签的任务接口
 */
public interface ILocalFixTagTask {
    /**
     * 获取本地标签的值
     * @param tag 当前的标签名称
     * @return
     */
    String getTagValue(String tag);
}
