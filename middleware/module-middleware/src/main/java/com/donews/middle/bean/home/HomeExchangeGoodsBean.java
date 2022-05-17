package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 兑换的首页数据实体对象
 */
public class HomeExchangeGoodsBean extends BaseCustomViewModel {


    @SerializedName("list")
    private List<SearchRespBean.SearchRespItemBean> list;

    public List<SearchRespBean.SearchRespItemBean> getList() {
        return list;
    }

    public void setList(List<SearchRespBean.SearchRespItemBean> list) {
        this.list = list;
    }
}
