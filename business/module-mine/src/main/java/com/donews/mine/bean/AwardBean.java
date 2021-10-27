package com.donews.mine.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AwardBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<AwardInfo> list;

    public List<AwardInfo> getList() {
        return list;
    }

    public void setList(List<AwardInfo> list) {
        this.list = list;
    }

    public static class AwardInfo {
        @SerializedName("name")
        private String name;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("produce_name")
        private String produceName;

        public String getName() {
            return name;
        }

        public void setNameX(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getProduceName() {
            return produceName;
        }

        public void setProduceName(String produceName) {
            this.produceName = produceName;
        }
    }
}
