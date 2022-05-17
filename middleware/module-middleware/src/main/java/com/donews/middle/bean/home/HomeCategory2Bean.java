package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeCategory2Bean extends BaseCustomViewModel {

    @SerializedName("list")
    public List<Category2Item> list;

    public static class Category2Item extends BaseCustomViewModel {
        //类目ID
        @SerializedName("category_id")
        public String category_id;
        //类目名称
        @SerializedName("name")
        public String name;
        //类目展示列数
        @SerializedName("cols")
        public int cols;
        //
        @SerializedName("selected")
        public boolean selected;
    }
}
