package com.donews.share;

/**
 * @Author: honeylife
 * @CreateDate: 2020/4/26 19:32
 * @Description:
 */
public interface ISWXSuccessCallBack {
    /**
     * 成功的回调
     */
    void onSuccess(int state,String code);

    /**
     * 失败了的回调
     */
    void onFailed(String msg);
}
