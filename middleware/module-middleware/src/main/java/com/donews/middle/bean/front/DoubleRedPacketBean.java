package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class DoubleRedPacketBean extends BaseCustomViewModel {

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

    public void setType(int type) {
        this.type = type;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getRestScore() {
        return restScore;
    }

    public void setRestScore(float restScore) {
        this.restScore = restScore;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }
}
