package com.donews.main.bean;

import com.donews.base.model.BaseLiveDataModel;
import com.google.gson.annotations.SerializedName;

public class WallTaskRpBean extends BaseLiveDataModel {

    @SerializedName("type")
    private int type;
    @SerializedName("score")
    private float score;
    @SerializedName("rest_score")
    private float restScore;
    @SerializedName("rest_id")
    private String restId;

    public int getType() {
        return type;
    }

    public float getScore() {
        return score;
    }

    public float getRestScore() {
        return restScore;
    }

    public String getRestId() {
        return restId;
    }
}
