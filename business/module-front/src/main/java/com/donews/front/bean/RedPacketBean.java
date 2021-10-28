package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class RedPacketBean extends BaseCustomViewModel {

    @SerializedName("award")
    private AwardBean award;

    public AwardBean getAward() {
        return award;
    }

    public void setAward(AwardBean award) {
        this.award = award;
    }

    public static class AwardBean  extends BaseCustomViewModel{
        @SerializedName("type")
        private int type;
        @SerializedName("score")
        private float score;

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
    }
}
