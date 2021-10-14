package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchSugBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<SugItem> list;

    public List<SugItem> getList() {
        return list;
    }

    public void setList(List<SugItem> list) {
        this.list = list;
    }

    public static class SugItem {
        @SerializedName("kw")
        private String kw;
        @SerializedName("total")
        private int total;

        public String getKw() {
            return kw;
        }

        public void setKw(String kw) {
            this.kw = kw;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
