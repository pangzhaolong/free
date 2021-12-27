package com.module.integral.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;
/**
 * 积分下载状态(服务器返回)
 *
 * */
public class IntegralDownloadStateDean extends BaseCustomViewModel {
    public Boolean getHandout() {
        return handout;
    }

    public void setHandout(Boolean handout) {
        this.handout = handout;
    }

    @SerializedName("handout")
    private Boolean handout;
}
