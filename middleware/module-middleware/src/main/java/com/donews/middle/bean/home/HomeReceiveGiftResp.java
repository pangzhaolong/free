package com.donews.middle.bean.home;

import com.donews.common.contract.BaseCustomViewModel;

/**
 * 领取礼盒的请求
 */
public class HomeReceiveGiftResp extends BaseCustomViewModel {
    // 获得的金币数量
    public int coin;
    // 剩余暴击次数(剩余次数)
    public int rest_times;
}
