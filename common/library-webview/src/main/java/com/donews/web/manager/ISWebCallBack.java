package com.donews.web.manager;

/**
 * @Author: honeylife
 * @CreateDate: 2020/5/11 16:42
 * @Description:
 */
public interface ISWebCallBack {

    /**
     * 加载成功
     */
    void onFinishUrl();

    /**
     * 进度走完了
     */
    void onProgress();

    /**
     * title的值
     */
    void onTitleName(String titleName);

    /**
     * 设置加载的flag状态
     */
    void isFlag(boolean isFlag);


}
