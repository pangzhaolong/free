package com.donews.middle.bean.front;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WinningRotationBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<WinnerItem> list;

    public List<WinnerItem> getList() {
        return list;
    }

    public static class WinnerItem {
        @SerializedName("name")
        private String name;
        @SerializedName("message")
        private String message;
        @SerializedName("avatar")
        private String avatar;

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
