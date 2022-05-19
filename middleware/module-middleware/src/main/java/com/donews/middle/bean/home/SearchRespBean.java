package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchRespBean extends BaseCustomViewModel {

    @SerializedName("list")
    private List<SearchRespItemBean> list;

    public List<SearchRespItemBean> getList() {
        return list;
    }

    public void setList(List<SearchRespItemBean> list) {
        this.list = list;
    }

    public static class SearchRespItemBean extends BaseCustomViewModel {
        //标题
        public String title;
        //商品主图
        public String main_pic;
        //兑换所需金币
        public int coin;
        //兑换所需的活跃度
        public int active;
        //商品id
        public String goods_id;
        //是否置顶
        public boolean top;
        //已兑换数量
        public int amount;
    }
}
