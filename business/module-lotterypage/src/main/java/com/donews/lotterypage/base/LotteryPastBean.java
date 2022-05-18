package com.donews.lotterypage.base;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LotteryPastBean extends BaseCustomViewModel {

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    @SerializedName("list")
        private List<ListDTO> list;

        public static class ListDTO implements Serializable {

            @SerializedName("name")
            private String name;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("produce_name")
            private String produceName;
            public String getName() {
                return name;
            }

            public void setName(String name) {
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
