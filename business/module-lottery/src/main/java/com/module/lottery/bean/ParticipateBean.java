package com.module.lottery.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ParticipateBean implements Serializable {

    @SerializedName("total")
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }

    @SerializedName("list")
    private List<ListDTO> list;

    public static class ListDTO implements Serializable {
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @SerializedName("id")
        private String id;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("user_name")
        private String userName;
    }
}
