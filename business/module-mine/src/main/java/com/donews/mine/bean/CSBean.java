package com.donews.mine.bean;

import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;

public class CSBean extends HistoryPeopleLotteryDetailResp.SpeedsDTO {
    public CSBean(String city, String speed) {
        this.city = city;
        this.speed = speed;
    }
}
