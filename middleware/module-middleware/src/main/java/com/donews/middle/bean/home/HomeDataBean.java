package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;
import com.donews.middle.bean.home.HomeGridCategoryBean;

import java.util.List;

public class HomeDataBean extends BaseCustomViewModel {
//    List<BannerBean> bannners;
    List<HomeGridCategoryBean> special_category;

//    public List<BannerBean> getBannners() {
//        return bannners;
//    }

    public List<HomeGridCategoryBean> getSpecial_category() {
        return special_category;
    }

//    public void setBannners(List<BannerBean> banners) {
//        this.bannners = banners;
//    }

    public void setSpecial_category(List<HomeGridCategoryBean> special_category) {
        this.special_category = special_category;
    }

    @Override
    public String toString() {
        return "DataBean{" +
//                "banners=" + bannners +
                ", special_category=" + special_category +
                '}';
    }
}
