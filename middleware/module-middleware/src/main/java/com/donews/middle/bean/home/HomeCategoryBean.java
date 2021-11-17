package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeCategoryBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<CategoryItem> list;

    public List<CategoryItem> getList() {
        return list;
    }

    public void setList(List<CategoryItem> list) {
        this.list = list;
    }

    public static class CategoryItem extends BaseCustomViewModel {
        @SerializedName("cid")
        private String cid;
        @SerializedName("cname")
        private String cname;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }
    }
}
