package com.donews.front.bean;

import com.donews.common.contract.BaseCustomViewModel;

public class AwardBean extends BaseCustomViewModel {

    public String avatar;
    public String name;
    public String award;

    public AwardBean(String avatar, String name, String award) {
        this.avatar = avatar;
        this.name = name;
        this.award = award;
    }


}
