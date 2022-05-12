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
        @SerializedName("name")
        private String name;
        @SerializedName("times")
        private Integer times;
        @SerializedName("human_time")
        private String humanTime;
        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getTimes() {
            return times;
        }

        public void setTimes(Integer times) {
            this.times = times;
        }

        public String getHumanTime() {
            return humanTime;
        }

        public void setHumanTime(String humanTime) {
            this.humanTime = humanTime;
        }


    }

    public List<ListDTO> getList() {
        return list;
    }

    public void setList(List<ListDTO> list) {
        this.list = list;
    }
}
