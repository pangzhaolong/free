package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;

import java.util.List;

public class DataBean extends BaseCustomViewModel {
    List<BannerBean> bannners;
    List<SpecialCategoryBean> special_category;

    public List<BannerBean> getBannners() {
        return bannners;
    }

    public List<SpecialCategoryBean> getSpecial_category() {
        return special_category;
    }

    public void setBannners(List<BannerBean> banners) {
        this.bannners = banners;
    }

    public void setSpecial_category(List<SpecialCategoryBean> special_category) {
        this.special_category = special_category;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "banners=" + bannners +
                ", special_category=" + special_category +
                '}';
    }
}
