package com.donews.web.manager;

/**
 * @Author: honeylife
 * @CreateDate: 2020/6/2 11:29
 * @Description:
 */
public interface ISFinishCallBack {
    /**
     * 加载成功
     */
    void onFinishUrl();

    /**
     * title的值
     */
    void onTitleName(String titleName);
}
