package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

/**
 * 赚金币的请求参数
 */
public class HomeEarnCoinReq extends BaseCustomViewModel {
    // 用户id
    public String user_id;
    // 时间戳
    public String timestamp = "" + System.currentTimeMillis() / 1000;
}
