package com.donews.main.bean;

import com.donews.base.model.BaseLiveDataModel;
import com.google.gson.annotations.SerializedName;

public class RetentionTaskBean extends BaseLiveDataModel {

    @SerializedName("handout")
    private Boolean handout;

    public Boolean getHandout() {
        return handout;
    }
}
