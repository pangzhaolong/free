package com.module.lottery.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WinLotteryBean implements Serializable {



    @SerializedName("list")
    private List<ListDTO> list;

    public static class ListDTO implements Serializable {
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("message")
        private String message;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }


}
