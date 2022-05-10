package com.donews.middle.bean.mine2.reqs;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

/**
 * @author lcl
 * Date on 2022/5/7
 * Description:
 * 签到的请求参数
 */
public class SignReq extends BaseCustomViewModel {
    /** 是否双倍 */
    @SerializedName("double")
    public boolean double_ = false;

}

