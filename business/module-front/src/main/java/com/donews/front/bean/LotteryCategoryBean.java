package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LotteryCategoryBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<categoryBean> list;

    public List<categoryBean> getList() {
        return list;
    }

    public void setList(List<categoryBean> list) {
        this.list = list;
    }

    public static class categoryBean  extends BaseCustomViewModel{
        @SerializedName("category_id")
        private String categoryId;
        @SerializedName("name")
        private String name;
        @SerializedName("cols")
        private Integer cols;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCols() {
            return cols;
        }

        public void setCols(Integer cols) {
            this.cols = cols;
        }
    }
}
