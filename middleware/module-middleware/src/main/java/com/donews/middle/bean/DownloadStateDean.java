package com.donews.middle.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class DownloadStateDean extends BaseCustomViewModel {


    public Boolean getHandout() {
        return handout;
    }

    public void setHandout(Boolean handout) {
        this.handout = handout;
    }

    @SerializedName("handout")
    private Boolean handout;

}
