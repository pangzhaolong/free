package com.donews.mine.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardHistoryBean extends BaseCustomViewModel {
    @SerializedName("list")
    private List<RewardBean> list;
    @SerializedName("total")
    private String total;

    public List<RewardBean> getList() {
        return list;
    }

    public void setList(List<RewardBean> list) {
        this.list = list;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public static class RewardBean  extends BaseCustomViewModel{
        @SerializedName("period")
        private Integer period;
        @SerializedName("code")
        private String code;

        public Integer getPeriod() {
            return period;
        }

        public void setPeriod(Integer period) {
            this.period = period;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
